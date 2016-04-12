package cz.vutbr.fit.mulplayer.ui.songs_list;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import cz.vutbr.fit.mulplayer.ui.BaseFragmentPresenter;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class SongsListPresenter extends BaseFragmentPresenter implements Loader.OnLoadCompleteListener<Cursor> {
	private static final int LOADER_SONGS_MUSIC = 0;

	ISongsListView mFragment;
	CursorLoader mCursorLoader;

	//Some audio may be explicitly marked as not being music
	String mSelection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

	String[] mProjection = {
			MediaStore.Audio.Media._ID,
			MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media.TITLE,
			MediaStore.Audio.Media.DATA,
			MediaStore.Audio.Media.DURATION,
			MediaStore.Audio.Media.ALBUM_ID,
			MediaStore.Audio.Media.ALBUM,
			MediaStore.Audio.Media.TRACK
	};

	public SongsListPresenter(ISongsListView songsListFragment) {
		mFragment = songsListFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCursorLoader = new CursorLoader(
				mFragment.getActivity(),
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				mProjection,
				mSelection,
				null,
				MediaStore.Audio.Media.TITLE_KEY
		);

		mCursorLoader.registerListener(LOADER_SONGS_MUSIC, this);
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

	/**
	 * When user clicked on item in list
	 *
	 * @param position
	 * @param viewType
	 */
	public void setOnRecyclerItemClick(int position, int viewType) {

	}
}
