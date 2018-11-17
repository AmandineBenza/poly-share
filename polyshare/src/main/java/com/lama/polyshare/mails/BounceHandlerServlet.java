package com.lama.polyshare.mails;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.mail.BounceNotification;
import com.google.appengine.api.mail.BounceNotificationParser;

public class BounceHandlerServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7411674201504140429L;

	@Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            BounceNotification bounce = BounceNotificationParser.parse(req);
            System.out.println("Bounced email notification.");
            
            // The following data is available in a BounceNotification object
            // bounce.getOriginal().getFrom()
            // bounce.getOriginal().getTo()
            // bounce.getOriginal().getSubject()
            // bounce.getOriginal().getText()
            // bounce.getNotification().getFrom()
            // bounce.getNotification().getTo()
            // bounce.getNotification().getSubject()
            // bounce.getNotification().getText()
            // ...
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
	

}
