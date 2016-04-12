package cz.vutbr.fit.mulplayer.model;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import cz.vutbr.fit.mulplayer.model.entity.Song;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class MusicService extends Service {
	Playback mPlayback;

	@Override
	public void onCreate() {
		super.onCreate();

		mPlayback = new Playback(this);
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
