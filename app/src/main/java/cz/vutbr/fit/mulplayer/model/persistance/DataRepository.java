package cz.vutbr.fit.mulplayer.model.persistance;

import android.util.LongSparseArray;

import java.util.ArrayList;
import java.util.List;

import cz.vutbr.fit.mulplayer.model.entity.Song;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class DataRepository {
	private static DataRepository ourInstance = new DataRepository();
	public static DataRepository getInstance() {
		return ourInstance;
	}

	public LongSparseArray<Song> mQueueSongs = new LongSparseArray<>();
	public List<Long> mQueueOrderList = new ArrayList<>();



}
