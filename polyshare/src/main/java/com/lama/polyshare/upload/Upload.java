package com.lama.polyshare.upload;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Entity;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.JsonObject;
import com.lama.polyshare.commons.JSONUtils;
import com.lama.polyshare.datastore.model.EnumUserRank;
import com.lama.polyshare.datastore.model.UserManager;

@MultipartConfig()
public class Upload extends HttpServlet {

	private static final String BUCKET_NAME = System.getenv("BUCKET_NAME");
	private static Storage storage = null;

	@Override
	public void init() {
		storage = StorageOptions.getDefaultInstance().getService();
	}

	static private final long serialVersionUID = 1L;
	static private final Logger log = Logger.getLogger(Worker.class.getName());

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		
		CloudStorageHelper storageHelper = new CloudStorageHelper();
		BlobInfo docInformation = storageHelper.getImageOrTxtUrl(req, resp, "staging.poly-share.appspot.com");// "polyshare.appspot.com");

		JsonObject root = new JsonObject();
		root.addProperty("event", "edit-user");

		String downloadLink = docInformation.getMediaLink();
		long fileSize = docInformation.getSize();
		String fileName = docInformation.getName();
		// TODO CHECK
		Entity plasticUser = UserManager.instance.buildUser(req.getParameter("mail"), Timestamp.now(),
				EnumUserRank.NOOB, 0, 0);

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withUrl("/worker/upload").payload(JSONUtils.toJson(plasticUser))
				.param("downloadLink", downloadLink).param("fileName", fileName).param("fileSize", String.valueOf(fileSize)));

		try {
			// resp.getWriter().write(downloadLink);
		} catch (Exception e) {
			throw new ServletException("Error updating book", e);
		}
	}
}