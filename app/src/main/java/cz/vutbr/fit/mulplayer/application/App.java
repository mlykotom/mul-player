package cz.vutbr.fit.mulplayer.application;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.ActivityCompat;

import com.squareup.picasso.Picasso;

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

	}

	public static Context getContext() {
		return sContext;
	}
}
