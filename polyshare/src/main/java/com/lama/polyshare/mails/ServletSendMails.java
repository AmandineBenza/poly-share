package com.lama.polyshare.mails;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

public class ServletSendMails extends HttpServlet { 
	
	private static final long serialVersionUID = 2316390422015026794L;

	
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
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
	    resp.getWriter().print("Sending simple email.");
		sendMail("amandinebenza@gmail.com", "itsdamoy@gmail.com", "Damien Fornali", "Dragibus", "Smarties");
	};

	  @Override
	  public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
	    String type = req.getParameter("type");
	    if (type != null ) {
	      resp.getWriter().print("Sending simple email.");
	      sendMail("amandinebenza@gmail.com", "itsdamoy@gmail.com", "Damien Fornali", "Dragibus", "Smarties");
	      resp.getWriter().print("Done ?");
	    }
	}
	
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