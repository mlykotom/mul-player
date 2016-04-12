package cz.vutbr.fit.mulplayer;

import android.app.Application;

import com.squareup.picasso.Picasso;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class Global extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Picasso.Builder builder = new Picasso.Builder(this);
		Picasso built = builder.build();
		built.setIndicatorsEnabled(true);
		built.setLoggingEnabled(true);
		Picasso.setSingletonInstance(built);
	}
}
