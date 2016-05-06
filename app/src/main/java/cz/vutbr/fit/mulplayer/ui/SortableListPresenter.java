package cz.vutbr.fit.mulplayer.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.HashMap;

import cz.vutbr.fit.mulplayer.Constants;
import cz.vutbr.fit.mulplayer.R;

/**
 * @author mlyko
 * @since 07.05.2016
 */
public abstract class SortableListPresenter extends BaseFragmentPresenter {
	private static final String PREF_ORDER_KEY = "%s_order";
	private static final String PREF_ORDER_KEY_ASC_DESC = "%s_order_asc_desc";

	protected String mOrderKey;
	protected String mOrderAscDesc;
	protected static HashMap<Integer, String> mOrderMap = new HashMap<>();
	protected SharedPreferences mPreferences;

	static {
		// albums list
		mOrderMap.put(R.id.sort_album_name, MediaStore.Audio.Albums.ALBUM_KEY);
		mOrderMap.put(R.id.sort_album_year, MediaStore.Audio.Albums.FIRST_YEAR);
		mOrderMap.put(R.id.sort_album_songs_number, MediaStore.Audio.Albums.NUMBER_OF_SONGS);
		// songs list
		mOrderMap.put(R.id.sort_song_name, MediaStore.Audio.Media.TITLE_KEY);
		mOrderMap.put(R.id.sort_artist_name, MediaStore.Audio.Media.ARTIST_KEY);
		mOrderMap.put(R.id.sort_album_name, MediaStore.Audio.Media.ALBUM_KEY);
	}

	public SortableListPresenter(BaseFragment fragment) {
		super(fragment);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPreferences = getBaseActivity().getPreferences(Context.MODE_PRIVATE);

		mOrderKey = mPreferences.getString(getPreferenceKey(PREF_ORDER_KEY), getDefaultOrderKey());
		mOrderAscDesc = mPreferences.getString(getPreferenceKey(PREF_ORDER_KEY_ASC_DESC), Constants.DB_ORDER_ASC);
	}

	/**
	 * Classic handler for creating options menu (like fragment/activity)
	 * This must be called from activity/fragment
	 *
	 * @param menu     which is inflating
	 * @param inflater
	 */
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// simple iteration for checking which item is selected
		for (Integer itemId : mOrderMap.keySet()) {
			if (mOrderMap.get(itemId).equals(mOrderKey)) {
				menu.findItem(itemId).setChecked(true);
				return;
			}
		}
		// set checked ascending/descending
		menu.findItem(R.id.sort_asc_desc).setChecked(mOrderAscDesc.equals(Constants.DB_ORDER_ASC));
	}

	/**
	 * Classic handler for menu item click
	 *
	 * @param item which was clicked
	 * @return if handler was consumed
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.sort_asc_desc) {
			mOrderAscDesc = mOrderAscDesc.equals(Constants.DB_ORDER_ASC) ? Constants.DB_ORDER_DESC : Constants.DB_ORDER_ASC;
		} else {
			String orderKey = mOrderMap.get(item.getItemId());
			if (orderKey == null) return false;
			mOrderKey = orderKey;
		}

		item.setChecked(!item.isChecked());

		mPreferences.edit()
				.putString(getPreferenceKey(PREF_ORDER_KEY), mOrderKey)
				.putString(getPreferenceKey(PREF_ORDER_KEY_ASC_DESC), mOrderAscDesc)
				.apply();

		onSortChanged();
		return true;
	}

	/**
	 * @param prefKey pref key with %s as prefix!
	 * @return Returns formatted preference key
	 */
	private String getPreferenceKey(String prefKey) {
		return String.format(prefKey, getKeyPrefix());
	}

	/**
	 * Setups which ordering will be default (must be one from mOrderMap!)
	 *
	 * @return string for CursorLoader ordering
	 */
	public abstract String getDefaultOrderKey();

	/**
	 * @return Prefix for actual fragment so that many ordering keys may be set in one preference file
	 */
	public abstract String getKeyPrefix();

	/**
	 * Called when selected item from menu (sort changed)
	 */
	public abstract void onSortChanged();

}
