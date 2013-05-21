package com.socklabs.nemail;

import org.joda.time.DateTime;

import javax.annotation.Nullable;
import javax.mail.Header;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * A representation of an email received by the application.
 */
public class Email {
	private final String messageId;
	private final List<String> tos;
	private final String from;
	private final String subject;
	private final String body;
	private final Map<String, String> headers;
	private final DateTime receivedAt;
	private final String rawMessage;

	public Email(final String rawMessage) throws MessagingException {

		this.headers = new HashMap<String, String>();

		final Session s = Session.getDefaultInstance(new Properties());
		final InputStream is = new ByteArrayInputStream(rawMessage.getBytes());
		final MimeMessage message = new MimeMessage(s, is);
		for (final Enumeration<Header> e = message.getAllHeaders(); e.hasMoreElements();) {
			final Header h = e.nextElement();
			headers.put(h.getName().toLowerCase(), h.getValue());
		}

		this.messageId = headers.get("message-id");
		this.tos = Arrays.asList(headers.get("to").split(","));
		this.from = headers.get("from");
		this.subject = headers.get("subject");
		this.body = "";
		this.receivedAt = new DateTime();
		this.rawMessage = rawMessage;
	}

	public DateTime getReceivedAt() {
		return receivedAt;
	}

	public String getRawMessage() {
		return rawMessage;
	}

	public String getMessageId() {
		return messageId;
	}

	public List<String> getTos() {
		return tos;
	}

	public String getFrom() {
		return from;
	}

	public String getSubject() {
		return subject;
	}

	public String getBody() {
		return body;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	@Override
	public boolean equals(@Nullable final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Email email = (Email) o;

		if (!rawMessage.equals(email.rawMessage)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return rawMessage.hashCode();
	}

	public boolean isTo(final String to) {
		return tos.contains(to);
	}

	@Override
	public String toString() {
		return "Email{" +
				"rawMessage='" + rawMessage + '\'' +
				'}';
	}
}
