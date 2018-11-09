package com.lama.polyshare.mails;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServlet;

public class ServletSendMails extends HttpServlet { 
	
	//https://cloud.google.com/appengine/docs/standard/java/mail/sending-mail-with-mail-api
	
	//Properties props = new Properties();
	//Session session = Session.getDefaultInstance(props, null);

	//try {
	//  Message msg = new MimeMessage(session);
	//  msg.setFrom(new InternetAddress("admin@example.com", "Example.com Admin"));
	//  msg.addRecipient(Message.RecipientType.TO,
	//                   new InternetAddress("user@example.com", "Mr. User"));
	//  msg.setSubject("Your Example.com account has been activated");
	//  msg.setText("This is a test");
	//  Transport.send(msg);
	//} catch (AddressException e) {
	//  // ...
	//} catch (MessagingException e) {
	//  // ...
	//} catch (UnsupportedEncodingException e) {
	//  // ...
	//}

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 2316390422015026794L;

	public void sendMail(String sender,String recipient,String recipientName,String subject,String body){
		
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props,null);
		
		try{ 
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(sender));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient, "Dear Mr or Ms "+ recipientName));
			msg.setSubject(subject);
			msg.setText(body);
			Transport.send(msg);

		} catch (AddressException e) {
			System.out.println("Invalid adress");
		} catch (MessagingException e) {
			System.out.println("Invalid message");
		} catch (UnsupportedEncodingException e) {
			System.out.println("Unsupported Encoding");
		}
	}

}