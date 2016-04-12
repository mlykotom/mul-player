package cz.vutbr.fit.mulplayer.model;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.media.AudioManager;
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

import cz.vutbr.fit.mulplayer.model.entity.Song;
import cz.vutbr.fit.mulplayer.model.event.PlaybackEvent;
import cz.vutbr.fit.mulplayer.model.event.SongEvent;

/**
 * @author mlyko
 * @since 10.04.2016
 */
public class AudioService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, Loader.OnLoadCompleteListener<Cursor>, MediaPlayer.OnCompletionListener {

	private static final int LOADER_AUDIO_MUSIC = 0;

	@StringDef({INIT, PLAY_PAUSE, NEXT, PREVIOUS, SEEK_TO, SHUFFLE_TOGGLE, REPEAT_TOGGLE})
	@interface AudioAction {
	}

	private static final int UI_REFRESH_INTERVAL = 500;

	// ----- player actions ----- //
	public static final String INIT = "cz.vutbr.fit.mulplayer.action.INIT";
	public static final String PLAY_PAUSE = "cz.vutbr.fit.mulplayer.action.PLAY_PAUSE";
	public static final String NEXT = "cz.vutbr.fit.mulplayer.action.NEXT";
	public static final String PREVIOUS = "cz.vutbr.fit.mulplayer.action.PREVIOUS";
	public static final String SEEK_TO = "cz.vutbr.fit.mulplayer.action.SEEK_TO";
	public static final String SHUFFLE_TOGGLE = "cz.vutbr.fit.mulplayer.action.SHUFFLE_TOGGLE";
	public static final String REPEAT_TOGGLE = "cz.vutbr.fit.mulplayer.action.REPEAT_TOGGLE";

	public static final String ACTION_VALUE = "cz.vutbr.fit.mulplayer.action_value";

	// ----- player states ----- //
	@IntDef({IDLE, PLAYING, PAUSED})
	@interface AudioState {
	}

	public static final int IDLE = 0;
	public static final int PLAYING = 1;
	public static final int PAUSED = 2;

	//Some audio may be explicitly marked as not being music
	String mAudioSelector = MediaStore.Audio.Media.IS_MUSIC + " != 0";

	String[] mAudioProjector = {
			MediaStore.Audio.Media._ID,
			MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media.TITLE,
			MediaStore.Audio.Media.DATA,
			MediaStore.Audio.Media.DURATION,
			MediaStore.Audio.Media.ALBUM_ID,
			MediaStore.Audio.Media.ALBUM,
			MediaStore.Audio.Media.TRACK,
			// -- other (sorting,etc)
			MediaStore.Audio.Media.ALBUM_KEY,
			MediaStore.Audio.Media.ARTIST_KEY,
			MediaStore.Audio.Media.TITLE_KEY,
	};

	public @AudioState int mPlayerState = IDLE;
	public CursorLoader mAudioCursorLoader;

	EventBus mEventBus = EventBus.getDefault();
	MediaPlayer mMediaPlayer = null;
	public List<Song> mSongList = new ArrayList<>();
	private Handler mHandler = new Handler();

	/**
	 * Fires specified action on player's service
	 *
	 * @param context application's context
	 * @param action  {@link AudioAction}
	 */
	public static void fireAction(Context context, @AudioAction String action) {
		Intent intent = new Intent(context, AudioService.class);
		intent.setAction(action);
		context.startService(intent);
	}

	public static void fireAction(Context context, @AudioAction String action, int value) {
		Intent intent = new Intent(context, AudioService.class);
		intent.setAction(action);
		intent.putExtra(ACTION_VALUE, value);
		context.startService(intent);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mAudioCursorLoader = new CursorLoader(
				getApplicationContext(),
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				mAudioProjector,
				mAudioSelector,
				null,
				null
		);

		mAudioCursorLoader.registerListener(LOADER_AUDIO_MUSIC, this);
		mAudioCursorLoader.startLoading();


		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		mMediaPlayer.setOnPreparedListener(this);
		mMediaPlayer.setOnErrorListener(this);
		mMediaPlayer.setOnCompletionListener(this);
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
		DataRepository rep = DataRepository.getInstance();
		while (cursor.moveToNext()) {
			long id = cursor.getLong(0);
			String artist = cursor.getString(1);
			String title = cursor.getString(2);
			String filepath = cursor.getString(3);
			int duration = cursor.getInt(4);
			long albumId = cursor.getLong(5);
			String album = cursor.getString(6);
			int trackNum = cursor.getInt(7);

			Song song = new Song(id, artist, title, duration, filepath, albumId, album);
			rep.mSongList.add(song); // TODO DIFFERENT WAY !!!!!!!!!!!!!
			mSongList = rep.mSongList;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMediaPlayer.release();

		mHandler.removeCallbacks(mUpdateSongTime);

		// Stop the CURSOR loader
		if (mAudioCursorLoader != null) {
			mAudioCursorLoader.unregisterListener(this);
			mAudioCursorLoader.cancelLoad();
			mAudioCursorLoader.stopLoading();
		}
	}

	public void nextSong(Song song) {

	}

	public void playSong(Song song) throws IOException {
		mMediaPlayer.setDataSource(song.filepath);
		mMediaPlayer.prepareAsync();
		mPlayerState = PLAYING;
		mEventBus.post(new SongEvent(song, true));
		mHandler.postDelayed(mUpdateSongTime, UI_REFRESH_INTERVAL);
	}


	private Runnable mUpdateSongTime = new Runnable() {
		@SuppressLint("DefaultLocale")
		public void run() {
			int actualTime = mMediaPlayer.getCurrentPosition();
			mEventBus.post(new PlaybackEvent(actualTime));
			mHandler.postDelayed(this, UI_REFRESH_INTERVAL);
		}
	};

	public void pauseSong(Song song) {
		mPlayerState = PAUSED;
		mMediaPlayer.stop();
		mHandler.removeCallbacks(mUpdateSongTime);
		mEventBus.post(new SongEvent(song, false));
	}

	private int test = 0;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		String action;
		if (intent == null) {
			action = INIT;
		} else {
			action = intent.getAction();
		}

		try {
			switch (action) {
				case PLAY_PAUSE:
					if (mMediaPlayer.isPlaying()) {
						pauseSong(mSongList.get(test));
					} else {
						if (mPlayerState == PAUSED)
							mMediaPlayer.start();
						else if (mPlayerState == IDLE)
							playSong(mSongList.get(test));
					}
					break;

				case NEXT:
					test++;
					mMediaPlayer.stop();
					mMediaPlayer.reset();
					mPlayerState = PAUSED;
					playSong(mSongList.get(test));
					break;

				case PREVIOUS:
					test--;
					mMediaPlayer.stop();
					mMediaPlayer.reset();
					mPlayerState = PAUSED;
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


		return START_STICKY;
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
	 * Called when the end of a media source is reached during playback.
	 *
	 * @param mp the MediaPlayer that reached the end of the file
	 */
	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO should we somehow use 		mMediaPlayer.setNextMediaPlayer(); ??

		mp.stop();
		mp.reset();
		try {
			playSong(mSongList.get(++test));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
