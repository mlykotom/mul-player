package cz.vutbr.fit.mulplayer.model;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import java.io.IOException;

import butterknife.Bind;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.model.entity.Song;
import cz.vutbr.fit.mulplayer.ui.Visualizer.VisualizerView;

/**
 * Class for handling one song (playing, pausing, seeking, ...).
 * Base on itself's state, handles device logic (wakelocking, noise reduction,...)
 *
 * @author mlyko
 * @since 12.04.2016
 */
public class Playback implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener, AudioManager.OnAudioFocusChangeListener {
	private static final String TAG = Playback.class.getSimpleName();
	protected @PlaybackStateCompat.State int mState = PlaybackStateCompat.STATE_NONE;
	protected MusicService mService;
	protected @Nullable MediaPlayer mMediaPlayer;
	protected IPlaybackCallback mCallback;

	private @Nullable Song mActiveSong;
	private volatile int mCurrentPosition;

	//@Bind(R.id.Visualizer)	VisualizerView mVisualizerView;

	Playback(MusicService service, IPlaybackCallback callback) {
		mService = service;
		mCallback = callback;
	}

	/**
	 * Safely creates media player or reset its state so that any next music can be played;
	 */
	private void onSaveCreate() {
		if (mMediaPlayer == null) {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnErrorListener(this);
			mMediaPlayer.setOnSeekCompleteListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setWakeMode(mService.getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
		} else {
			mMediaPlayer.reset();
		}
	}

	/**
	 * Artificial destructor because media player must be released
	 */
	public void release() {
		if (mMediaPlayer == null) return;
		mMediaPlayer.release();
		mMediaPlayer.reset();
		mMediaPlayer = null;
	}

	/**
	 * Playing specified song. Handles unpausing, playing from the beginning.
	 * Notifies callback about playback status change
	 * States:
	 * - STOPPED -> BUFFERING
	 * - PAUSED -> PLAYING
	 *
	 * @param song which will be played
	 */
	public void play(Song song) {
		boolean isTheSameSong = song.equals(mActiveSong);

		if (mState == PlaybackStateCompat.STATE_PAUSED && mMediaPlayer != null && isTheSameSong) {
			// check if jumped to different location when paused
			if (mMediaPlayer.getCurrentPosition() != mCurrentPosition) {
				mMediaPlayer.seekTo(mCurrentPosition);
			}

			mMediaPlayer.start();
			mState = PlaybackStateCompat.STATE_PLAYING;
		} else {
			mState = PlaybackStateCompat.STATE_STOPPED;
			onSaveCreate();
			assert mMediaPlayer != null; // media player here  won't be null because onSaveCreate() always creates it
			try {
				mMediaPlayer.setDataSource(song.filepath);
				mMediaPlayer.prepareAsync();
				mState = PlaybackStateCompat.STATE_BUFFERING;
				mActiveSong = song;

				//mVisualizerView.link(mMediaPlayer);

			} catch (IOException e) {
				mCallback.onError(e.getMessage());
			}
		}

		mCallback.onPlaybackStatusChanged(mState);
	}

	/**
	 * Pauses actual song, saves current position and notifies callback
	 */
	public void pause() {
		if (mState == PlaybackStateCompat.STATE_PLAYING) {
			if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
				mMediaPlayer.pause();
				mCurrentPosition = getCurrentPosition();
			}
		}

		mState = PlaybackStateCompat.STATE_PAUSED;
		mCallback.onPlaybackStatusChanged(mState);
	}

	/**
	 * @return whether song is playing or not
	 */
	public boolean isPlaying() {
		return mMediaPlayer != null && mMediaPlayer.isPlaying();
	}

	/**
	 * Jumps to specified time. If out of range, selects 0 or max time
	 *
	 * @param seekTime in milliseconds
	 */
	public void seekTo(int seekTime) {
		if (seekTime < 0) seekTime = 0;
//		if(seekTime > mMediaPlayer.getDuration()) seekTime = mMediaPlayer.getDuration(); // TODO check if seektime better than duration
		mCurrentPosition = seekTime;
		if (mMediaPlayer == null) return;
		mMediaPlayer.seekTo(seekTime);
	}

	/**
	 * Returns current position from media player or from inner state
	 *
	 * @return current position in ms
	 */
	public int getCurrentPosition() {
		return mMediaPlayer == null ? mCurrentPosition : mMediaPlayer.getCurrentPosition();
	}

	@Nullable
	public Song getActiveSong() {
		return mActiveSong;
	}

	public void stop() {
		// TODO needed?
		mActiveSong = null;
	}

	public int getState() {
		return mState;
	}

	/**
	 * Called when the media file is ready for playback.
	 *
	 * @param player the MediaPlayer that is ready for playback
	 */
	@Override
	public void onPrepared(MediaPlayer player) {
		player.start();
		mState = PlaybackStateCompat.STATE_PLAYING;
		mCallback.onPlaybackStatusChanged(mState);
	}

	/**
	 * Called to indicate the completion of a seek operation.
	 *
	 * @param player the MediaPlayer that issued the seek operation
	 */
	@Override
	public void onSeekComplete(MediaPlayer player) {
		// TODO onseekcompleted();
	}

	/**
	 * Called when the end of a media source is reached during playback.
	 *
	 * @param player the MediaPlayer that reached the end of the file
	 */
	@Override
	public void onCompletion(MediaPlayer player) {
		mCallback.onPlaybackCompleted();
	}

	@Override
	public boolean onError(MediaPlayer player, int what, int extra) {
		mCallback.onError("MediaPlayer error " + what + " (" + extra + ")");
		return true;
	}

	/**
	 * Called on the listener to notify it the audio focus for this listener has been changed.
	 * The focusChange value indicates whether the focus was gained,
	 * whether the focus was lost, and whether that loss is transient, or whether the new focus
	 * holder will hold it for an unknown amount of time.
	 * When losing focus, listeners can use the focus change information to decide what
	 * behavior to adopt when losing focus. A music player could for instance elect to lower
	 * the volume of its music stream (duck) for transient focus losses, and pause otherwise.
	 *
	 * @param focusChange the type of focus change, one of {@link AudioManager#AUDIOFOCUS_GAIN},
	 *                    {@link AudioManager#AUDIOFOCUS_LOSS}, {@link AudioManager#AUDIOFOCUS_LOSS_TRANSIENT}
	 *                    and {@link AudioManager#AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK}.
	 */
	@Override
	public void onAudioFocusChange(int focusChange) {
		Log.d(TAG, "onAudioFocusChange. focusChange = " + focusChange);

	}

	public interface IPlaybackCallback {
		void onPlaybackStatusChanged(@PlaybackStateCompat.State int state);

		void onPlaybackCompleted();

		void onError(String error); // TODO errorType?
	}
}
