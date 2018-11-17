package com.lama.polyshare.download;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.storage.Blob;
import com.google.gson.JsonObject;
import com.lama.polyshare.commons.JSONUtils;
import com.lama.polyshare.commons.Utils;
import com.lama.polyshare.upload.CloudStorageHelper;

@SuppressWarnings("serial")
public class ServletDownload extends HttpServlet  {

	private final static Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		JsonObject root = JSONUtils.fromJson(req.getReader(), JsonObject.class);
		String mail = root.get("mail").getAsString();

		if(Utils.checkRequest(mail)) {
			String linkID = req.getParameter("linkId");

			Query<Entity> uploadQuery = Query.newEntityQueryBuilder().setKind("CustomsLinks")
					.setFilter(PropertyFilter.eq("id", linkID))
					.build();

			QueryResults<Entity> workingLinks = datastore.run(uploadQuery);

			if(workingLinks.hasNext()) {
				Entity metaFileDataToDownload = workingLinks.next();
				String fileName = metaFileDataToDownload.getString("fileName");
				CloudStorageHelper storageHelper =	new CloudStorageHelper();
				Blob b = storageHelper.downloadFile("staging.poly-share.appspot.com", fileName);

				resp.setCharacterEncoding("UTF-8");
				resp.setContentType("application/octet-stream");   
				resp.setHeader("Content-Disposition","attachment; filename=\"" + fileName + "\"");   

				try (OutputStream out = resp.getOutputStream()){
					out.write(b.getContent());
					out.flush();
				}catch(IOException e){

					e.printStackTrace();
				}
				Queue queue = QueueFactory.getDefaultQueue();
				queue.add(TaskOptions.Builder.withUrl("/worker/download").method(
						Method.GET).param("mail", mail));
			}
			else {
				//TODO send mail ou refuser lien invalide lol (avec une mention le lien peut etre expiré)
				// ServletSendMails.instance.sendDownloadMail(mail, "localhost:8080/Download?linkId=" + linkID);
			}
		} else {
			//TODO mail ou resp.senderreur400 invalid operation pour son rang ou sa vie ou idk balec
		}

	}

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
	ServletException {
		CloudStorageHelper storageHelper =	new CloudStorageHelper();
		
		// TODO CHECK
		String fileName = req.getParameter("fileName");
		String mail = req.getParameter("mail");

		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(TaskOptions.Builder.withUrl("/worker/download").method(Method.POST)
				.param("fileName",fileName).param("mail", mail));

		try {
			//			resp.getWriter().write(downloadLink);
		} catch (Exception e) {
			throw new ServletException("Error updating book", e);
		}
	}
}
