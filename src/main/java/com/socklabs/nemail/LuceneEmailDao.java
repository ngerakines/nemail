package com.socklabs.nemail;

import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LuceneEmailDao implements EmailDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(LuceneEmailDao.class);

	private final List<String> recentEmails = new LinkedList<String>();
	private final Directory index;
	private final IndexWriterConfig config;
	private final Cache<String, Email> emailCache;
	private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
	private final StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_43);

	public LuceneEmailDao() {
		index = new RAMDirectory();
		config = new IndexWriterConfig(Version.LUCENE_43, analyzer);

		emailCache  = CacheBuilder.newBuilder()
				.maximumSize(1000)
				.expireAfterWrite(10, TimeUnit.MINUTES)
				.removalListener(new RemovalListener<String, Email>() {
					@Override
					public void onRemoval(final RemovalNotification<String, Email> stringLongRemovalNotification) {
						lock.writeLock().lock();
						try {
							final IndexWriter indexWriter = new IndexWriter(index, config);
							indexWriter.deleteDocuments(new Term("id", stringLongRemovalNotification.getKey()));
							indexWriter.close();
						} catch (IOException e) {
							LOGGER.error("Ooops", e);
						} finally {
							lock.writeLock().unlock();
						}
					}
				})
				.build();
	}

	@Override
	public void store(Email email) {
		emailCache.put(email.getMessageId(), email);

		recentEmails.add(email.getMessageId());
		if (recentEmails.size() > 100) {
			recentEmails.remove(0);
		}

		final Document doc = new Document();
		doc.add(new StringField("id", email.getMessageId(), Field.Store.YES));
		doc.add(new LongField("received_at", email.getReceivedAt().getMillis(), Field.Store.NO));
		doc.add(new TextField("subject", new StringReader(email.getSubject())));
		doc.add(new TextField("from", email.getFrom(), Field.Store.NO));
		for (final String to: email.getTos()) {
			doc.add(new TextField("to", to, Field.Store.NO));
		}

		try {
			lock.writeLock().lock();
			final IndexWriter indexWriter = new IndexWriter(index, config);
			indexWriter.addDocument(doc);
			indexWriter.close();
		} catch (final IOException e) {
			LOGGER.error("Ooops", e);
		} finally {
			lock.writeLock().unlock();
		}
	}

	@Override
	public Optional<Email> findById(String id) {
		return Optional.fromNullable(emailCache.getIfPresent(id));
	}

	@Override
	public List<Email> findBySender(String from) {
		try {
			return search(new QueryParser(Version.LUCENE_43, "from", analyzer).parse(from));
		} catch (ParseException e) {
			return new ArrayList<Email>();
		}
	}

	@Override
	public List<Email> findByReceiver(String to) {
		try {
			return search(new QueryParser(Version.LUCENE_43, "to", analyzer).parse(to));
		} catch (ParseException e) {
			return new ArrayList<Email>();
		}
	}

	@Override
	public List<Email> findBySubject(String subject) {
		try {
			return search(new QueryParser(Version.LUCENE_43, "subject", analyzer).parse(subject));
		} catch (ParseException e) {
			return new ArrayList<Email>();
		}
	}

	@Override
	public List<Email> listRecent() {
		return collectEmails(recentEmails);
	}

	/**
	 * Executes a lucene query against the index.
	 */
	private List<Email> search(final Query query) {
		final List<String> emails = new ArrayList<String>();
		try {
			final IndexReader reader = DirectoryReader.open(index);
			final IndexSearcher searcher = new IndexSearcher(reader);

			final TopScoreDocCollector collector = TopScoreDocCollector.create(10, true);
			searcher.search(query, collector);

			final ScoreDoc[] hits = collector.topDocs().scoreDocs;
			for (final ScoreDoc hit : hits) {
				final int docId = hit.doc;
				final Document d = searcher.doc(docId);
				LOGGER.info("doc {}", d);
				final String messageId = d.get("id");
				emails.add(messageId);
			}

			reader.close();
		} catch (final IOException e) {
			LOGGER.error("oops", e);
		}
		return collectEmails(emails);
	}

	/**
	 * For a list of message ids, return email objects if they can be found.
	 */
	private List<Email> collectEmails(final List<String> emailIds) {
		final List<Email> emails = new ArrayList<Email>(emailIds.size());
		for (final String messageId : emailIds) {
			final Email possibleEmail = emailCache.getIfPresent(messageId);
			if (possibleEmail != null) {
				emails.add(possibleEmail);
			}
		}
		return emails;
	}

}
