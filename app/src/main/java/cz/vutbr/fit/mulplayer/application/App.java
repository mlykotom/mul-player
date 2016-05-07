package cz.vutbr.fit.mulplayer.application;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.squareup.picasso.Picasso;

import cz.vutbr.fit.mulplayer.model.MusicService;
import cz.vutbr.fit.mulplayer.model.persistance.DataRepository;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class App extends Application {

	private static Context sContext;

	@Override
	public void onCreate() {
		super.onCreate();
		sContext = getApplicationContext();

		Picasso.Builder builder = new Picasso.Builder(this);
		Picasso built = builder.build();
		built.setIndicatorsEnabled(false);
		built.setLoggingEnabled(false);
		Picasso.setSingletonInstance(built);

		if (DataRepository.getInstance().mQueueSongs.size() == 0) {
			Log.i(this.getPackageName(), "No queue in App, trying to rebuild it.");
			MusicService.fireAction(this, MusicService.CMD_REBUILD_QUEUE);
		}
	}

	public static Context getContext() {
		return sContext;
	}
}
