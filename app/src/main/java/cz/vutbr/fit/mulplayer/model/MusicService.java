package cz.vutbr.fit.mulplayer.model;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.media.session.PlaybackStateCompat;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import cz.vutbr.fit.mulplayer.model.entity.Song;
import cz.vutbr.fit.mulplayer.model.event.PlaybackEvent;
import cz.vutbr.fit.mulplayer.model.event.SongEvent;
import cz.vutbr.fit.mulplayer.model.persistance.DataRepository;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class MusicService extends Service implements Playback.IPlaybackCallback {
	private static final String ACTION_CMD = "cz.vutbr.fit.mulplayer.PLAYER_ACTION";
	private static final String ACTION_CMD_NAME = "CMD_NAME";
	private static final String ACTION_CMD_VALUE = "CMD_VALUE";

	private static final int UI_REFRESH_INTERVAL_MS = 250;

	@StringDef({CMD_REBUILD, CMD_PLAY_PAUSE, CMD_SEEK_TO, CMD_PREVIOUS, CMD_NEXT})
	@interface MusicCommand {
	}

	public static final String CMD_REBUILD = "CMD_REBUILD";
	public static final String CMD_PLAY_PAUSE = "CMD_PLAY_PAUSE";
	public static final String CMD_SEEK_TO = "CMD_SEEK_TO";
	public static final String CMD_NEXT = "CMD_NEXT";
	public static final String CMD_PREVIOUS = "CMD_PREVIOUS";

	Playback mPlayback;
	private int mActiveQueuePosition = 0;

	DataRepository mData = DataRepository.getInstance();
	EventBus mEventBus = EventBus.getDefault();

	private Handler mHandler = new Handler();

	private Runnable mUpdateSongTime = new Runnable() {
		public void run() {
			int actualTime = mPlayback.getCurrentPosition();
			mEventBus.post(new PlaybackEvent(actualTime));
			mHandler.postDelayed(this, UI_REFRESH_INTERVAL_MS);
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();
		mPlayback = new Playback(this, this);
	}

	/**
	 * Fires specified action on player's service
	 *
	 * @param context application's context
	 * @param command {@link MusicCommand}
	 * @param value
	 */
	public static void fireAction(Context context, @MusicCommand String command, long value) {
		Intent intent = new Intent(context, MusicService.class);
		intent.setAction(ACTION_CMD);
		intent.putExtra(ACTION_CMD_NAME, command);
		intent.putExtra(ACTION_CMD_VALUE, value);
		context.startService(intent);
	}

	public static void fireAction(Context context, @MusicCommand String command) {
		fireAction(context, command, -1);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent != null) {
			String action = intent.getAction();
			if (ACTION_CMD.equals(action)) {
				@MusicCommand String command = intent.getStringExtra(ACTION_CMD_NAME);
				if (CMD_REBUILD.equals(command)) {
					// TODO rebuild saved queue from list of ids
					rebuildQueue();
				}
				long value = intent.getLongExtra(ACTION_CMD_VALUE, -1);

				switch (command) {
					case CMD_PLAY_PAUSE:
						if (mPlayback.isPlaying()) {
							mPlayback.pause();
						} else {
							playFromQueue((int) value);
						}
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
	}

	@Override
	public void onPlaybackStatusChanged(@PlaybackStateCompat.State int state) {
		switch (state) {
			case PlaybackStateCompat.STATE_BUFFERING:
				// TODO?
				break;

			case PlaybackStateCompat.STATE_PLAYING:
				Song activeSong = mPlayback.getActiveSong();
				mEventBus.post(new SongEvent(activeSong, true));
				mHandler.postDelayed(mUpdateSongTime, UI_REFRESH_INTERVAL_MS);
				break;

			case PlaybackStateCompat.STATE_STOPPED:
				// TODO?
			case PlaybackStateCompat.STATE_PAUSED:
				mHandler.removeCallbacks(mUpdateSongTime);
				mEventBus.post(new SongEvent(null, false));
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
	 * Just has to be here
	 **/
	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
