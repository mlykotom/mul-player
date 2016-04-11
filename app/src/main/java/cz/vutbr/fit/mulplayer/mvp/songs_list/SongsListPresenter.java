package cz.vutbr.fit.mulplayer.mvp.songs_list;

import android.os.Bundle;

import cz.vutbr.fit.mulplayer.model.DataRepository;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class SongsListPresenter {
	SongsListFragment mFragment;

	public SongsListPresenter(SongsListFragment songsListFragment) {
		mFragment = songsListFragment;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		mFragment.mSongsListAdapter.updateData(DataRepository.getInstance().mSongList);
	}
}
