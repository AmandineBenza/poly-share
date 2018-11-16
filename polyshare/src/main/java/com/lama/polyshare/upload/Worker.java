package com.lama.polyshare.upload;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(
		name = "TaskWorker",
		description = "TaskQueues: worker",
		urlPatterns = "/taskqueues/worker"
		)
public class Worker extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(Worker.class.getName());

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String key = request.getParameter("id");
		PrintWriter x = response.getWriter();
		x.write(key);
	}
}