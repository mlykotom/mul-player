package cz.vutbr.fit.mulplayer.model;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.annotation.IntDef;

import cz.vutbr.fit.mulplayer.model.entity.Song;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class Playback {
	@IntDef({IDLE, PLAYING, PAUSED})
	@interface AudioState {
	}

	public static final int IDLE = 0;
	public static final int PLAYING = 1;
	public static final int PAUSED = 2;

	MusicService mService;
	MediaPlayer mMediaPlayer;
	Song mActiveSong;

	Playback(MusicService service) {
		mService = service;
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//		mMediaPlayer.setOnPreparedListener(this);
//		mMediaPlayer.setOnErrorListener(this);
//		mMediaPlayer.setOnCompletionListener(this);
//		mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
	}


	void play(Song song) {

	}

	boolean isPlaying() {
		return false;
	}

	void seekTo(int position) {

	}

	void pause() {

	}

	void stop() {

	}
}
