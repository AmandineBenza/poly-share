package com.lama.polyshare.jobs;

import javax.servlet.http.HttpServlet;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.KeyFactory;
import com.lama.polyshare.upload.CloudStorageHelper;

public class DeleteLinksJob extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4274864314000137136L;
	
	private final static Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
	public final static KeyFactory keyFactory = datastore.newKeyFactory();
	static final CloudStorageHelper cloudHelper = new CloudStorageHelper();
	
	//TODO get or post.
}
