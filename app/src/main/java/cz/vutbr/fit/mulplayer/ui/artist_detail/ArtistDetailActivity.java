package cz.vutbr.fit.mulplayer.ui.artist_detail;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.vutbr.fit.mulplayer.Constants;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.adapter.AlbumsListAdapter;
import cz.vutbr.fit.mulplayer.adapter.base.ClickableRecyclerAdapter;
import cz.vutbr.fit.mulplayer.model.MusicService;
import cz.vutbr.fit.mulplayer.ui.BaseActivity;
import cz.vutbr.fit.mulplayer.ui.album.AlbumActivity;
import cz.vutbr.fit.mulplayer.utils.SimpleDividerItemDecoration;
import icepick.Icepick;
import icepick.State;

/**
 * @author mlyko
 * @since 04.05.2016
 */
public class ArtistDetailActivity extends BaseActivity implements Loader.OnLoadCompleteListener<Cursor>, ClickableRecyclerAdapter.OnItemClickListener {
	public static final String EXTRA_ARTIST_ID = "artist_id";
	private static final int ARTIST_LOADER_ID = 0;
	private static final int ALBUMS_LOADER_ID = 1;
	private static final String TAG = ArtistDetailActivity.class.getSimpleName();

	@State long mArtistId;
	@Bind(R.id.artist_albums_list) RecyclerView mArtistAlbumsList;

	AlbumsListAdapter mAlbumsListAdapter;
	@Bind(R.id.artist_albums_songs) TextView mArtistAlbumsSongs;

	private CursorLoader mArtistInfoLoader;
	private CursorLoader mAlbumsLoader;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_artist_detail);
		ButterKnife.bind(this);
		Icepick.restoreInstanceState(this, savedInstanceState);

		Intent intent = getIntent();
		if (intent != null) {
			mArtistId = intent.getLongExtra(EXTRA_ARTIST_ID, 0);
		}

		if (mArtistId == 0) {
			Toast.makeText(this, R.string.artists_detail_no_id, Toast.LENGTH_LONG).show();
			finish();
		}

		initLoaders();
		initAlbumsList();
	}

	public void initLoaders() {
		// artist info
		mArtistInfoLoader = new CursorLoader(
				this,
				MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
				Constants.ARTISTS_PROJECTOR,
				BaseColumns._ID + " = ?",
				new String[]{String.valueOf(mArtistId)},
				null
		);
		mArtistInfoLoader.registerListener(ARTIST_LOADER_ID, this);

		// albums for this artist
		mAlbumsLoader = new CursorLoader(this);
		mAlbumsLoader.setUri(MediaStore.Audio.Artists.Albums.getContentUri("external", mArtistId));
		mAlbumsLoader.setProjection(Constants.ALBUMS_PROJECTOR);
		mAlbumsLoader.registerListener(ALBUMS_LOADER_ID, this);
	}

	public void initAlbumsList() {
		mAlbumsListAdapter = new AlbumsListAdapter(this, Constants.ALBUMS_PROJECTOR);
		mAlbumsListAdapter.setOnItemClickListener(this);

		mArtistAlbumsList.setLayoutManager(new LinearLayoutManager(this));
		mArtistAlbumsList.setItemAnimator(new DefaultItemAnimator());
		mArtistAlbumsList.setAdapter(mAlbumsListAdapter);
		mArtistAlbumsList.addItemDecoration(new SimpleDividerItemDecoration(this));
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

	/**
	 * Asynchronously loads artist information
	 *
	 * @param data cursor with artist data
	 */
	private void fillArtistInfo(Cursor data) {
		String artist = data.getString(data.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
		setupToolbar(artist, INDICATOR_BACK);

		int albumCount = data.getInt(data.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
		int songCount = data.getInt(data.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
		String albumQuantityString = getResources().getQuantityString(R.plurals.albums_count, albumCount, albumCount);
		String songQuantityString = getResources().getQuantityString(R.plurals.songs_count, songCount, songCount);
		mArtistAlbumsSongs.setText(String.format("%s | %s", albumQuantityString, songQuantityString));
	}

	private void fillAlbums(Cursor data) {
		mAlbumsListAdapter.swapCursor(data);
	}

	@Override
	public void onRecyclerViewItemClick(ClickableRecyclerAdapter.ViewHolder holder, int position, int viewType) {
		Intent intent = new Intent(this, AlbumActivity.class);
		intent.putExtra(AlbumActivity.EXTRA_ALBUM_ID, mAlbumsListAdapter.getItemId(position));
		startActivity(intent);
	}

	@OnClick(R.id.artist_random_all)
	public void onClick() {
		MusicService.fireAction(this, MusicService.CMD_PLAY_ARTIST, mArtistId);
	}
}
