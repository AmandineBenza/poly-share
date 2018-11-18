package com.lama.polyshare;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/***
 * Servlet pour tester le bon depploiement de l'application 
 */
public class ServletYo extends HttpServlet {

	private static final long serialVersionUID = -343983157453804598L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain");
		response.getWriter().println("Yo");
	}

}
