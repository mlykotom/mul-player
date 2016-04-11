package cz.vutbr.fit.mulplayer.model;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.vutbr.fit.mulplayer.entity.Song;
import cz.vutbr.fit.mulplayer.event.PlaybackEvent;
import cz.vutbr.fit.mulplayer.event.SongEvent;

/**
 * @author mlyko
 * @since 10.04.2016
 */
public class AudioService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, Loader.OnLoadCompleteListener<Cursor> {

	private static final int LOADER_AUDIO_MUSIC = 0;

	@StringDef({INIT, PLAY_PAUSE, STOP, NEXT, PREVIOUS, SEEK_TO})
	@interface AudioAction {
	}

	public static final String INIT = "cz.vutbr.fit.mulplayer.action.INIT";
	public static final String PLAY_PAUSE = "cz.vutbr.fit.mulplayer.action.PLAY_PAUSE";
	public static final String STOP = "cz.vutbr.fit.mulplayer.action.STOP";
	public static final String NEXT = "cz.vutbr.fit.mulplayer.action.NEXT";
	public static final String PREVIOUS = "cz.vutbr.fit.mulplayer.action.PREVIOUS";

	public static final String SEEK_TO = "cz.vutbr.fit.mulplayer.action.SEEK_TO";

	public static final String ACTION_VALUE = "cz.vutbr.fit.mulplayer.action_value";

	@IntDef({PLAYING, PAUSED, STOPPED})
	@interface AudioState {
	}

	public static final int STOPPED = 0;
	public static final int PLAYING = 1;
	public static final int PAUSED = 2;

	public @AudioService.AudioState int mPlayerState = STOPPED;

	public CursorLoader mCursorLoader;

	//Some audio may be explicitly marked as not being music
	String mAudioSelector = MediaStore.Audio.Media.IS_MUSIC + " != 0";

	EventBus mEventBus = EventBus.getDefault();

	String[] mAudioProjector = {
			MediaStore.Audio.Media._ID,
			MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media.TITLE,
			MediaStore.Audio.Media.DATA,
			MediaStore.Audio.Media.DISPLAY_NAME,
			MediaStore.Audio.Media.DURATION
	};

	// TODO INIT?

	// TODO implement as foreground service !!!
//	String songName;
//	// assign the song name to songName
//	PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0,
//			new Intent(getApplicationContext(), MainActivity.class),
//			PendingIntent.FLAG_UPDATE_CURRENT);
//	Notification notification = new Notification();
//	notification.tickerText = text;
//	notification.icon = R.drawable.play0;
//	notification.flags |= Notification.FLAG_ONGOING_EVENT;
//	notification.setLatestEventInfo(getApplicationContext(), "MusicPlayerSample",
//			"Playing: " + songName, pi);
//	startForeground(NOTIFICATION_ID, notification);

	MediaPlayer mMediaPlayer = null;

	List<Song> mPlayQueue = new ArrayList<>();

	public List<Song> mSongList = new ArrayList<>();

	/**
	 * Does setAction on service
	 *
	 * @param context application's context
	 * @param action  {@link AudioAction}
	 */
	public static void setAction(Context context, @AudioAction String action) {
		Intent intent = new Intent(context, AudioService.class);
		intent.setAction(action);
		context.startService(intent);
	}

	public static void setAction(Context context, @AudioAction String action, int value) {
		Intent intent = new Intent(context, AudioService.class);
		intent.setAction(action);
		intent.putExtra(ACTION_VALUE, value);
		context.startService(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mCursorLoader = new CursorLoader(
				getApplicationContext(),
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				mAudioProjector,
				mAudioSelector,
				null,
				null
		);

		mCursorLoader.registerListener(LOADER_AUDIO_MUSIC, this);
		mCursorLoader.startLoading();

		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnPreparedListener(this);
		mMediaPlayer.setOnErrorListener(this);
		mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
	}

	/**
	 * Called on the thread that created the Loader when the load is complete.
	 *
	 * @param loader the loader that completed the load
	 * @param cursor the result of the load
	 */
	@Override
	public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
		while (cursor.moveToNext()) {
			String artist = cursor.getString(1);
			String title = cursor.getString(2);
			String data = cursor.getString(3);
			int duration = cursor.getInt(5);
			Song song = new Song(artist, title, duration, data);
			mSongList.add(song);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMediaPlayer.release();

		// Stop the CURSOR loader
		if (mCursorLoader != null) {
			mCursorLoader.unregisterListener(this);
			mCursorLoader.cancelLoad();
			mCursorLoader.stopLoading();
		}
	}

	public void nextSong(Song song) {

	}

	public void playSong(Song song) throws IOException {
		mPlayerState = PLAYING;
		mMediaPlayer.setDataSource(song.data);
		mMediaPlayer.prepareAsync();
		mEventBus.post(new SongEvent(song, true));
		mHandler.postDelayed(UpdateSongTime, 100);
	}

	private Handler mHandler = new Handler();

	private Runnable UpdateSongTime = new Runnable() {
		@SuppressLint("DefaultLocale")
		public void run() {
			int actualTime = mMediaPlayer.getCurrentPosition();
			mEventBus.post(new PlaybackEvent(actualTime));
			mHandler.postDelayed(this, 100);
		}
	};

	public void pauseSong(Song song) {
		mPlayerState = PAUSED;
		mMediaPlayer.stop();
		mHandler.removeCallbacks(UpdateSongTime);
		mEventBus.post(new SongEvent(song, false));
	}

	private int test = 0;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (intent == null) {
			return START_STICKY;
			// TODO
		}

		try {
			switch (intent.getAction()) {
				case PLAY_PAUSE:
					switch (mPlayerState) {
//						case PAUSED:
						case STOPPED:
							playSong(mSongList.get(test));
							break;

						case PLAYING:
							pauseSong(mSongList.get(test));
							break;
					}
					break;

				case NEXT:
					test++;
					mMediaPlayer.stop();
					playSong(mSongList.get(test));
					break;

				case SEEK_TO:
					int seekTime = intent.getIntExtra(ACTION_VALUE, mMediaPlayer.getCurrentPosition());
					mMediaPlayer.seekTo(seekTime);
					break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}


		return START_STICKY; // TODO should start after killed?
	}


	/**
	 * Called when the media file is ready for playback.
	 *
	 * @param mp the MediaPlayer that is ready for playback
	 */
	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
	}

	/**
	 * Called to indicate an error.
	 *
	 * @param mp    the MediaPlayer the error pertains to
	 * @param what  the type of error that has occurred:
	 *              <ul>
	 *              <li>{@link MediaPlayer#MEDIA_ERROR_UNKNOWN}
	 *              <li>{@link MediaPlayer#MEDIA_ERROR_SERVER_DIED}
	 *              </ul>
	 * @param extra an extra code, specific to the error. Typically
	 *              implementation dependent.
	 *              <ul>
	 *              <li>{@link MediaPlayer#MEDIA_ERROR_IO}
	 *              <li>{@link MediaPlayer#MEDIA_ERROR_MALFORMED}
	 *              <li>{@link MediaPlayer#MEDIA_ERROR_UNSUPPORTED}
	 *              <li>{@link MediaPlayer#MEDIA_ERROR_TIMED_OUT}
	 *              <li><code>MEDIA_ERROR_SYSTEM (-2147483648)</code> - low-level system error.
	 *              </ul>
	 * @return True if the method handled the error, false if it didn't.
	 * Returning false, or not having an OnErrorListener at all, will
	 * cause the OnCompletionListener to be called.
	 */
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		return false;
	}


	/**
	 * Note: ignore it
	 * Useless cause service is not bound to any activity
	 *
	 * @param intent startup intent
	 * @return binder
	 */
	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
