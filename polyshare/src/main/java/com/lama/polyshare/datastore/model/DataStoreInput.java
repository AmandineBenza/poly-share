package com.lama.polyshare.datastore.model;

public class DataStoreInput {

	private String event;
	private Object data;

	public DataStoreInput(String event, Object data) {
		this.event = event;
		this.data = data;
	}
	
	public DataStoreInput() {
		
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}