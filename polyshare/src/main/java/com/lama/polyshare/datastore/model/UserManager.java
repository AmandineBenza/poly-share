package com.lama.polyshare.datastore.model;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.EntityQuery.Builder;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.lama.polyshare.datastore.DataStoreWorker;

public final class UserManager {
	
	public volatile static UserManager instance = new UserManager();
	private volatile static Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	private volatile static KeyFactory keyFactory = datastore.newKeyFactory();
	
	private UserManager() {}
	
	public Entity buildUser(String mail, Timestamp lastSending, EnumUserRank rank, int points, int bytesCount) {
		return Entity
				.newBuilder(keyFactory .setKind("user").newKey(mail))
				.set("mail", mail)
				.set("lastSending", lastSending == null ? null : lastSending)
				.set("rank", rank.toString())
				.set("points", capPoints(rank, points))
				.set("bytesCount", bytesCount)
				.build();
	}
	
	public static int capPoints(EnumUserRank rank, int points) {
		switch(rank) {
		case NOOB:
			return Math.min(points, 100);
		case CASUAL:
			return Math.min(points, 200);
		case LEET:
			return Math.max(points, 201);
		default:
			return points;
		}
	}
	
	public static EnumUserRank adaptRank(int points) {
		if(points <= 100)
			return EnumUserRank.NOOB;
		if(points <= 200)
			return EnumUserRank.CASUAL;
		return EnumUserRank.LEET;
	}
	
	public Entity editUser(Entity user, Timestamp lastSending, EnumUserRank rank, int points, int bytesCount) {
		return Entity
				.newBuilder(keyFactory.setKind("user").newKey(user.getKey().getName()))
				.set("mail", user.getKey().getName())
				.set("lastSending", lastSending == null ? null : lastSending)
				.set("rank", adaptRank(points).toString())
				.set("points", points)
				.set("bytesCount", bytesCount)
				.build();
	}
	
	public QueryResults<Entity> getAllUsers() {
		return datastore.run(Query.newEntityQueryBuilder()
				.setKind("user").build());
	}
	
	public QueryResults<Entity> getAll(String kind) {
		return datastore.run(Query.newEntityQueryBuilder()
				.setKind(kind).build());
	}
	
	public Builder getUserQueryBuilder(int nbUsers) {
		return Query.newEntityQueryBuilder().setKind("user").setLimit(nbUsers);
	}
	
	public Entity getUserByMail(String userMail) {
		return datastore.get(DataStoreWorker.getKey("user", userMail));
	}
	
}