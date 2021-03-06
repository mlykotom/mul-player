package cz.vutbr.fit.mulplayer.model.persistance;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.LongSparseArray;

import java.util.ArrayList;
import java.util.LinkedHashSet;
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

	private LinkedHashSet<String> mStoreQueueIds = new LinkedHashSet<>();

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
		mStoreQueueIds.clear();

		int foundSongPos = Constants.NO_POSITION;
		cursor.moveToFirst();
		do {
			Song song = Song.from(cursor);
			mQueueSongs.put(song.getId(), song);
			mQueueOrderList.add(song.getId());
			// creating set of IDS for saving it
			mStoreQueueIds.add(String.valueOf(song.getId()));
			// finding ID
			if (findSongId > 0 && findSongId == song.getId()) {
				foundSongPos = mQueueOrderList.size() - 1;
			}
		}
		while (cursor.moveToNext());
		// save queue of song ids
		setQueue(foundSongPos, mStoreQueueIds);
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

	public void setQueue(int queueIndex, Set<String> queueIds) {
		mSharedPreferences.edit()
				.putStringSet(PERSISTENCE_QUEUE_IDS, queueIds)
				.putInt(PERSISTENCE_ACTUAL_ID, queueIndex)
				.apply();
	}

	public Set<String> getSavedQueue() {
		return mSharedPreferences.getStringSet(PERSISTENCE_QUEUE_IDS, null);
	}

	public void setActualSongIndex(int queueIndex) {
		mSharedPreferences.edit()
				.putInt(PERSISTENCE_ACTUAL_ID, queueIndex)
				.apply();
	}

	public int getSavedActualSongIndex() {
		return mSharedPreferences.getInt(PERSISTENCE_ACTUAL_ID, Constants.NO_POSITION);
	}
}
