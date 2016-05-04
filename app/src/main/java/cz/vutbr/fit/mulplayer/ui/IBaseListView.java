package cz.vutbr.fit.mulplayer.ui;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public interface IBaseListView<AdapterType extends RecyclerView.Adapter> extends IBaseView {
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

	/**
	 * Returns adapter for getting data
	 * @return recyclerview adapter with specific type
	 */
	AdapterType getListAdapter();
}
