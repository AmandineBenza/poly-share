package com.lama.polyshare.upload;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Acl.Role;
import com.google.cloud.storage.Acl.User;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class CloudStorageHelper {

	private static Storage storage = null;

	static {
		storage = StorageOptions.getDefaultInstance().getService();
	}


	/**
	 * Uploads a file to Google Cloud Storage to the bucket specified in the BUCKET_NAME
	 * environment variable, appending a timestamp to end of the uploaded filename.
	 */
	@SuppressWarnings("deprecation")
	public BlobInfo uploadFile(Part filePart, final String bucketName) throws IOException {

		DateTimeFormatter dtf = DateTimeFormat.forPattern("-YYYY-MM-dd-HHmmssSSS");
		DateTime dt = DateTime.now(DateTimeZone.UTC);
		String dtString = dt.toString(dtf);
		final String fileName = filePart.getSubmittedFileName() + dtString;

		// the inputstream is closed by default, so we don't need to close it here
		BlobInfo blobInfo =
				storage.create(
						BlobInfo
						.newBuilder(bucketName, fileName)
						// Modify access list to allow all users with link to read file
						.setAcl(new ArrayList<>(Arrays.asList(Acl.of(User.ofAllUsers(), Role.READER))))
						.build(),
						filePart.getInputStream());
		return blobInfo;
	}


	
	public Blob downloadFile(final String bucketName,final String fileName) throws IOException {
		BlobId blobID = BlobId.of(bucketName, fileName);
		Blob blobInfo =	storage.get(blobID);
		return blobInfo;
	}
	
	public boolean deleteFile(final String bucketName,final String fileName) throws IOException {
		BlobId blobID = BlobId.of(bucketName, fileName);
		boolean blobInfo =	storage.delete(blobID);
		return blobInfo;
	}

	/**
	 * Extracts the file payload from an HttpServletRequest, checks that the file extension
	 * is supported and uploads the file to Google Cloud Storage.
	 */
	public BlobInfo getImageOrTxtUrl(HttpServletRequest req, HttpServletResponse resp,
			final String bucket) throws IOException, ServletException {
		Part filePart = req.getPart("file");
		final String fileName = filePart.getSubmittedFileName();

		// Check extension of file
		if (fileName != null && !fileName.isEmpty() && fileName.contains(".")) {
			final String extension = fileName.substring(fileName.lastIndexOf('.') + 1);
			String[] allowedExt = {"jpg", "jpeg", "png", "gif",".pdf","txt","py","html",".dat"};
			for (String s : allowedExt) {
				if (extension.equals(s)) {
					return this.uploadFile(filePart, bucket);
				}
			}
			throw new ServletException("file must be an image");
		}
		return null;
	}
	
}
