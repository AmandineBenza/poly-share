package com.lama.polyshare.commons;

import java.util.Date;
import java.util.Random;

import com.lama.polyshare.datastore.model.EnumUserRank;

public class Utils {

	private final static Random SEED = new Random();

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

}
