package cz.vutbr.fit.mulplayer.model.persistance;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.LongSparseArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import cz.vutbr.fit.mulplayer.Constants;
import cz.vutbr.fit.mulplayer.application.App;
import cz.vutbr.fit.mulplayer.model.entity.Song;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class DataRepository {
	private static final String PERSISTENCE_QUEUE_IDS = "persistence_queue_ids";
	private static final String PERSISTENCE_ACTUAL_ID = "persistence_actual_id";

	private static DataRepository ourInstance = new DataRepository();

	public static DataRepository getInstance() {
		return ourInstance;
	}

	public LongSparseArray<Song> mQueueSongs = new LongSparseArray<>();
	public List<Long> mQueueOrderList = new ArrayList<>();

	private SharedPreferences mSharedPreferences = App.getContext().getSharedPreferences(Constants.QUEUE_PERSISTENCE_NAME, Context.MODE_PRIVATE);

	/**
	 * TODO somehow compare if the old list is not the same as new one, than skip populating
	 * Queue specified songs from cursor and finds position of song id.
	 *
	 * @param cursor which will be queued
	 */
	public int queueSongsAndFindPosition(Cursor cursor, long findSongId) {
		mQueueSongs.clear();
		mQueueOrderList.clear();
		int foundSongPos = Constants.NO_POSITION;
		cursor.moveToFirst();
		do {
			Song song = Song.from(cursor);
			mQueueSongs.put(song.getId(), song);

			if (findSongId > 0 && findSongId == song.getId()) {
				foundSongPos = mQueueOrderList.size();
			}

			mQueueOrderList.add(song.getId());
		}
		while (cursor.moveToNext());

		return foundSongPos;
	}

	/**
	 * Queue specified songs from cursor
	 *
	 * @param cursor
	 */
	public void queueSongs(Cursor cursor) {
		queueSongsAndFindPosition(cursor, 0);
	}

	public void rebuildQueue() {
		// TODO complete!
//		long actualSongId = mSharedPreferences.getLong(PERSISTENCE_ACTUAL_ID, 0);
//		Set<String> queuedIds = mSharedPreferences.getStringSet(PERSISTENCE_QUEUE_IDS, null);
//		if (queuedIds == null) return;			// TODO should we do something?
//
//		CursorLoader cursorLoader = new CursorLoader(App.getContext());
//		cursorLoader.setUri(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
//		cursorLoader.setProjection(Constants.SONG_PROJECTOR);
////		cursorLoader.registerListener(1, this);
//		cursorLoader.setSelection();
//		cursorLoader.startLoading();
//		for (String idParsed : queuedIds) {
//
//		}
	}

	public void setQueue(long queueId, Set<String> queueIds) {
		mSharedPreferences.edit()
				.putStringSet(PERSISTENCE_QUEUE_IDS, queueIds)
				.putLong(PERSISTENCE_ACTUAL_ID, queueId)
				.apply();
	}

	public void saveActualSongId(long queueId) {
		mSharedPreferences.edit()
				.putLong(PERSISTENCE_ACTUAL_ID, queueId)
				.apply();
	}

}
