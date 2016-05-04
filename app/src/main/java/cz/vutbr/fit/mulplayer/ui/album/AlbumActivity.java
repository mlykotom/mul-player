package cz.vutbr.fit.mulplayer.ui.album;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.Constants;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.ui.BaseActivity;
import icepick.Icepick;
import icepick.State;

/**
 * @author mlyko
 * @since 18.04.2016
 */
public class AlbumActivity extends BaseActivity implements Loader.OnLoadCompleteListener<Cursor> {
	public static final String EXTRA_ALBUM_ID = "album_id";
	private static final int ALBUM_LOADER_ID = 0;

	AlbumPresenter mPresenter;
	@Bind(R.id.album_artist) TextView mAlbumArtist;
	@Bind(R.id.album_name) TextView mAlbumName;
	@Bind(R.id.album_cover)	ImageView mAlbumCover;

	@State long mAlbumId;

	private CursorLoader mAlbumInfoLoader;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		mBasePresenter = mPresenter = new AlbumPresenter(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_album);
		ButterKnife.bind(this);
		setupToolbar(R.string.albums_detail_title, INDICATOR_BACK);
		Icepick.restoreInstanceState(this, savedInstanceState);

		Intent intent = getIntent();
		if(intent != null){
			mAlbumId = intent.getLongExtra(EXTRA_ALBUM_ID, 0);
		}

		mAlbumName.setText(String.valueOf(mAlbumId));

		mAlbumInfoLoader = new CursorLoader(
				this,
				MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
				Constants.ALBUMS_PROJECTOR,
				"_id = ?",
				new String[]{String.valueOf(mAlbumId)},
				null
		);
		mAlbumInfoLoader.registerListener(ALBUM_LOADER_ID,this);

	}

	@Override
	protected void onStart() {
		super.onStart();
		mAlbumInfoLoader.startLoading();

	}

	@Override
	protected void onStop() {
		super.onStop();
		mAlbumInfoLoader.reset();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Icepick.saveInstanceState(this, outState);
	}

	@Override
	public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
		if (loader.getId() == ALBUM_LOADER_ID){
			if (data != null) {
				int i = data.getCount();
				if (data.moveToFirst()){

					String path = data.getString(data.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
					Drawable img = Drawable.createFromPath(path);
					mAlbumCover.setImageDrawable(img);
				}

//				Bitmap bmp= BitmapFactory.decodeFile(path);
//				mAlbumCover.setImageBitmap(bmp);{

			}
		}
	}
}
