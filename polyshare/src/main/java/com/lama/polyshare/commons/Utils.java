package com.lama.polyshare.commons;

import java.util.Random;

public final class Utils {
	
	private static final Random SEED = new Random();
	
	private Utils() {}
	
	public static int irand(int min, int max) {
		return SEED.nextInt(max - min) + min;
	}

}
