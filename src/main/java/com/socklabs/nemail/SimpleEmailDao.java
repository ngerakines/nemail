package com.socklabs.nemail;

import com.google.common.base.Optional;

import java.util.*;

public class SimpleEmailDao implements EmailDao {

	private final Set<Email> emails = new HashSet<Email>();
	private final List<Email> recentEmails = new LinkedList<Email>();

	@Override
	public void store(final Email email) {
		emails.add(email);
		recentEmails.add(email);
		if (recentEmails.size() > 100) {
			recentEmails.remove(0);
		}
	}

	@Override
	public Optional<Email> findById(final String id) {
		for (final Email email : emails) {
			if (email.getMessageId().equals(id)) {
				return Optional.of(email);
			}
		}
		return Optional.absent();
	}

	@Override
	public List<Email> findBySender(final String from) {
		final List<Email> matches = new ArrayList<Email>();
		for (final Email email : emails) {
			if (email.getFrom().equals(from)) {
				matches.add(email);
			}
		}
		return matches;
	}

	@Override
	public List<Email> findByReceiver(final String to) {
		final List<Email> matches = new ArrayList<Email>();
		for (final Email email : emails) {
			if (email.isTo(to)) {
				matches.add(email);
			}
		}
		return matches;
	}

	@Override
	public List<Email> findBySubject(final String subject) {
		final List<Email> matches = new ArrayList<Email>();
		for (final Email email : emails) {
			final String emailSubject = email.getSubject();
			if (emailSubject.contains(subject)) {
				matches.add(email);
			}
		}
		return matches;
	}

	@Override
	public List<Email> listRecent() {
		return recentEmails;
	}

}
