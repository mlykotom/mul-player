package cz.vutbr.fit.mulplayer.ui.artists_list;

import android.database.Cursor;

import cz.vutbr.fit.mulplayer.ui.IBaseView;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public interface IArtistsListView extends IBaseView {
	/**
	 * Initializes adapter and RecyclerView for showing data
	 * @param projection
	 */
	void initList(String[] projection);

	/**
	 * Updates list with refreshed data (swaps the cursor)
	 * @param data
	 */
	void updateList(Cursor data);
}
