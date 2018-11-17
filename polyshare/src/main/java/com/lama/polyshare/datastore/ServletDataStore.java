package com.lama.polyshare.datastore;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.Key;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.QueryResults;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lama.polyshare.commons.JSONUtils;
import com.lama.polyshare.commons.Utils;
import com.lama.polyshare.datastore.model.DataStoreMessage;
import com.lama.polyshare.datastore.model.EnumUserRank;
import com.lama.polyshare.datastore.model.UserManager;

@SuppressWarnings("serial")
public class ServletDataStore extends HttpServlet {

	public volatile static Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	public volatile static DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
	public volatile static KeyFactory keyFactory = datastore.newKeyFactory();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		// Query.newEntityQueryBuilder();
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
	 *  {"event":"consult-users"}
	 *  
	 *  {"event":"consult-user", "mail":"eventual@consistency.com"}
	 */
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		JsonObject root = JSONUtils.fromJson(req.getReader(), JsonObject.class);
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
				result = handleUserEdition(root);
				break;
			}
			case "consult-users": {
				result = handleUsersConsultation();
				break;
			}
			case "consult-user" : {
				result = handleUserConsultation(root);
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
		String userLastSending = null;
		
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
		
		Entity entity = UserManager.instance.buildUser(userMail, userLastSending, userRank, userPoints, userBytesCount);
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
	
	private String handleUserEdition(JsonObject msgRoot) {
		String userMail = msgRoot.get("mail").getAsString();
		Entity toEditUser = UserManager.instance.getUserByMail(userMail);
		
		if(toEditUser == null) {
			return JSONUtils.toJson(new DataStoreMessage("User " 
					+ userMail + " does not exist !"));
		}
		
		JsonElement userDateElement = msgRoot.get("lastSending");
		JsonElement userPointsElement = msgRoot.get("points");
		JsonElement userBytesCount = msgRoot.get("bytesCount");
		
		toEditUser = UserManager.instance.editUser(toEditUser,
			userDateElement == null ? toEditUser.getString("lastSending") : userDateElement.getAsString(),
			EnumUserRank.valueOf(toEditUser.getString("rank")),
			userPointsElement == null ? (int) toEditUser.getLong("points") : userPointsElement.getAsInt(),
			userBytesCount == null ? (int) toEditUser.getLong("bytesCount") : userBytesCount.getAsInt());
		
		datastore.update(toEditUser);
		return JSONUtils.toJson(datastore.get(toEditUser.getKey()));
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
	
	private String handleUserConsultation(JsonObject msgRoot) {
		String mail = msgRoot.get("mail").getAsString();
		return JSONUtils.toJson(UserManager.instance.getUserByMail(mail));
	}
	
	public static Key getKey(String kind, String id) {
		return keyFactory.setKind(kind).newKey(id);
	}

}