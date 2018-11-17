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


public class ServletSendMails extends HttpServlet {

	private static final long serialVersionUID = 2316390422015026794L;
	
	public ServletSendMails() {
	}
	
	private final static ServletSendMails INSTANCE = new ServletSendMails();

	public static ServletSendMails getInstance(){
		return INSTANCE;
	}
	
	// https://cloud.google.com/appengine/docs/standard/java/mail/sending-mail-with-mail-api

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
//		resp.getWriter().print("Sending simple email.");
//		sendMail("amandinebenza@gmail.com", "itsdamoy@gmail.com", "Damien Fornali", "DRAGIBUS", "IT'S WORKING");
//		sendMail("amandinebenza@gmail.com", "amandinebenza@gmail.com", "Amandine Benza", "DRAGIBUS", "IT'S WORKING");
//		sendNoob("amandine@benza.fr");
	};

//	@Override
//	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
//		  String type = req.getParameter("type");
//    if (type != null && type.equals("multipart")) {
//      resp.getWriter().print("Sending HTML email with attachment.");
//      sendMultipartMail();
//    } else {
//      resp.getWriter().print("Sending simple email.");
//      sendMail();
//    }
//	}

	public void sendMail(String sender, String recipient, String recipientName, String subject, String body) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(sender));
			msg.addRecipient(Message.RecipientType.TO,
					new InternetAddress(recipient, "Dear Mr or Ms " + recipientName));
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
	
	public void sendUploadMail(String recipient, String link) {
		sendMail("amandinebenza@gmail.com", recipient, "", "Successful upload of your file", link);	
	}
	
	public void sendDownloadMail(String recipient, String link) {
		sendMail("amandinebenza@gmail.com", recipient, "", "Download link for your file", link);	
	}
	
	public void sendNoob(String recipient) {
		sendMail("amandinebenza@gmail.com", recipient, "", "Here's your link", "Lol. No noob.");	
	}
    
	
}