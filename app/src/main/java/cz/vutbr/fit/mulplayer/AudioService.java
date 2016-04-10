package cz.vutbr.fit.mulplayer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import java.util.ArrayList;
import java.util.List;

import cz.vutbr.fit.mulplayer.model.Song;

/**
 * @author mlyko
 * @since 10.04.2016
 */
public class AudioService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
	@StringDef({PLAY, PAUSE, STOP})
	@interface AudioAction {
	}

	public static final String PLAY = "cz.vutbr.fit.mulplayer.action.PLAY";
	public static final String PAUSE = "cz.vutbr.fit.mulplayer.action.PAUSE";
	public static final String STOP = "cz.vutbr.fit.mulplayer.action.STOP";

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

	/**
	 * Does action on service
	 *
	 * @param context application's context
	 * @param action  {@link AudioAction}
	 */
	public static void action(Context context, @AudioAction String action) {
		Intent intent = new Intent(context, AudioService.class);
		intent.setAction(action);
		context.startService(intent);
	}


	@Override
	public void onCreate() {
		super.onCreate();
		mMediaPlayer = new MediaPlayer();
		mMediaPlayer.setOnPreparedListener(this);
		mMediaPlayer.setOnErrorListener(this);
		mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mMediaPlayer.release();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (intent == null) {
			return START_STICKY;
			// TODO
		}

		switch (intent.getAction()) {
			case PLAY:
//				mMediaPlayer.setDataSource(); // TODO
				mMediaPlayer.prepareAsync();
				break;
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

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
}
