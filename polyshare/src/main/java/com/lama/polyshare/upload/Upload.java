package com.lama.polyshare.upload;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.gson.JsonObject;
import com.lama.polyshare.commons.JSONUtils;
import com.lama.polyshare.datastore.model.User;


@MultipartConfig()
public class Upload extends HttpServlet {

	//	private static final long serialVersionUID = 1L;
	//

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


		CloudStorageHelper storageHelper =	new CloudStorageHelper();

		BlobInfo docInformation =
				storageHelper.getImageOrTxtUrl(
						req, resp, "staging.poly-share.appspot.com");//"polyshare.appspot.com");

//		JsonObject root = JSONUtils.fromJson(req.getReader(), JsonObject.class);
//		System.out.println(root);
//		String event = root.get("event").getAsString();
//		String result = "Empty.";
		
		JsonObject root = new JsonObject();
		JsonObject userData = new JsonObject();
		root.addProperty("event","edit-user");
		User plasticUser = new User(); 
		
		
		String downloadLink = docInformation.getMediaLink();
		long fileSize = docInformation.getSize();
		plasticUser.setLastSendDate(new Date());
		plasticUser.setMail(req.getParameter("mailAdress"));
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withUrl("/worker/upload")
				.payload(JSONUtils.toJson(plasticUser))
				.param("downloadLink", downloadLink)
				.param("fileSize",String.valueOf(fileSize)));
		
		Blob b = storageHelper.downloadFile("staging.poly-share.appspot.com", "Script.py-2018-11-14-220218976");
//		b.getContent(BlobSourceOption.);
		try {
			resp.getWriter().write(downloadLink);
		} catch (Exception e) {
			throw new ServletException("Error updating book", e);
		}
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {


	}
}