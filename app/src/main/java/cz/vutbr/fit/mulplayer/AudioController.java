package cz.vutbr.fit.mulplayer;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import cz.vutbr.fit.mulplayer.entities.Song;

/**
 * @author mlyko
 * @since 10.04.2016
 */
public class AudioController {
	private static AudioController ourInstance = new AudioController();

	public static AudioController getInstance() {
		return ourInstance;
	}

	public List<Song> mSongList = new ArrayList<>();


	private AudioController() {
	}
}
