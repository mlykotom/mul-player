package cz.vutbr.fit.mulplayer.ui.albums_list;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;

import cz.vutbr.fit.mulplayer.Constants;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.ui.BaseFragmentPresenter;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class AlbumsListPresenter extends BaseFragmentPresenter implements Loader.OnLoadCompleteListener<Cursor> {
	private static final int LOADER_ALBUMS_MUSIC = 0;

	AlbumsListFragment mFragment;
	CursorLoader mCursorLoader;
	SharedPreferences mPreferences;

	String mOrderKey = MediaStore.Audio.Albums.ALBUM_KEY;
	private String mOrderAscDesc = Constants.DB_ORDER_ASC;

	public AlbumsListPresenter(AlbumsListFragment fragment) {
		mFragment = fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mPreferences = mFragment.getPreferences(Context.MODE_PRIVATE);

		mCursorLoader = new CursorLoader(mFragment.getActivity());
		mCursorLoader.setUri(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI);
		mCursorLoader.setProjection(Constants.ALBUMS_PROJECTOR);
		mCursorLoader.setSortOrder(mOrderKey + Constants.DB_ORDER_ASC);
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

	}

	public boolean menuItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.sort_album_name:
				if (item.isChecked()) item.setChecked(false);
				else item.setChecked(true);
				return true;

			case R.id.sort_artist_name:
				if (item.isChecked()) item.setChecked(false);
				else item.setChecked(true);
				return true;

			default:
				return false;
		}
	}


	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.sort_artist_name:
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

		mCursorLoader.setSortOrder(mOrderKey + mOrderAscDesc);
		mCursorLoader.reset();
		mCursorLoader.startLoading();

		return true;
	}
}
