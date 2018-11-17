package com.lama.polyshare.datastore;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.Key;
import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lama.polyshare.commons.JSONUtils;
import com.lama.polyshare.commons.Utils;
import com.lama.polyshare.datastore.model.DataStoreMessage;
import com.lama.polyshare.datastore.model.EnumUserRank;
import com.lama.polyshare.datastore.model.UserManager;
import com.lama.polyshare.mails.ServletSendMails;
import com.lama.polyshare.upload.CloudStorageHelper;


@SuppressWarnings("serial")
public class DataStoreWorker extends HttpServlet {

	private final static Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	private final static KeyFactory keyFactory = datastore.newKeyFactory();

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		String event = req.getParameter("event");
		String result = "Empty.";

		switch(event){
		case "consults": {
			result = handleUsersConsultation();
			break;
		}
		case "consult" : {
			result = handleUserConsultation(req.getParameter("mail"));
			break;
		}
		}

		resp.getWriter().println(result);
	};

	/**
	 * Input is:
	 * 
	 * {"event":"create-user",
	 * "mail":...}
	 * 
	 * 	{"event":"create-user",
	 * "data":{..}}
	 * 
	 * {"event":"edit-user",
	 * "points":...}
	 *  
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		JsonObject root;
		if(req.getParameter("data") != null) {
			root = JSONUtils.fromJson(req.getParameter("data"), JsonObject.class);
		}
		else {
			root = JSONUtils.fromJson(req.getReader(), JsonObject.class);
		}
		System.out.println(root);
		String event = root.get("event").getAsString();
		String result = "Empty.";

		switch(event){
		case "create-user": {
			result = handleUserCreation(root);
			break;
		}
		case "create-users": {
			result = handleUserCreations(root);
			break;
		}
		case "edit-user": {
			result = handleUserEdition(root, 
					req.getParameter("fileSize"),
					req.getParameter("downloadLink"), 
					req.getParameter("fileName"));
			break;
		}
		}

		resp.getWriter().println(result);
	}

	private String handleUserCreation(JsonObject msgRoot) {
		String userMail = msgRoot.get("mail").getAsString();
		JsonElement userRankElement = msgRoot.get("rank");
		EnumUserRank userRank = null;
		int userPoints = 0;
		int userBytesCount = 0;
		com.google.cloud.Timestamp userLastSending = Timestamp.MIN_VALUE;

		if(userRankElement == null) {
			userRank = EnumUserRank.NOOB;
			userPoints = Utils.irand(0, 100 - 20);
		} else {
			userRank = EnumUserRank.valueOf(userRankElement.getAsString().trim().toUpperCase());
			switch (userRank) {
			case NOOB:
				userPoints = Utils.irand(0, 100 - 20);
				break;
			case CASUAL:
				userPoints = Utils.irand(100, 200 - 20);
				break;
			case LEET:
				userPoints = Utils.irand(201, 3000);
				break;
			default:
				throw new IllegalStateException("Type of user unknown.");
			}
		}

		Entity toEditUser = UserManager.instance.getUserByMail(userMail);

		if(toEditUser != null) {
			return JSONUtils.toJson(new DataStoreMessage("User " + userMail + " already exists !"));
		}

		Entity entity = UserManager.instance.buildUser(userMail,
				userLastSending, userRank, userPoints, userBytesCount);
		datastore.add(entity);
		return JSONUtils.toJson(datastore.get(entity.getKey()));
	}

	private String handleUserCreations(JsonObject msgRoot) {

		JsonArray usersJson = msgRoot.get("data").getAsJsonArray();
		StringBuffer buffer = new StringBuffer();
		int cpt = 1;

		for(JsonElement userJson : usersJson) {
			buffer.append("User " + cpt++ + " creation:\n");
			buffer.append(handleUserCreation(userJson.getAsJsonObject()));
			buffer.append("\n");
		}

		return buffer.toString();
	}

	private String handleUserEdition(JsonObject msgRoot, String fileSize, String downloadLink,String fileName ) throws IOException {
		String userMail = msgRoot.get("mail").getAsString();
		Entity toEditUser = UserManager.instance.getUserByMail(userMail);

		System.out.println(toEditUser);
		System.out.println(userMail);


		if(toEditUser == null) {
			return JSONUtils.toJson(new DataStoreMessage("User " 
					+ userMail + " does not exist !"));
		}

		JsonElement userDateElement = msgRoot.get("lastSending");
		JsonElement userPointsElement = msgRoot.get("points");
		JsonElement userBytesCount = msgRoot.get("bytesCount");
		System.out.println(		);
		int newNumberOfBytes =  (int) toEditUser.getLong("bytesCount") + Integer.parseInt(fileSize.trim());

		toEditUser = UserManager.instance.editUser(toEditUser,

				(userDateElement == null ? 
						Timestamp.now() : 
							Timestamp.parseTimestamp(userDateElement.getAsString())),
				null,
				userPointsElement == null ?
						(int) toEditUser.getLong("points") : 
							newNumberOfBytes/1000000,
							userBytesCount == null ? 
									(int) newNumberOfBytes :
										userBytesCount.getAsInt());

		registerUpload(toEditUser.getString("mail"),
				EnumUserRank.valueOf(toEditUser.getString("rank")), fileName, downloadLink);

		datastore.update(toEditUser);
		return JSONUtils.toJson(datastore.get(toEditUser.getKey()));
	}


	private void registerUpload(String mail, EnumUserRank rank, String fileName, String downloadLink) throws IOException {
		IncompleteKey key = keyFactory.setKind("FileUploaded").newKey();
		int downloadCpt = 0;
		int uploadCpt = 0;

//		try {
			Query<Entity> uploadQuery = Query.newEntityQueryBuilder()
					.setKind("FileUploaded")
					.setFilter(CompositeFilter.and(PropertyFilter.eq("mail", mail),
							PropertyFilter.gt("UploadRequestStart",
							Timestamp.of(Utils.addMinutesToDate(-1, Timestamp.now().toDate())))))
					.build();

			QueryResults<Entity> upload = datastore.run(uploadQuery);
			
			while (upload.hasNext()) {
				upload.next();
				uploadCpt++;
			}
//		}catch(Exception e){
//		}
		
		try {
			Query<Entity> downloadQuery = Query.newEntityQueryBuilder().setKind("FileDownloaded")
					.setFilter(
							CompositeFilter
							.and(PropertyFilter.eq("mail", mail),
									PropertyFilter.gt("downloadRequestStart",
											Timestamp.of(Utils.addMinutesToDate(-1, Timestamp.now().toDate())))))
					.build();

			QueryResults<Entity> download = datastore.run(downloadQuery);
			while (download.hasNext()) {
				download.next();
				downloadCpt++;
			}
		}
		catch(Exception e){
		}



		if (Utils.isAuthorizedRequest(downloadCpt + uploadCpt, rank)) {
			String id = Timestamp.now().toString();

			datastore.add(Entity.newBuilder(DataStoreWorker.keyFactory.newKey()).setKey(key)
					.set("mail", mail)
					.set("id", id).set("UploadRequestStart", Timestamp.now())
					.set("fileName", fileName).set("rank", rank.toString()).build());

			// send mail ok
			ServletSendMails.instance.sendUploadMail(mail, downloadLink, fileName);
		} else {
			new CloudStorageHelper().deleteFile("staging.poly-share.appspot.com", fileName);
			// send mail no lol
			ServletSendMails.instance.sendNoob(mail);
		}
	}

	private String handleUsersConsultation() {
		StringBuffer buffer = new StringBuffer();
		QueryResults<Entity> users = UserManager.instance.getAllUsers();
		int cpt = 1;

		while(users.hasNext()) {
			buffer.append(">> User " + cpt++ + ":\n");
			buffer.append(JSONUtils.toJson(users.next()));
			buffer.append("\n");
		}

		return buffer.toString();
	}

	private String handleUserConsultation(String mail) {
		if(mail == null || mail.isEmpty())
			return "Input mail badly formed.";

		return JSONUtils.toJson(UserManager.instance.getUserByMail(mail));
	}

	public static Key getKey(String kind, String id) {
		return keyFactory.setKind(kind).newKey(id);
	}

}