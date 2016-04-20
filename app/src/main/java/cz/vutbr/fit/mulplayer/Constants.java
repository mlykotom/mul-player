package cz.vutbr.fit.mulplayer;

import android.provider.MediaStore;

/**
 * @author mlyko
 * @since 15.04.2016
 */
public class Constants {

	public static final String QUEUE_PERSISTENCE_NAME = "queue_persistence";

	public static final String MUSIC_SELECTOR = MediaStore.Audio.Media.IS_MUSIC + " != 0";

	public static final String DB_ORDER_ASC = " ASC";
	public static final String DB_ORDER_DESC = " DESC";

	public static final String[] SONG_PROJECTOR = {
			MediaStore.Audio.Media._ID,
			MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media.TITLE,
			MediaStore.Audio.Media.DATA,
			MediaStore.Audio.Media.DURATION,
			MediaStore.Audio.Media.ALBUM_ID,
			MediaStore.Audio.Media.ALBUM,
			MediaStore.Audio.Media.TRACK,
	};

	public static final String[] ARTISTS_PROJECTOR = {
			MediaStore.Audio.Artists._ID,
			MediaStore.Audio.Artists.ARTIST,
			MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
			MediaStore.Audio.Artists.NUMBER_OF_TRACKS,
	};

	public static final String[] ALBUMS_PROJECTOR = {
			MediaStore.Audio.Albums._ID,
			MediaStore.Audio.Albums.ALBUM_ART,
			MediaStore.Audio.Albums.ALBUM,
			MediaStore.Audio.Albums.ARTIST,
			MediaStore.Audio.Albums.NUMBER_OF_SONGS
	};
}
