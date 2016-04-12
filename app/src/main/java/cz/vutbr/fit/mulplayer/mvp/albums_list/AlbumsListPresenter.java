package cz.vutbr.fit.mulplayer.mvp.albums_list;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import cz.vutbr.fit.mulplayer.mvp.BaseFragmentPresenter;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class AlbumsListPresenter extends BaseFragmentPresenter implements Loader.OnLoadCompleteListener<Cursor> {
	private static final int LOADER_ALBUMS_MUSIC = 0;

	AlbumsListFragment mFragment;
	CursorLoader mCursorLoader;

	String[] mProjection = {
			MediaStore.Audio.Albums._ID,
			MediaStore.Audio.Albums.ALBUM_ART,
			MediaStore.Audio.Albums.ALBUM
	};

	public AlbumsListPresenter(AlbumsListFragment fragment) {
		mFragment = fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCursorLoader = new CursorLoader(
				mFragment.getActivity(),
				MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
				mProjection,
				null,
				null,
				null
		);

		mCursorLoader.registerListener(LOADER_ALBUMS_MUSIC, this);
		mCursorLoader.startLoading();
	}

	@Override
	public void onCreateView() {
		super.onCreateView();
		mFragment.initList(mProjection);
	}

	@Override
	public void onPause() {
		super.onPause();
		mCursorLoader.reset();
	}

	@Override
	public void onResume() {
		super.onResume();
		mCursorLoader.startLoading();
	}

	@Override
	public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
		mFragment.updateList(data);
	}
}
