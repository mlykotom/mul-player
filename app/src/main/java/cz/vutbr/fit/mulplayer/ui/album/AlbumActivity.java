package cz.vutbr.fit.mulplayer.ui.album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.ui.BaseActivity;

/**
 * @author mlyko
 * @since 18.04.2016
 */
public class AlbumActivity extends BaseActivity {
	AlbumPresenter mPresenter;
	@Bind(R.id.album_artist) TextView mAlbumArtist;
	@Bind(R.id.album_name) TextView mAlbumName;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		mBasePresenter = mPresenter = new AlbumPresenter(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);
		ButterKnife.bind(this);
		setupToolbar(R.string.album_title, );
	}
}
