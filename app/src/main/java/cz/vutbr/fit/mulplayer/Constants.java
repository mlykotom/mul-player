package cz.vutbr.fit.mulplayer;

import android.provider.MediaStore;

/**
 * @author mlyko
 * @since 15.04.2016
 */
public class Constants {
	public static final String MUSIC_SELECTOR = MediaStore.Audio.Media.IS_MUSIC + " != 0";

	public static final String[] SONG_PROJECTOR = {
			MediaStore.Audio.Media._ID,
			MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media.TITLE,
			MediaStore.Audio.Media.DATA,
			MediaStore.Audio.Media.DURATION,
			MediaStore.Audio.Media.ALBUM_ID,
			MediaStore.Audio.Media.ALBUM,
			MediaStore.Audio.Media.TRACK,
			// -- other (sorting,etc)
//			MediaStore.Audio.Media.ALBUM_KEY,
//			MediaStore.Audio.Media.ARTIST_KEY,
//			MediaStore.Audio.Media.TITLE_KEY,
	};
}
