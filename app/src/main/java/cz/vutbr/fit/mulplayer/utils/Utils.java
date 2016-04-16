package cz.vutbr.fit.mulplayer.utils;

import android.annotation.SuppressLint;

import java.util.concurrent.TimeUnit;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class Utils {

	/**
	 * Properly formats time as minutes:seconds from timestamp
	 *
	 * @param time timestamp
	 * @return formatted string
	 */
	@SuppressLint("DefaultLocale")
	public static String formatTime(long time) {
		long minutes = TimeUnit.MILLISECONDS.toMinutes(time);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(minutes);
		return String.format("%02d:%02d", minutes, seconds);
	}


	/**
	 * Returns string of first letters of any word in string
	 *
	 * @param text where fill be searched
	 * @param maxNum maximum number count
	 * @return string of first lettesr
	 */
	public static String getFirstLetters(String text, int maxNum) {
		String firstLetters = "";
		text = text.replaceAll("[.,]", ""); // Replace dots, etc (optional)
		int i = 1;
		for (String s : text.split(" ")) {
			if (i > maxNum) break;    // we have enough
			if (s.length() == 0) continue;
			firstLetters += s.charAt(0);
			i++;
		}
		return firstLetters;
	}
}
