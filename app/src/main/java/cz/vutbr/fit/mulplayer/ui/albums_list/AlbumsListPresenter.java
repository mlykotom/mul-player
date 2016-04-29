package cz.vutbr.fit.mulplayer.ui.albums_list;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import cz.vutbr.fit.mulplayer.Constants;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.adapter.AlbumsListAdapter;
import cz.vutbr.fit.mulplayer.ui.BaseFragmentPresenter;
import cz.vutbr.fit.mulplayer.ui.IBaseListView;
import cz.vutbr.fit.mulplayer.ui.album.AlbumActivity;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class AlbumsListPresenter extends BaseFragmentPresenter implements Loader.OnLoadCompleteListener<Cursor> {
	private static final int LOADER_ALBUMS_MUSIC = 0;

	public static final String PREF_ORDER_KEY = "albums_order";
	public static final String PREF_ORDER_KEY_ASC_DESC = "albums_order_asc_desc";

	IBaseListView<AlbumsListAdapter> mFragment;
	CursorLoader mCursorLoader;
	SharedPreferences mPreferences;

	String mOrderKey;
	private String mOrderAscDesc;

	public AlbumsListPresenter(AlbumsListFragment fragment) {
		mFragment = fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPreferences = mFragment.getActivity().getPreferences(Context.MODE_PRIVATE);
		mOrderKey = mPreferences.getString(PREF_ORDER_KEY, MediaStore.Audio.Albums.ALBUM_KEY);
		mOrderAscDesc = mPreferences.getString(PREF_ORDER_KEY_ASC_DESC, Constants.DB_ORDER_ASC);

		mCursorLoader = new CursorLoader(mFragment.getActivity());
		mCursorLoader.setUri(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI);
		mCursorLoader.setProjection(Constants.ALBUMS_PROJECTOR);
		mCursorLoader.setSortOrder(mOrderKey + mOrderAscDesc);
		mCursorLoader.registerListener(LOADER_ALBUMS_MUSIC, this);
		mCursorLoader.startLoading();
	}

	@Override
	public void onCreateView() {
		super.onCreateView();
		mFragment.initList(Constants.ALBUMS_PROJECTOR);
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
		Intent intent = new Intent(mFragment.getActivity(), AlbumActivity.class);

		AlbumsListAdapter adapter = mFragment.getSongsListAdapter();
//		Cursor cursor = adapter.getCursor();
		long albumId = adapter.getItemId(position);

		intent.putExtra(AlbumActivity.EXTRA_ALBUM_ID, albumId);
		mFragment.getActivity().startActivity(intent);
	}

	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		switch (mOrderKey) {
			case MediaStore.Audio.Albums.NUMBER_OF_SONGS:
				menu.findItem(R.id.sort_album_songs_number).setChecked(true);
				break;

			case MediaStore.Audio.Albums.ALBUM_KEY:
				menu.findItem(R.id.sort_album_name).setChecked(true);
				break;

			case MediaStore.Audio.Albums.FIRST_YEAR:
				menu.findItem(R.id.sort_album_year).setChecked(true);
				break;
		}

		menu.findItem(R.id.sort_asc_desc).setChecked(mOrderAscDesc.equals(Constants.DB_ORDER_ASC));
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.sort_album_songs_number:
				mOrderKey = MediaStore.Audio.Albums.NUMBER_OF_SONGS;
				break;

			case R.id.sort_album_name:
				mOrderKey = MediaStore.Audio.Albums.ALBUM_KEY;
				break;

			case R.id.sort_album_year:
				mOrderKey = MediaStore.Audio.Albums.FIRST_YEAR;
				break;

			case R.id.sort_asc_desc:
				mOrderAscDesc = mOrderAscDesc.equals(Constants.DB_ORDER_ASC) ? Constants.DB_ORDER_DESC : Constants.DB_ORDER_ASC;
				break;

			default:
				return false;
		}

		item.setChecked(!item.isChecked());

		mPreferences.edit()
				.putString(PREF_ORDER_KEY, mOrderKey)
				.putString(PREF_ORDER_KEY_ASC_DESC, mOrderAscDesc)
				.apply();

		mCursorLoader.setSortOrder(mOrderKey + mOrderAscDesc);
		mCursorLoader.reset();
		mCursorLoader.startLoading();

		return true;
	}
}
