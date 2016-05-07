package cz.vutbr.fit.mulplayer.ui.artists_list;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import cz.vutbr.fit.mulplayer.Constants;
import cz.vutbr.fit.mulplayer.adapter.ArtistsListAdapter;
import cz.vutbr.fit.mulplayer.adapter.base.ClickableRecyclerAdapter;
import cz.vutbr.fit.mulplayer.ui.SortableListPresenter;
import cz.vutbr.fit.mulplayer.ui.artist_detail.ArtistDetailActivity;

/**
 * @author mlyko
 * @since 16.04.2016
 */
public class ArtistsListPresenter extends SortableListPresenter implements Loader.OnLoadCompleteListener<Cursor>, ClickableRecyclerAdapter.OnItemClickListener {
	private static final int LOADER_ARTISTS_MUSIC = 0;

	ArtistsListFragment mFragment;
	CursorLoader mCursorLoader;

	public ArtistsListPresenter(ArtistsListFragment fragment) {
		super(fragment);
		mFragment = fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mCursorLoader = new CursorLoader(
				getBaseActivity(),
				MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
				Constants.ARTISTS_PROJECTOR,
				null,
				null,
				mOrderKey + mOrderAscDesc
		);
		mCursorLoader.registerListener(LOADER_ARTISTS_MUSIC, this);
	}

	/**
	 * Setups which ordering will be default (must be one from mOrderMap!)
	 *
	 * @return string for CursorLoader ordering
	 */
	@Override
	public String getDefaultOrderKey() {
		return MediaStore.Audio.Artists.ARTIST_KEY;
	}

	/**
	 * @return Prefix for actual fragment so that many ordering keys may be set in one preference file
	 */
	@Override
	public String getKeyPrefix() {
		return "artists";
	}

	/**
	 * Called when selected item from menu (sort changed)
	 */
	@Override
	public void onSortChanged() {
		mCursorLoader.reset();
		mCursorLoader.setSortOrder(mOrderKey + mOrderAscDesc);
		mCursorLoader.startLoading();
	}

	@Override
	public void onCreateView() {
		super.onCreateView();
		mFragment.initList(Constants.ARTISTS_PROJECTOR);
	}

	@Override
	public void onStart() {
		super.onStart();
		mCursorLoader.startLoading();
	}

	@Override
	public void onStop() {
		super.onStop();
		mCursorLoader.reset();
	}

	@Override
	public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
		mFragment.updateList(data);
	}


	/**
	 * When user clicked on item in list
	 *
	 * @param holder
	 * @param position
	 * @param viewType
	 */
	@Override
	public void onRecyclerViewItemClick(ClickableRecyclerAdapter.ViewHolder holder, int position, int viewType) {
		ArtistsListAdapter adapter = mFragment.getListAdapter();
		long artistId = adapter.getItemId(position);

		Intent intent = new Intent(getBaseActivity(), ArtistDetailActivity.class);
		intent.putExtra(ArtistDetailActivity.EXTRA_ARTIST_ID, artistId);
		mFragment.getActivity().startActivity(intent);
	}
}
