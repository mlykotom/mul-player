package cz.vutbr.fit.mulplayer.ui.album;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.ui.BaseActivity;
import icepick.Icepick;
import icepick.State;

/**
 * @author mlyko
 * @since 18.04.2016
 */
public class AlbumActivity extends BaseActivity {
	public static final String EXTRA_ALBUM_ID = "album_id";

	AlbumPresenter mPresenter;
	@Bind(R.id.album_artist) TextView mAlbumArtist;
	@Bind(R.id.album_name) TextView mAlbumName;

	@State long mAlbumId;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		mBasePresenter = mPresenter = new AlbumPresenter(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);
		ButterKnife.bind(this);
		setupToolbar(R.string.album_title, INDICATOR_BACK);
		Icepick.restoreInstanceState(this, savedInstanceState);

		Intent intent = getIntent();
		if(intent != null){
			mAlbumId = intent.getLongExtra(EXTRA_ALBUM_ID, 0);
		}

		mAlbumName.setText(String.valueOf(mAlbumId));
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Icepick.saveInstanceState(this, outState);
	}
}
