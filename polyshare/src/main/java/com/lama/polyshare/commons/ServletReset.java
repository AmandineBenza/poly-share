package com.lama.polyshare.commons;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;

public class ServletReset extends HttpServlet {

	private static final long serialVersionUID = -3033318538535959542L;
	private final static Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		List<Key> allKeys = new ArrayList<>();
		Query<Entity> query = Query.newEntityQueryBuilder().setKind("user").build();
		datastore.run(query).forEachRemaining(e -> allKeys.add(e.getKey()));
		allKeys.forEach(k -> datastore.delete(k));
	}
	
}