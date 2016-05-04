package cz.vutbr.fit.mulplayer.ui.artist_detail;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.Constants;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.adapter.AlbumsListAdapter;
import cz.vutbr.fit.mulplayer.ui.BaseActivity;
import icepick.Icepick;
import icepick.State;

/**
 * @author mlyko
 * @since 04.05.2016
 */
public class ArtistDetailActivity extends BaseActivity implements Loader.OnLoadCompleteListener<Cursor> {
	public static final String EXTRA_ARTIST_ID = "artist_id";
	private static final int ARTIST_LOADER_ID = 0;
	private static final int ALBUMS_LOADER_ID = 1;
	private static final String TAG = ArtistDetailActivity.class.getSimpleName();

	@State long mArtistId;
	@Bind(R.id.artist_name) TextView mArtistName;
	@Bind(R.id.artist_albums_list) RecyclerView mArtistAlbumsList;

	AlbumsListAdapter mAlbumsListAdapter;

	private CursorLoader mArtistInfoLoader;
	private CursorLoader mAlbumsLoader;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artist_detail);
		ButterKnife.bind(this);
		setupToolbar(R.string.artists_detail_title, INDICATOR_BACK);
		Icepick.restoreInstanceState(this, savedInstanceState);

		Intent intent = getIntent();
		if (intent != null) {
			mArtistId = intent.getLongExtra(EXTRA_ARTIST_ID, 0);
		}

		mArtistInfoLoader = new CursorLoader(
				this,
				MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
				Constants.ARTISTS_PROJECTOR,
				"_id = ?",
				new String[]{String.valueOf(mArtistId)},
				null
		);
		mArtistInfoLoader.registerListener(ARTIST_LOADER_ID, this);

		mAlbumsListAdapter = new AlbumsListAdapter(this, null, )


		mAlbumsLoader = new CursorLoader(this);
		mAlbumsLoader.setUri(MediaStore.Audio.Artists.Albums.getContentUri("external", mArtistId));
		mAlbumsLoader.setProjection(Constants.ALBUMS_PROJECTOR);
		mAlbumsLoader.registerListener(ALBUMS_LOADER_ID, this);
	}


	@Override
	protected void onStart() {
		super.onStart();
		mArtistInfoLoader.startLoading();
		mAlbumsLoader.startLoading();
	}

	@Override
	protected void onStop() {
		super.onStop();
		mArtistInfoLoader.reset();
		mAlbumsLoader.reset();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Icepick.saveInstanceState(this, outState);
	}

	/**
	 * Called on the thread that created the Loader when the load is complete.
	 *
	 * @param loader the loader that completed the load
	 * @param data   the result of the load
	 */
	@Override
	public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
		switch (loader.getId()) {
			case ARTIST_LOADER_ID:
				data.moveToFirst();
				fillArtistInfo(data);
				break;

			case ALBUMS_LOADER_ID:
				fillAlbums(data);
				break;
			default:
				Log.e(TAG, "Load completion not specified for loader with id " + loader.getId());
		}
	}

	private void fillArtistInfo(Cursor data) {
		String artist = data.getString(1);

		mArtistName.setText(artist);
	}

	private void fillAlbums(Cursor data) {
	}

}
