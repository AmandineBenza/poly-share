package com.lama.polyshare.datastore;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.lama.polyshare.commons.JSONUtils;
import com.lama.polyshare.commons.Utils;
import com.lama.polyshare.datastore.model.DataStoreMessage;
import com.lama.polyshare.datastore.model.EnumUserRank;
import com.lama.polyshare.datastore.model.UserBuilder;

@SuppressWarnings("serial")
public class ServletDataStore extends HttpServlet {

	private final static Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	public final static KeyFactory keyFactory = datastore.newKeyFactory();
	
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		Query.newEntityQueryBuilder();
	};

	/**
	 * Input is:
	 * 
	 * {"event":"create-user",
	 *   "data": {
	 *   	"mail":"..."
	 *  	}
	 *  }
	 *  
	 * {"event":"edit-user",
	 *   "data": {
	 *   	"mail":"...",
	 *  	}
	 *  }
	 *  
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
			case "edit-user": {
				result = handleUserEdition(root.get("data").toString());
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
		Date userLastSending = null;
		
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
		
		Key key = keyFactory.setKind("user").newKey(userMail);
		
		Query<Entity> query = Query.newEntityQueryBuilder()
				.setKind("user").setFilter(PropertyFilter.eq("mail", userMail)).build();
		
		QueryResults<Entity> users = datastore.run(query);

		if(users != null && users.hasNext()) {
			return JSONUtils.toJson(new DataStoreMessage("User " + userMail + " already exists !"));
		}
		
		if(key == null) 
			throw new IllegalStateException("Error while processing user key.");
		
		Entity entity = new UserBuilder().build(userMail, userLastSending, userRank, userPoints);
		datastore.add(entity);
		return JSONUtils.toJson(entity);
	}
	
	private String handleUserEdition(String userEditionJson) {
		UserEdition userEdition = JSONUtils.fromJson(userEditionJson, UserEdition.class);
		
		Query<Entity> query = Query.newEntityQueryBuilder()
				.setKind("user")
				.setFilter(PropertyFilter.eq("mail", userEdition.getOID().getMail()))
				.build();
		
		QueryResults<Entity> users = datastore.run(query);

		if(users != null && !users.hasNext()) {
			return JSONUtils.toJson(new DataStoreMessage("User " 
					+ userEdition.getOID().getMail() + " does not exist !"));
		}
		
		Entity toEditUser= users.next();
		toEditUser = new UserBuilder().editWith(toEditUser, userEdition);
		datastore.update(toEditUser);
		return JSONUtils.toJson(toEditUser);
	}

}