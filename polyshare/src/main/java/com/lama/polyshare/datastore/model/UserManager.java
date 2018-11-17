package com.lama.polyshare.datastore.model;

import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.lama.polyshare.datastore.ServletDataStore;

public final class UserManager {
	
	public final static UserManager instance = new UserManager();
	
	private UserManager() {}
	
	public Entity buildUser(String mail, String lastSending, EnumUserRank rank, int points, int bytesCount) {
		return Entity
				.newBuilder(
						ServletDataStore
						.keyFactory
						.setKind("user").newKey(mail))
				.set("mail", mail)
				.set("lastSending", lastSending == null ? "null" : lastSending)
				.set("rank", rank.toString())
				.set("points", points)
				.set("bytesCount", bytesCount)
				.build();
	}
	
	public Entity editUser(Entity user, String lastSending, EnumUserRank rank, int points, int bytesCount) {
		return Entity
				.newBuilder(
						ServletDataStore
						.keyFactory
						.setKind("user").newKey(user.getKey().getName()))
				.set("mail", user.getKey().getName())
				.set("lastSending", lastSending == null ? "null" : lastSending)
				.set("rank", rank.toString())
				.set("points", points)
				.set("bytesCount", bytesCount)
				.build();
	}
	
	public QueryResults<Entity> getAllUsers() {
		return ServletDataStore.datastore.run(Query.newEntityQueryBuilder()
				.setKind("user").build());
	}
	
	public QueryResults<Entity> getAll(String kind) {
		return ServletDataStore.datastore.run(Query.newEntityQueryBuilder()
				.setKind(kind).build());
	}
	
	public Entity getUserByMail(String userMail) {
		return ServletDataStore.datastore.get(ServletDataStore.getKey("user", userMail));
	}
	
}