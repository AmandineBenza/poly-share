package com.lama.polyshare.upload;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;


@WebServlet(
		name = "TaskWorker",
		description = "TaskQueues: worker",
		urlPatterns = "/taskqueues/worker"
		)
public class Worker extends HttpServlet {

	private static final String BUCKET_NAME = System.getenv("BUCKET_NAME");
	private static Storage storage = null;

	@Override
	public void init() {
		storage = StorageOptions.getDefaultInstance().getService();
	}

	static private final long serialVersionUID = 1L;
	static private final Logger log = Logger.getLogger(Worker.class.getName());

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
	ServletException {


	}
}