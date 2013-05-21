package com.socklabs.nemail;

import org.subethamail.smtp.helper.SimpleMessageListener;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * An SMTPD listener that attempts to store received emails with the email dao.
 */
public class DefaultSimpleMessageListener implements SimpleMessageListener {

	private final EmailDao emailDao;

	public DefaultSimpleMessageListener(final EmailDao emailDao) {
		this.emailDao = emailDao;
	}

	@Override
	public boolean accept(String from, String recipient) {
		return true;
	}

	@Override
	public void deliver(String from, String recipient, InputStream data) throws IOException {
		final String rawMessage = convertStreamToString(data);
		final Email email;
		try {
			email = new Email(rawMessage);
			emailDao.store(email);
		} catch (MessagingException e) {
			// TODO[NKG]: Clean this up.
			e.printStackTrace();
		}
	}

	private String convertStreamToString(InputStream is) {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		final StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			// TODO[NKG]: Clean this up.
			e.printStackTrace();
		}
		return sb.toString();
	}

}
