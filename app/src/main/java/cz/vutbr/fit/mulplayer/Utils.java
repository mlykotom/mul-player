package cz.vutbr.fit.mulplayer;

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

}
