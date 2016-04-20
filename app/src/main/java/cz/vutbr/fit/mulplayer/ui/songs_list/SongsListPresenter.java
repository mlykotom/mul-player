package cz.vutbr.fit.mulplayer.ui.songs_list;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import cz.vutbr.fit.mulplayer.Constants;
import cz.vutbr.fit.mulplayer.adapter.SongsListAdapter;
import cz.vutbr.fit.mulplayer.model.MusicService;
import cz.vutbr.fit.mulplayer.model.persistance.DataRepository;
import cz.vutbr.fit.mulplayer.ui.BaseFragmentPresenter;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class SongsListPresenter extends BaseFragmentPresenter implements Loader.OnLoadCompleteListener<Cursor> {
	private static final int LOADER_SONGS_MUSIC = 0;

	ISongsListView mFragment;
	CursorLoader mCursorLoader;
	DataRepository mData = DataRepository.getInstance();

	public SongsListPresenter(ISongsListView songsListFragment) {
		mFragment = songsListFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCursorLoader = new CursorLoader(
				mFragment.getActivity(),
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				Constants.SONG_PROJECTOR,
				Constants.MUSIC_SELECTOR,
				null,
				MediaStore.Audio.Media.TITLE_KEY
		);

		mCursorLoader.registerListener(LOADER_SONGS_MUSIC, this);
		mCursorLoader.startLoading();
	}

	@Override
	public void onCreateView() {
		super.onCreateView();
		mFragment.initList(Constants.SONG_PROJECTOR);
	}

	@Override
	public void onPause() {
		super.onPause();

		mCursorLoader.reset(); // TODO how to reset it?
//		mCursorLoader.unregisterListener(this);
//		mCursorLoader.cancelLoad();
//		mCursorLoader.stopLoading();
	}

	@Override
	public void onResume() {
		super.onResume();
		mCursorLoader.startLoading();
	}

	@Override
	public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
		mFragment.updateList(cursor);
	}

	/**
	 * When user clicked on item in list
	 *
	 * @param position
	 * @param viewType
	 */
	public void setOnRecyclerItemClick(int position, int viewType) {
		SongsListAdapter adapter = mFragment.getSongsListAdapter();
		Cursor cursor = adapter.getCursor();
		mData.queueSongs(cursor);
		MusicService.fireAction(mFragment.getActivity(), MusicService.CMD_PLAY_PAUSE, position - 1); // TODO why position - 1?
	}
}
