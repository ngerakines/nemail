package com.socklabs.nemail;

import com.google.common.base.Optional;

import java.util.List;
import java.util.Map;

public interface EmailDao {

	void store(final Email email);

	Optional<Email> findById(final String id);

	List<Email> findBySender(final String from);

	List<Email> findByReceiver(final String to);

	List<Email> findBySubject(final String subject);

	List<Email> listRecent();

}
