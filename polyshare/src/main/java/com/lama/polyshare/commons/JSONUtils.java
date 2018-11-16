package com.lama.polyshare.commons;

import java.io.Reader;

import com.google.gson.GsonBuilder;

public final class JSONUtils {

	private final static GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
	
	private JSONUtils() {}
	
	public static <T> String toJson(T o) {
		return builder.create().toJson(o);
	}
	
	public static <T> T fromJson(String json, Class<T> clazz) {
		return builder.create().fromJson(json, clazz);
	}
	
	public static <T> T fromJson(Reader reader, Class<T> clazz) {
		return builder.create().fromJson(reader, clazz);
	}
}
