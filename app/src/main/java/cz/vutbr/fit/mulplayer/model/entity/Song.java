package cz.vutbr.fit.mulplayer.model.entity;

import android.database.Cursor;
import android.provider.MediaStore;

/**
 * @author mlyko
 * @since 08.04.2016
 */
public class Song extends BaseModel {

	public String artist;
	public String title;
	public String filepath;
	public int duration;
	public long albumId;
	public String album;
	private String mimeType;

	public Song(long id, String artist, String title, int duration, String filepath, long albumId, String album, String mimeType) {
		super(id);

		this.artist = artist;
		this.title = title;
		this.duration = duration;
		this.filepath = filepath;
		this.albumId = albumId;
		this.album = album;
		this.mimeType = mimeType;
	}

	public static Song from(Cursor cursor) {
		long _id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
		String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
		String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
		int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
		String filepath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
		String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
		long albumId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
		String mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.MIME_TYPE));
		return new Song(_id, artist, title, duration, filepath, albumId, album, mimeType);
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o); // TODO only compare ids?
	}

	public String getArtist() {
		return artist;
	}

	public String getTitle() {
		return title;
	}

	public String getMimeType() {
		return mimeType;
	}
}
