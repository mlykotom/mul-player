package cz.vutbr.fit.mulplayer.model;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.model.entity.Song;
import cz.vutbr.fit.mulplayer.ui.main.MainActivity;

/**
 * @author mlyko
 * @since 07.05.2016
 */
public class MusicNotificationController {
	private static final String TAG = MusicNotificationController.class.getSimpleName();

	private static final int NOTIFICATION_ID = 412;
	private static final int REQUEST_PREV = 101;
	private static final int REQUEST_PLAY_PAUSE = 102;
	private static final int REQUEST_NEXT = 103;

	private MusicService mMusicService;
	private boolean mStarted = false;

	private Handler mHandler = new Handler();

	private Notification.Builder mBuilder;

	private Notification.Action mNextAction;
	private Notification.Action mPlayAction;
	private Notification.Action mPauseAction;
	private Notification.Action mPrevAction;

	private Runnable mDestroyNotification = new Runnable() {
		@Override
		public void run() {
			Log.d(TAG, "Stopping foreground service");
			mMusicService.stopForeground(true);
		}
	};
	private PendingIntent mOnClickPending;

	public MusicNotificationController(MusicService service) {
		mMusicService = service;

		mOnClickPending = PendingIntent.getActivity(service, 0, new Intent(service, MainActivity.class), 0);

		mBuilder = new Notification.Builder(mMusicService);
		mPrevAction = new Notification.Action(R.drawable.ic_skip_previous_black_24dp, "Previous", MusicService.getFireActionPending(mMusicService, REQUEST_PREV, MusicService.CMD_PREVIOUS));
		mPlayAction = new Notification.Action(R.drawable.ic_play_arrow_black_24dp, "Play", MusicService.getFireActionPending(mMusicService, REQUEST_PLAY_PAUSE, MusicService.CMD_PLAY_PAUSE));
		mPauseAction = new Notification.Action(R.drawable.ic_pause_black_24dp, "Pause", MusicService.getFireActionPending(mMusicService, REQUEST_PLAY_PAUSE, MusicService.CMD_PLAY_PAUSE));
		mNextAction = new Notification.Action(R.drawable.ic_skip_next_black_24dp, "Next", MusicService.getFireActionPending(mMusicService, REQUEST_NEXT, MusicService.CMD_NEXT));
	}

	private void createNotification() {
		mBuilder.setOngoing(true);
	}

	public void startNotification() {
//		if (mStarted) return;
		mHandler.removeCallbacks(mDestroyNotification);

		createNotification();

		Song activeSong = mMusicService.mPlayback.getActiveSong();
		if (activeSong == null) {
			Log.e(TAG, "active song null");
			return;
		}

		mBuilder
				.addAction(mPrevAction)
				.addAction(mPlayAction)
				.addAction(mNextAction)
				.setContentIntent(mOnClickPending)
				.setSmallIcon(R.drawable.ic_play_arrow_black_24dp)
				.setContentTitle(activeSong.getArtist())
				.setContentText(activeSong.getTitle());


		Notification notification = mBuilder.build();
		cancel();
		mMusicService.startForeground(NOTIFICATION_ID, notification);
		mStarted = true;
	}

	public void stopNotification() {
		mStarted = false;
//
		// TODO

//		mBuilder
//				.addAction(mPrevAction)
//				.addAction(mPlayAction)
//				.addAction(mNextAction)
//				.setOngoing(false)
//				.setSmallIcon(R.drawable.ic_pause_black_24dp);

//		cancel();
		mMusicService.startForeground(NOTIFICATION_ID, mBuilder.build());
		mHandler.postDelayed(mDestroyNotification, MusicService.STOP_FOREGROUND_SERVICE_IN_MS);
	}

	private void cancel() {
		NotificationManagerCompat managerCompat = NotificationManagerCompat.from(mMusicService);
		managerCompat.cancel(NOTIFICATION_ID);
	}
}
