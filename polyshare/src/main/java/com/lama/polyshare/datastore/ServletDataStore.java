package com.lama.polyshare.datastore;

import java.io.IOException;

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
import com.google.gson.JsonObject;
import com.lama.polyshare.commons.JSONUtils;
import com.lama.polyshare.datastore.model.DataStoreMessage;
import com.lama.polyshare.datastore.model.User;
import com.lama.polyshare.datastore.model.User.EnumUserRank;

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
		System.out.println(root);
		String event = root.get("event").getAsString();
		String result = "Empty.";
		
		switch(event){
			case "create-user": {
				result = handleUserCreation(root.get("data").toString());
				break;
			}
			case "edit-user": {
				result = handleUserEdition(root.get("data").toString());
				break;
			}
		}
		
		resp.getWriter().println(result);
	}
	
	private String handleUserCreation(String userJson) {
		User newUser = JSONUtils.fromJson(userJson, User.class);
		Key key = keyFactory.setKind("user").newKey(newUser.getMail());
		
		Query<Entity> query = Query.newEntityQueryBuilder()
				.setKind("user").setFilter(PropertyFilter.eq("mail", newUser.getMail())).build();
		
		QueryResults<Entity> users = datastore.run(query);

		if(users != null && users.hasNext()) {
			return JSONUtils.toJson(new DataStoreMessage("User " + newUser.getMail() + " already exists !"));
		}
		
		newUser.setKeyID(key.getId());
		newUser.setRank(EnumUserRank.NOOB);
		newUser.setPoints(0);
		newUser.setLastSendDate(null);
		
		Entity entity = newUser.toEntity(); 
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
		
		Entity toEditEntity = users.next();
		User toEditUser  = User.fromEntity(toEditEntity);
		toEditUser.editWith(userEdition);
		datastore.update(toEditUser.toEntity());
		return JSONUtils.toJson(toEditUser);
	}

}
