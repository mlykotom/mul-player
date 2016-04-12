package cz.vutbr.fit.mulplayer.model;

import android.util.SparseArray;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import cz.vutbr.fit.mulplayer.entity.Song;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class DataRepository {
	private static DataRepository ourInstance = new DataRepository();

	public List<Song> mSongList = new ArrayList<>();

	public SparseArray<Song> mSongsIdList = new SparseArray<>();

	public static DataRepository getInstance() {
		return ourInstance;
	}

	private DataRepository() {
	}
}
