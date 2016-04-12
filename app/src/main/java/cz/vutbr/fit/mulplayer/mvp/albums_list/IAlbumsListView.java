package cz.vutbr.fit.mulplayer.mvp.albums_list;

import android.database.Cursor;

import cz.vutbr.fit.mulplayer.mvp.IBaseView;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public interface IAlbumsListView extends IBaseView {
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
