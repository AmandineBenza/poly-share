package com.lama.polyshare.commons;

import java.util.Date;
import java.util.Random;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.lama.polyshare.datastore.model.EnumUserRank;

public class Utils {

	private final static Random SEED = new Random();
	private static Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	
	/*
	 * Convenience method to add a specified number of minutes to a Date object
	 * From:
	 * http://stackoverflow.com/questions/9043981/how-to-add-minutes-to-my-date
	 * 
	 * @param minutes The number of minutes to add
	 * 
	 * @param beforeTime The time that will have minutes added to it
	 * 
	 * @return A date object with the specified number of minutes added to it
	 */
	static public Date addMinutesToDate(int minutes, Date beforeTime) {
		final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs
		long curTimeInMs = beforeTime.getTime();
		Date afterAddingMins = new Date(curTimeInMs + (minutes * ONE_MINUTE_IN_MILLIS));
		return afterAddingMins;
	}

	public static boolean isAuthorizedRequest(int i, EnumUserRank rank) {
		if (rank.equals(EnumUserRank.NOOB))
			return i < 1;
		if (rank.equals(EnumUserRank.CASUAL))
			return i < 2;
		return i < 4;
	}

	public static int irand(int min, int max) {
		return SEED.nextInt(max - min) + min;
	}
	
	public static boolean checkRequest(String mail) {
		Query<Entity> uploadQuery = Query.newEntityQueryBuilder().setKind("FileUploaded")
				.setFilter(CompositeFilter.and(PropertyFilter.eq("mail", mail), PropertyFilter.gt("UploadRequestStart",
						Timestamp.of(Utils.addMinutesToDate(-1, Timestamp.now().toDate())))))
				.build();

		QueryResults<Entity> upload = datastore.run(uploadQuery);

		Query<Entity> downloadQuery = Query.newEntityQueryBuilder().setKind("FileDownloaded")
				.setFilter(
						CompositeFilter
								.and(PropertyFilter.eq("mail", mail),
										PropertyFilter.gt("DownloadRequestStart",
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
				.setFilter(PropertyFilter.eq("mail", mail)).build();

		QueryResults<Entity> user = datastore.run(query);
		EnumUserRank rank = null;
		
		while (user.hasNext()) {
			Entity ent = user.next();
			rank = EnumUserRank.valueOf(ent.getString("rank"));
		}
		
		return Utils.isAuthorizedRequest(downloadCpt + uploadCpt, rank);
	}
}