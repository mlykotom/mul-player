package cz.vutbr.fit.mulplayer.model;

import android.app.Notification;
import android.os.Handler;
import android.util.Log;

import cz.vutbr.fit.mulplayer.R;
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

	private Notification.Builder mBuilder;

	private Notification.Action mNextAction;
	private Notification.Action mPlayAction;
	private Notification.Action mPrevAction;

	private Runnable mDestroyNotification = new Runnable() {
		@Override
		public void run() {
			Log.d(TAG, "Stopping foreground service");
			mMusicService.stopForeground(true);
		}
	};

	public MusicNotificationController(MusicService service) {
		mMusicService = service;

		mBuilder = new Notification.Builder(mMusicService);
		mPrevAction = new Notification.Action(R.drawable.ic_skip_previous_black_24dp, "Previous", MusicService.getFireActionPending(mMusicService, MusicService.CMD_PREVIOUS));
		mPlayAction = new Notification.Action(R.drawable.ic_play_arrow_black_24dp, "Play", MusicService.getFireActionPending(mMusicService, MusicService.CMD_PLAY_PAUSE));
		mNextAction = new Notification.Action(R.drawable.ic_skip_next_black_24dp, "Next", MusicService.getFireActionPending(mMusicService, MusicService.CMD_NEXT));
	}

	private void createNotification() {
		mBuilder
				.setOngoing(true)
				.addAction(mPrevAction)
				.addAction(mPlayAction)
				.addAction(mNextAction);
	}

	public void startNotification() {
		if (mStarted) return;
		mHandler.removeCallbacks(mDestroyNotification);

		createNotification();

		Song activeSong = mMusicService.mPlayback.getActiveSong();
		if (activeSong == null) {
			Log.e(TAG, "active song null");
			return;
		}

		mBuilder
				.setSmallIcon(R.drawable.ic_play_arrow_black_24dp)
				.setContentTitle(activeSong.getArtist())
				.setContentText(activeSong.getTitle());


		Notification notification = mBuilder.build();

		mMusicService.startForeground(NOTIFICATION_ID, notification);
		mStarted = true;
	}

	public void stopNotification() {
		mStarted = false;

		mBuilder
				.setSmallIcon(R.drawable.ic_pause_black_24dp);

		mMusicService.startForeground(NOTIFICATION_ID, mBuilder.build());


		mHandler.postDelayed(mDestroyNotification, MusicService.STOP_FOREGROUND_SERVICE_IN_MS);
	}
}
