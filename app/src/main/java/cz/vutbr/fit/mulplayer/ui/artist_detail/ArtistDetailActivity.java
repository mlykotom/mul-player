package cz.vutbr.fit.mulplayer.ui.artist_detail;

import android.os.Bundle;
import android.support.annotation.Nullable;

import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.ui.BaseActivity;

/**
 * @author mlyko
 * @since 04.05.2016
 */
public class ArtistDetailActivity extends BaseActivity {
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artist_detail);
	}
}
