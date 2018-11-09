package com.lama.polyshare.datastore.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.cloud.datastore.Entity;
import com.lama.polyshare.datastore.ServletDataStore;
import com.lama.polyshare.datastore.UserEdition;

public class User {
	
	private final static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	private long keyID;
	private String mail;
	private Date lastSendDate;
	private EnumUserRank rank;
	private int points;
	
	public User() {
		
	}
	
	public User(long keyID, String mail, Date lastSendDate, EnumUserRank rank, int points) {
		this.keyID = keyID;
		this.mail = mail;
		this.lastSendDate = lastSendDate;
		this.rank = rank;
		this.points = points;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public EnumUserRank getRank() {
		return rank;
	}

	public void setRank(EnumUserRank rank) {
		this.rank = rank;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Date getLastSendDate() {
		return lastSendDate;
	}

	public void setLastSendDate(Date lastSendDate) {
		this.lastSendDate = lastSendDate;
	}
	
	public Entity toEntity() {
		return Entity.newBuilder(ServletDataStore.keyFactory.newKey(keyID))
				.set("key", keyID)
				.set("mail", mail)
				.set("lastSendDate", sdf.format(lastSendDate))
				.set("rank", rank.toString())
				.set("points", points)
				.build();
	}
	
	public static User fromEntity(Entity entity) {
		long keyID = entity.getLong("key");
		String mail = entity.getString("mail");
		EnumUserRank rank = EnumUserRank.valueOf(entity.getString("rank"));
		int value = Integer.parseInt(entity.getString("points"));
		Date lastSendDate = null;
		
		try {
			lastSendDate = sdf.parse(entity.getString("lastSendDate"));
		} catch (ParseException e) {
			System.out.println("Error while parsing entity last send date.");
		}
		
		return new User(keyID, mail, lastSendDate, rank, value);
	}
	
	public User editWith(UserEdition edition) {
		return this;
	}
	
	public long getKeyID() {
		return keyID;
	}

	public void setKeyID(long keyID) {
		this.keyID = keyID;
	}

	// Rank 
	public static enum EnumUserRank {
		NOOB, CASUAL, LEET;
	}
	
}
