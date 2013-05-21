package com.socklabs.nemail;

import com.google.common.base.Optional;

import java.util.List;

/**
 * An interface that defines a standard set of email dao actions.
 */
// TODO[NKG]: Add support for multi-value lookup (ie search by to AND subject).
public interface EmailDao {

	void store(final Email email);

	Optional<Email> findById(final String id);

	List<Email> findBySender(final String from);

	List<Email> findByReceiver(final String to);

	List<Email> findBySubject(final String subject);

	List<Email> listRecent();

}
