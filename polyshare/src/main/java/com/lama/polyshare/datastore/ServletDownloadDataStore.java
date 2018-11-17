package com.lama.polyshare.datastore;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.lama.polyshare.commons.Utils;
import com.lama.polyshare.datastore.model.EnumUserRank;

@SuppressWarnings("serial")
public class ServletDownloadDataStore extends HttpServlet {

	private volatile static Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	private volatile static KeyFactory keyFactory = datastore.newKeyFactory();

	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

		IncompleteKey key = keyFactory.setKind("FileUploaded").newKey();
		String mail = req.getParameter("mail");

		datastore.add(Entity.newBuilder(keyFactory.newKey()).setKey(key).set("mail", mail)
				.set("DownloadRequestStart", Timestamp.now()).build());
	};

	// Requested field are user email as "userMail"
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		handleDownloadLinkRequest(req);
	}

	private void handleDownloadLinkRequest(HttpServletRequest req) {
		IncompleteKey key = keyFactory.setKind("CustomsLinks").newKey();
		String mail = req.getParameter("userMail");

		Query<Entity> uploadQuery = Query.newEntityQueryBuilder().setKind("FileUploaded")
				.setFilter(CompositeFilter.and(PropertyFilter.eq("mail", mail), PropertyFilter.gt("uploadRequestStart",
						Timestamp.of(Utils.addMinutesToDate(-1, Timestamp.now().toDate())))))
				.build();

		QueryResults<Entity> upload = datastore.run(uploadQuery);

		Query<Entity> downloadQuery = Query.newEntityQueryBuilder().setKind("FileDownloaded")
				.setFilter(
						CompositeFilter
								.and(PropertyFilter.eq("mail", mail),
										PropertyFilter.gt("downloadRequestStart",
												Timestamp.of(Utils.addMinutesToDate(-1, Timestamp.now().toDate())))))
				.build();

		QueryResults<Entity> download = datastore.run(downloadQuery);
		int downloadCpt = 0;
		int uploadCpt = 0;
		while (download.hasNext()) {
			download.next();
			downloadCpt++;
		}

		while (upload.hasNext()) {
			upload.next();
			uploadCpt++;
		}

		Query<Entity> query = Query.newEntityQueryBuilder().setKind("user")
				.setFilter(PropertyFilter.eq("mail", req.getParameter("userMail"))).build();

		QueryResults<Entity> user = datastore.run(query);

		EnumUserRank rank = null;
		while (user.hasNext()) {
			Entity ent = user.next();
			rank = EnumUserRank.valueOf(ent.getString("rank"));
		}

		// TODO check rank != null
		if (Utils.isAuthorizedRequest(downloadCpt + uploadCpt, rank)) {
			String id = Timestamp.now().toString();
			datastore.add(Entity.newBuilder(keyFactory.newKey()).setKey(key).set("mail", mail).set("id", id)
					.set("DownloadRequestStart", Timestamp.now()).set("fileName", req.getParameter("fileName"))
					.set("rank", rank.toString()).build());
			// TODO mail ok whith id
		} else {
			// TODO mail nop
		}

		//

	}

}
