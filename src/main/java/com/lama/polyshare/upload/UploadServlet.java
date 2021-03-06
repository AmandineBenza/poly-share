package com.lama.polyshare.upload;

import java.io.IOException;

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
import com.lama.polyshare.datastore.model.EnumUserRank;
import com.lama.polyshare.datastore.model.UserManager;


/***
 * Servlet pour l'upload de fichier
 */
@MultipartConfig(maxFileSize = 10*1024*1024,maxRequestSize = 20*1024*1024,fileSizeThreshold = 50*1024*1024)
public class UploadServlet extends HttpServlet {

	private static final String BUCKET_NAME = System.getenv("BUCKET_NAME");
	private static Storage storage = null;

	@Override
	public void init() {
		storage = StorageOptions.getDefaultInstance().getService();
	}

	static private final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		CloudStorageHelper storageHelper = new CloudStorageHelper();
		String fileSizeS = req.getParameter("generatedFileSize");
		BlobInfo docInformation = null;
		
		if(fileSizeS == null ) {
			docInformation = storageHelper.getImageOrTxtUrl(req, resp, "staging.poly-share.appspot.com");// "polyshare.appspot.com");
			
		} else {
			docInformation = storageHelper.getDevDebugTestAPIImageOrTxtUrl(Integer.parseInt(fileSizeS), req.getParameter("reqFileName"), "staging.poly-share.appspot.com"); 
		}
		
		String downloadLink = docInformation.getMediaLink();
		long fileSize = docInformation.getSize();
		String fileName = docInformation.getName();
		
		Entity plasticUser = UserManager.instance.buildUser(req.getParameter("mail"), Timestamp.now(),
				EnumUserRank.NOOB, 0, 0);
		
		JsonObject root =new JsonObject();
		root.addProperty("mail", plasticUser.getString("mail"));
		root.addProperty("event", "edit-user");
		
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withUrl("/taskqueues/datastoreUpload").param("data",root.toString())
				.param("downloadLink", downloadLink).param("fileName", fileName).param("fileSize", ((Long)fileSize).toString()));

		try {
			 resp.getWriter().write(fileName);
		} catch (Exception e) {
			throw new ServletException("Error updating book", e);
		}
	}
}