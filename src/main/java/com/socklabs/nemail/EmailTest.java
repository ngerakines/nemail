package com.socklabs.nemail;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * A small java class that composes and sends a test email.
 */
public class EmailTest {
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "false");
		props.put("mail.smtp.starttls.enable", "false");
		props.put("mail.smtp.host", "localhost");
		props.put("mail.smtp.port", "2500");

		Session session = Session.getInstance(props);

		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress("nick@gerakines.net"));
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("carolyn@gerakines.net"));
		message.setSubject("Love");
		message.setText("Can't wait to be home! -- nkg");

		Transport.send(message);
	}
}
