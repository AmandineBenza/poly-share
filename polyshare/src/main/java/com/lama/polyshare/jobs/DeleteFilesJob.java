package com.lama.polyshare.jobs;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.StructuredQuery.CompositeFilter;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.lama.polyshare.commons.Utils;
import com.lama.polyshare.datastore.model.EnumUserRank;
import com.lama.polyshare.upload.CloudStorageHelper;

public class DeleteFilesJob extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4274864314000137136L;
	
	private final static Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	public final static KeyFactory keyFactory = datastore.newKeyFactory();
	static final CloudStorageHelper cloudHelper = new CloudStorageHelper();
	
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		
		

		CloudStorageHelper storageHelper =	new CloudStorageHelper();
		Query<Entity> noobFiles = Query.newEntityQueryBuilder().setKind("FileUploaded")
				.setFilter(CompositeFilter.and(PropertyFilter.eq("rank", EnumUserRank.NOOB.toString()), PropertyFilter.lt("uploadRequestStart",
						Timestamp.of(Utils.addMinutesToDate(-5, Timestamp.now().toDate())))))
				.build();

		QueryResults<Entity> noobFilesU = datastore.run(noobFiles);

		Query<Entity> casualFiles = Query.newEntityQueryBuilder().setKind("FileUploaded")
				.setFilter(
						CompositeFilter
								.and(PropertyFilter.eq("rank", EnumUserRank.CASUAL.toString()),
										PropertyFilter.lt("downloadRequestStart",
												Timestamp.of(Utils.addMinutesToDate(-10, Timestamp.now().toDate())))))
				.build();

		QueryResults<Entity> casualFilesU = datastore.run(casualFiles);
		
		
		Query<Entity> leetFiles = Query.newEntityQueryBuilder().setKind("FileUploaded")
				.setFilter(
						CompositeFilter
								.and(PropertyFilter.eq("rank", EnumUserRank.LEET.toString()),
										PropertyFilter.lt("downloadRequestStart",
												Timestamp.of(Utils.addMinutesToDate(-30, Timestamp.now().toDate())))))
				.build();

		QueryResults<Entity> leetFilesU = datastore.run(leetFiles);
		
		while(noobFilesU.hasNext()) {
			Entity file = noobFilesU.next();
			
			
			String fileName = file.getString("fileName");
			
			
			Query<Entity> linksQuery = Query.newEntityQueryBuilder().setKind("CustomsLinks")
					.setFilter(PropertyFilter.eq("fileName", fileName))
					.build();
			
			QueryResults<Entity> links = datastore.run(linksQuery);
			while(links.hasNext()) {
				datastore.delete(links.next().getKey());
			}
			
			//Delete the file registery.
			datastore.delete(file.getKey());
			//Delete the file himself
			cloudHelper.deleteFile("staging.poly-share.appspot.com", fileName);
		}
		
		
		while(casualFilesU.hasNext()) {
			Entity file = noobFilesU.next();
			
			
			String fileName = file.getString("fileName");
			
			
			Query<Entity> linksQuery = Query.newEntityQueryBuilder().setKind("CustomsLinks")
					.setFilter(PropertyFilter.eq("fileName", fileName))
					.build();
			
			QueryResults<Entity> links = datastore.run(linksQuery);
			while(links.hasNext()) {
				datastore.delete(links.next().getKey());
			}
			
			//Delete the file registery.
			datastore.delete(file.getKey());
			//Delete the file himself
			cloudHelper.deleteFile("staging.poly-share.appspot.com", fileName);
		}
		
		while(leetFilesU.hasNext()) {
			Entity file = noobFilesU.next();
			
			
			String fileName = file.getString("fileName");
			
			
			Query<Entity> linksQuery = Query.newEntityQueryBuilder().setKind("CustomsLinks")
					.setFilter(PropertyFilter.eq("fileName", fileName))
					.build();
			
			QueryResults<Entity> links = datastore.run(linksQuery);
			while(links.hasNext()) {
				datastore.delete(links.next().getKey());
			}
			
			//Delete the file registery.
			datastore.delete(file.getKey());
			//Delete the file himself
			cloudHelper.deleteFile("staging.poly-share.appspot.com", fileName);
		}
	}
}
