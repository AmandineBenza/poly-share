package com.lama.polyshare.datastore.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.cloud.datastore.Entity;
import com.lama.polyshare.datastore.ServletDataStore;
import com.lama.polyshare.datastore.UserEdition;

public final class UserBuilder {
	
	private final static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	private Long keyID;
	private String mail;
	private Date lastSending;
	private EnumUserRank rank;
	private int points;
	
	public UserBuilder() {
	}
	
	public Entity build() {
		return Entity
				.newBuilder(ServletDataStore.keyFactory.newKey(keyID.longValue()))
				.set("key", keyID)
				.set("mail", mail)
				.set("lastSending", sdf.format(lastSending))
				.set("rank", rank.toString())
				.set("points", points)
				.build();
	}
	
	public Entity build(String mail, Date lastSending, EnumUserRank rank, int points) {
		return Entity
				.newBuilder(
						ServletDataStore
						.keyFactory
						.newKey(mail))
				.set("mail", mail)
				.set("lastSending", lastSending != null ? sdf.format(lastSending) : "noLastSendingDate")
				.set("rank", rank.toString())
				.set("points", points)
				.build();
	}
	
	public Long getKeyID() {
		return keyID;
	}

	public void setKeyID(Long keyID) {
		this.keyID = keyID;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Date getLastSending() {
		return lastSending;
	}

	public void setLastSending(Date lastSending) {
		this.lastSending = lastSending;
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

	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	@Deprecated // TODO
	public Entity editWith(Entity user, UserEdition edition) {
//		if(edition.getSending() != null) {
//			Sending = edition.getSending();
//		}
//		
//		if(edition.getPoints() != -1) {
//			points = edition.getPoints();
//		}
//		
//		if(edition.getRank() != EnumUserRank.NONE) {
//			rank = edition.getRank();
//		}
		
		return user;
	}
	
}