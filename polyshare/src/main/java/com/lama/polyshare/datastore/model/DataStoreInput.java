package com.lama.polyshare.datastore.model;

public class DataStoreInput {

	private String event;
	private Object content;

	public DataStoreInput(String event, Object content) {
		this.event = event;
		this.content = content;
	}
	
	public DataStoreInput() {
		
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

}
