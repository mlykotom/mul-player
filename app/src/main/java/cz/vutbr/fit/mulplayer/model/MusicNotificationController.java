package cz.vutbr.fit.mulplayer.model;

import android.app.Notification;
import android.os.Handler;
import android.util.Log;

import cz.vutbr.fit.mulplayer.model.entity.Song;

/**
 * @author mlyko
 * @since 07.05.2016
 */
public class MusicNotificationController {
	private static final String TAG = MusicNotificationController.class.getSimpleName();

	private static final int NOTIFICATION_ID = 412;

	private MusicService mMusicService;
	private boolean mStarted = false;

	private Handler mHandler = new Handler();

	private Runnable mDestroyNotification = new Runnable() {
		@Override
		public void run() {
			Log.d(TAG, "Stopping foreground service");
			mMusicService.stopForeground(true);
		}
	};

	public MusicNotificationController(MusicService service) {
		mMusicService = service;
	}

	public void startNotification() {
		if (mStarted) return;
		mHandler.removeCallbacks(mDestroyNotification);

		Song activeSong = mMusicService.mPlayback.getActiveSong();
		if (activeSong == null) {
			Log.e(TAG, "active song null");
			return;
		}

		Notification.Builder builder = new Notification.Builder(mMusicService);

		builder
				.setTicker("ahoj") // TODO
				.setOngoing(true)
				.setContentTitle(activeSong.getArtist())
				.setContentText(activeSong.getTitle());

		mMusicService.startForeground(NOTIFICATION_ID, builder.build());
	}

	public void stopNotification() {
		mStarted = false;
		mHandler.postDelayed(mDestroyNotification, MusicService.STOP_FOREGROUND_SERVICE_IN_MS);
	}
}
