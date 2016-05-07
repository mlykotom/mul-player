package cz.vutbr.fit.mulplayer.ui.albums_list;

import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;

import cz.vutbr.fit.mulplayer.Constants;
import cz.vutbr.fit.mulplayer.adapter.AlbumsListAdapter;
import cz.vutbr.fit.mulplayer.adapter.base.ClickableRecyclerAdapter;
import cz.vutbr.fit.mulplayer.ui.IBaseListView;
import cz.vutbr.fit.mulplayer.ui.SortableListPresenter;
import cz.vutbr.fit.mulplayer.ui.album.AlbumActivity;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class AlbumsListPresenter extends SortableListPresenter implements Loader.OnLoadCompleteListener<Cursor>, ClickableRecyclerAdapter.OnItemClickListener {
	private static final int LOADER_ALBUMS_MUSIC = 0;

	IBaseListView<AlbumsListAdapter> mFragment;
	CursorLoader mCursorLoader;

	public AlbumsListPresenter(AlbumsListFragment fragment) {
		super(fragment);
		mFragment = fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mCursorLoader = new CursorLoader(
				getBaseActivity(),
				MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
				Constants.ALBUMS_PROJECTOR,
				null,
				null,
				mOrderKey + mOrderAscDesc
		);
		mCursorLoader.registerListener(LOADER_ALBUMS_MUSIC, this);
	}

	@Override
	public void onStart() {
		super.onStart();
		mCursorLoader.startLoading();
	}

	@Override
	public void onCreateView() {
		super.onCreateView();
		mFragment.initList(Constants.ALBUMS_PROJECTOR);
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
	 * Setups which ordering will be default (must be one from mOrderMap!)
	 *
	 * @return string for CursorLoader ordering
	 */
	@Override
	public String getDefaultOrderKey() {
		return MediaStore.Audio.Albums.ALBUM_KEY;
	}

	/**
	 * @return Prefix for actual fragment so that many ordering keys may be set in one preference file
	 */
	@Override
	public String getKeyPrefix() {
		return "albums";
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

	/**
	 * When user clicked on item in list
	 *
	 * @param holder
	 * @param position
	 * @param viewType
	 */
	@Override
	public void onRecyclerViewItemClick(ClickableRecyclerAdapter.ViewHolder holder, int position, int viewType) {
		Intent intent = new Intent(mFragment.getActivity(), AlbumActivity.class);

		AlbumsListAdapter adapter = mFragment.getListAdapter();
		long albumId = adapter.getItemId(position);

		intent.putExtra(AlbumActivity.EXTRA_ALBUM_ID, albumId);
		mFragment.getActivity().startActivity(intent);
	}
}
