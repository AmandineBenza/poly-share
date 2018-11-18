package com.lama.polyshare.commons;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.Query;


/***
 * Servlet pour vider la base de donnée. 
 */
public class ServletReset extends HttpServlet {

	private static final long serialVersionUID = -3033318538535959542L;
	private final static Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

	// https://console.cloud.google.com/datastore/entities;kind=FileUploaded;ns=__$DEFAULT$__/query/kind?project=poly-share
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		// Reset users
		List<Key> allKeys = new ArrayList<>();
		Query<Entity> query = Query.newEntityQueryBuilder().setKind("user").build();
		datastore.run(query).forEachRemaining(e -> allKeys.add(e.getKey()));
		allKeys.forEach(k -> datastore.delete(k));
		resp.getWriter().println("Removed " + allKeys.size() + " users.");
		allKeys.clear();
		
		// Reset files downloaded
		query = Query.newEntityQueryBuilder().setKind("FileDownloaded").build();
		datastore.run(query).forEachRemaining(e -> allKeys.add(e.getKey()));
		allKeys.forEach(k -> datastore.delete(k));
		resp.getWriter().println("Removed " + allKeys.size() + " downloaded files.");
		allKeys.clear();
		
		// Reset files uploaded
		query = Query.newEntityQueryBuilder().setKind("FileUploaded").build();
		datastore.run(query).forEachRemaining(e -> allKeys.add(e.getKey()));
		allKeys.forEach(k -> datastore.delete(k));
		resp.getWriter().println("Removed " + allKeys.size() + " uploaded files.");
		allKeys.clear();
		
		// Reset customs links
		query = Query.newEntityQueryBuilder().setKind("CustomsLinks").build();
		datastore.run(query).forEachRemaining(e -> allKeys.add(e.getKey()));
		allKeys.forEach(k -> datastore.delete(k));
		resp.getWriter().println("Removed " + allKeys.size() + " customs links.");
	}
	
}