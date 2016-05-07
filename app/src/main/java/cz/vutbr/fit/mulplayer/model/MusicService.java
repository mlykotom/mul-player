package cz.vutbr.fit.mulplayer.model;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import cz.vutbr.fit.mulplayer.Constants;
import cz.vutbr.fit.mulplayer.model.entity.Song;
import cz.vutbr.fit.mulplayer.model.event.PlaybackEvent;
import cz.vutbr.fit.mulplayer.model.persistance.DataRepository;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class MusicService extends Service implements Playback.IPlaybackCallback, Loader.OnLoadCompleteListener<Cursor> {
	private static final String TAG = MusicService.class.getSimpleName();

	private static final String ACTION_CMD = "cz.vutbr.fit.mulplayer.PLAYER_ACTION";
	private static final String ACTION_CMD_NAME = "CMD_NAME";
	private static final String ACTION_CMD_VALUE = "CMD_VALUE";
	private static final String ACTION_CMD_VALUE2 = "CMD_VALUE2";

	private static final int PENDING_INTENT_REQUEST_CODE = 100;
	public static final long STOP_FOREGROUND_SERVICE_IN_MS = 60 * 1000; // turn off foreground service after minute
	private static final int UI_REFRESH_INTERVAL_MS = 250;
	private static final int LOADER_SONGS_MUSIC = 0;
	private MusicNotificationController mNotificationController;

	@StringDef({
			// -- queue
			CMD_PLAY_ARTIST, CMD_PLAY_ALBUM, CMD_PLAY_ALL_SONGS, CMD_PLAY_SONG_FROM_ALBUM,
			// -- playback
			CMD_PLAY_FORCE, CMD_PLAY_PAUSE, CMD_SEEK_TO, CMD_PREVIOUS, CMD_NEXT
	})
	@interface MusicCommand {
	}

	// ----- queue controls
	public static final String CMD_PLAY_ARTIST = "CMD_PLAY_ARTIST";
	public static final String CMD_PLAY_ALBUM = "CMD_PLAY_ALBUM";
	public static final String CMD_PLAY_ALL_SONGS = "CMD_PLAY_ALL_SONGS";
	public static final String CMD_PLAY_SONG_FROM_ALBUM = "CMD_PLAY_SONG_FROM_ALBUM";

	// ----- playback controls
	public static final String CMD_PLAY_FORCE = "CMD_PLAY_FORCE";
	public static final String CMD_PLAY_PAUSE = "CMD_PLAY_PAUSE";
	public static final String CMD_SEEK_TO = "CMD_SEEK_TO";
	public static final String CMD_NEXT = "CMD_NEXT";
	public static final String CMD_PREVIOUS = "CMD_PREVIOUS";

	Playback mPlayback;
	CursorLoader mSongLoader;
	private int mActiveQueuePosition = 0;
	private long mPlaySongId = Constants.NO_ID;

	DataRepository mData = DataRepository.getInstance();
	EventBus mEventBus = EventBus.getDefault();

	private Handler mHandler = new Handler();

	/**
	 * Runnable for updating UI when time is changing
	 */
	private Runnable mUpdateSongTime = new Runnable() {
		public void run() {
			boolean isSubscriber = mEventBus.hasSubscriberForEvent(PlaybackEvent.class);
			if (isSubscriber) {
				int actualTime = mPlayback.getCurrentPosition();
				mEventBus.post(new PlaybackEvent(mPlayback.getActiveSong(), true, actualTime));
			}

			mHandler.postDelayed(this, UI_REFRESH_INTERVAL_MS);
		}
	};

	/**
	 * Called only once when service not existing (first time or after service got killed)
	 * Prepares cursor loader for queuing songs + playback object
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		mSongLoader = new CursorLoader(this);
		mSongLoader.setUri(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
		mSongLoader.setProjection(Constants.SONG_PROJECTOR);
		mSongLoader.registerListener(LOADER_SONGS_MUSIC, this);

		mNotificationController = new MusicNotificationController(this);
		mPlayback = new Playback(this, this);
	}

	/**
	 * Fires specified action on player's service
	 *
	 * @param context application's context
	 * @param command {@link MusicCommand}
	 * @param value   any long value which can be set (albumId,songId,..)
	 * @param value2  any long value (same as #value)
	 */
	public static void fireAction(Context context, @MusicCommand String command, long value, long value2) {
		Intent intent = new Intent(context, MusicService.class);
		intent.setAction(ACTION_CMD);
		intent.putExtra(ACTION_CMD_NAME, command);
		intent.putExtra(ACTION_CMD_VALUE, value);
		intent.putExtra(ACTION_CMD_VALUE2, value2);
		context.startService(intent);
	}

	public static void fireAction(Context context, @MusicCommand String command, long value) {
		fireAction(context, command, value, Constants.NO_POSITION);
	}

	public static void fireAction(Context context, @MusicCommand String command) {
		fireAction(context, command, Constants.NO_POSITION);
	}

	/**
	 * Pending intent to run from widget / notification
	 *
	 * @param context where will be intent fired
	 * @param command music command (next/prev/play/pause)
	 * @return pending intent to set in widget / notification
	 */
	public static PendingIntent getFireActionPending(Context context, @MusicCommand String command) {
		Intent intent = new Intent(context, MusicService.class);
		intent.setAction(ACTION_CMD);
		intent.putExtra(ACTION_CMD_NAME, command);

		return PendingIntent.getService(context, PENDING_INTENT_REQUEST_CODE, intent, PendingIntent.FLAG_CANCEL_CURRENT);
	}

	/**
	 * Called every time any action was fired to this service.
	 *
	 * @param intent  holding information about action
	 * @param flags
	 * @param startId
	 * @return how should android handle this service -> START_STICKY = restart it if killed
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			String action = intent.getAction();
			if (ACTION_CMD.equals(action)) {
				@MusicCommand String command = intent.getStringExtra(ACTION_CMD_NAME);
				long value = intent.getLongExtra(ACTION_CMD_VALUE, Constants.NO_POSITION);
				long value2 = intent.getLongExtra(ACTION_CMD_VALUE2, Constants.NO_ID);

				switch (command) {
					// -------- queue commands -------- //
					case CMD_PLAY_ARTIST:
						mPlaySongId = Constants.NO_POSITION;
						buildArtistQueue(value);
						return START_STICKY;

					case CMD_PLAY_SONG_FROM_ALBUM:
						mPlaySongId = value2;
						buildAlbumQueue(value);
						return START_STICKY;

					case CMD_PLAY_ALBUM:
						mPlaySongId = Constants.NO_POSITION;
						buildAlbumQueue(value);
						return START_STICKY;

					case CMD_PLAY_ALL_SONGS:
						mPlaySongId = value;     // sets song id which wants to be played
						buildAllSongsQueue();
						return START_STICKY;

					// -------- playback commands -------- //
					case CMD_PLAY_PAUSE:
						if (mPlayback.isPlaying()) {                        // TODO check if player in allowed state
							mPlayback.pause();
							return START_STICKY;
						}
					case CMD_PLAY_FORCE:
						playFromQueue((int) value);
						break;

					case CMD_SEEK_TO:
						mPlayback.seekTo((int) value);
						break;

					case CMD_NEXT:
						playFromQueue(mActiveQueuePosition + 1);
						break;

					case CMD_PREVIOUS:
						playFromQueue(mActiveQueuePosition - 1);
						break;
				}
			}
		}

		return START_STICKY;
	}

	/**
	 * Builds queue based on selected album
	 *
	 * @param albumId id from mediastore
	 */
	private void buildAlbumQueue(long albumId) {
		mSongLoader.reset();
		mSongLoader.setSelection(Constants.MUSIC_SELECTOR + " AND " + MediaStore.Audio.Media.ALBUM_ID + " = ?");
		mSongLoader.setSelectionArgs(new String[]{String.valueOf(albumId)});
		mSongLoader.startLoading();
	}

	/**
	 * Builds all songs and tries to playit from selected song
	 */
	private void buildAllSongsQueue() {
		mSongLoader.reset();
		mSongLoader.setSelection(Constants.MUSIC_SELECTOR);
		mSongLoader.setSelectionArgs(null);
		mSongLoader.startLoading();
	}

	/**
	 * Builds queue based on selected artist
	 * Must be used asynchronously - this only set loading of songs
	 *
	 * @param artistId id from mediaStore
	 */
	private void buildArtistQueue(long artistId) {
		mSongLoader.reset();
		mSongLoader.setSelection(Constants.MUSIC_SELECTOR + " AND " + MediaStore.Audio.Media.ARTIST_ID + " = ?");
		mSongLoader.setSelectionArgs(new String[]{String.valueOf(artistId)});
		mSongLoader.startLoading();
	}

	/**
	 * Starts playing built queue of songs
	 * Called on the thread that created the Loader when the load is complete.
	 *
	 * @param loader the loader that completed the load
	 * @param data   the result of the load
	 */
	@Override
	public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
		int pos = mData.queueSongsAndFindPosition(data, mPlaySongId);
		mPlaySongId = Constants.NO_ID;
		fireAction(this, CMD_PLAY_FORCE, pos);
	}

	/**
	 * TODO should rebuild queue from saved or build all songs
	 */
	private void rebuildQueue() {

	}

	/**
	 * Tries to play song from queue list.
	 * If index < 0, plays actual song (unpause).
	 * If index > queue size, plays from the beginning
	 *
	 * @param index from queue list
	 */
	private void playFromQueue(int index) {
		if (mData.mQueueOrderList.isEmpty()) {
			// TODO try to rebuild queue first
			rebuildQueue();
			// this should never happen
			Toast.makeText(this, "No queued songs to play!", Toast.LENGTH_LONG).show();
			return;
		}

		// check boundaries first
		if (index < 0) index = mActiveQueuePosition;
		if (index >= mData.mQueueOrderList.size()) index = 0;

		long songId = mData.mQueueOrderList.get(index);
		mActiveQueuePosition = index;
		Song song = mData.mQueueSongs.get(songId); // TODO what to do when song not there?
		mPlayback.play(song);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacks(mUpdateSongTime);
		mPlayback.release();
		mSongLoader.stopLoading();
	}

	@Override
	public void onPlaybackStatusChanged(@PlaybackStateCompat.State int state) {
		switch (state) {
			case PlaybackStateCompat.STATE_BUFFERING:
				// TODO?
				break;

			case PlaybackStateCompat.STATE_PLAYING:
				mNotificationController.startNotification();
				mHandler.postDelayed(mUpdateSongTime, UI_REFRESH_INTERVAL_MS);
				break;

			case PlaybackStateCompat.STATE_STOPPED:
				// TODO?
			case PlaybackStateCompat.STATE_PAUSED:
				mNotificationController.stopNotification();

				mHandler.removeCallbacks(mUpdateSongTime);
				mEventBus.post(new PlaybackEvent(mPlayback.getActiveSong(), false));
				break;
		}
	}

	@Override
	public void onPlaybackCompleted() {
		playFromQueue(mActiveQueuePosition + 1);
	}

	@Override
	public void onError(String error) {
		Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
	}

	/**
	 * Just has to be here
	 **/
	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
