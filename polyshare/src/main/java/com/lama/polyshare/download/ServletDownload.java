package com.lama.polyshare.download;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobInfo;
import com.google.gson.JsonObject;
import com.lama.polyshare.commons.JSONUtils;
import com.lama.polyshare.datastore.model.EnumUserRank;
import com.lama.polyshare.datastore.model.UserManager;
import com.lama.polyshare.upload.CloudStorageHelper;

@SuppressWarnings("serial")
public class ServletDownload extends HttpServlet  {
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {

		CloudStorageHelper storageHelper =	new CloudStorageHelper();
		Blob b = storageHelper.downloadFile("staging.poly-share.appspot.com", "Script.py-2018-11-14-220218976");
		resp.setCharacterEncoding("UTF-8");

		resp.setContentType("application/octet-stream");   
		resp.setHeader("Content-Disposition","attachment; filename=\"" + "Script.py" + "\"");   

		try (OutputStream out = resp.getOutputStream()){
			out.write(b.getContent());
			out.flush();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
	ServletException {
		CloudStorageHelper storageHelper =	new CloudStorageHelper();

		BlobInfo docInformation =
				storageHelper.getImageOrTxtUrl(
						req, resp, "staging.poly-share.appspot.com");//"polyshare.appspot.com");

		JsonObject root = new JsonObject();
		root.addProperty("event","edit-user");

		// TODO CHECK
		String downloadLink = docInformation.getMediaLink();
		long fileSize = docInformation.getSize();
		
		// TODO CHECK
		Entity plasticUser = UserManager.instance.buildUser(req.getParameter("mail"),
				new Date().toString(), EnumUserRank.NOOB, 0, 0);
		
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withUrl("/worker/upload")
				.payload(JSONUtils.toJson(plasticUser))
				.param("downloadLink", downloadLink)
				.param("fileSize",String.valueOf(fileSize)));

		try {
			//			resp.getWriter().write(downloadLink);
		} catch (Exception e) {
			throw new ServletException("Error updating book", e);
		}
	}
}
