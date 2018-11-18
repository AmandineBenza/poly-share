package com.lama.polyshare.datastore.model;


/**
 * Utilitaire faciliter la transformation JSon 
 */
public class DataStoreMessage {

	private String[] infos;
	
	public DataStoreMessage(String... infos) {
		this.infos = infos;
	}

	public String[] getInfos() {
		return infos;
	}

	public void setInfos(String[] infos) {
		this.infos = infos;
	}

}