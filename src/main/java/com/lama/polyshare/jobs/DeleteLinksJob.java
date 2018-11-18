package com.lama.polyshare.jobs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.lama.polyshare.commons.Utils;


/***
 *	Supprimes les liens apres 5 minutes.
 */
public class DeleteLinksJob extends HttpServlet {
	
	private static final long serialVersionUID = -4274864314000137136L;
	private final static Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		Query<Entity> oldLinksQuery = Query.newEntityQueryBuilder().setKind("CustomsLinks")
				.setFilter(PropertyFilter.lt("DownloadRequestStart",
						Timestamp.of(Utils.addMinutesToDate(-5, Timestamp.now().toDate()))))
				.build();
		
		QueryResults<Entity> oldLinksResults = datastore.run(oldLinksQuery);
		oldLinksResults.forEachRemaining(link -> datastore.delete(link.getKey()));
	}
}