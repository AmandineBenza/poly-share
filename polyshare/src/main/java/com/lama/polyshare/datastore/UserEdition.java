package com.lama.polyshare.datastore;

import java.util.Date;

import com.lama.polyshare.datastore.model.User.EnumUserRank;

public class UserEdition {

	private UserIdentifier oid;
	private Date lastSendDate;
	private EnumUserRank rank;
	private int points;
	
	public UserEdition(UserIdentifier oid, long keyID, Date lastSendDate, EnumUserRank rank, int points) {
		this.oid = oid;
		this.lastSendDate = lastSendDate;
		this.rank = rank;
		this.points = points;
	}
	
	public UserEdition() {
		
	}

	public Date getLastSendDate() {
		return lastSendDate;
	}

	public void setLastSendDate(Date lastSendDate) {
		this.lastSendDate = lastSendDate;
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
	
	public UserIdentifier getOID() {
		return oid;
	}
	
	public void setOID(UserIdentifier oid) {
		this.oid = oid;
	}
	
	public static class UserIdentifier {
		
		private long keyID;
		private String mail;
		
		public UserIdentifier() {
			
		}
		
		public UserIdentifier(long keyID, String mail) {
			this.keyID = keyID;
			this.mail = mail;
		}
		
		public long getKeyID() {
			return keyID;
		}
		
		public void setKeyID(long keyID) {
			this.keyID = keyID;
		}
		
		public String getMail() {
			return mail;
		}
		
		public void setMail(String mail) {
			this.mail = mail;
		}
	}
	
}
