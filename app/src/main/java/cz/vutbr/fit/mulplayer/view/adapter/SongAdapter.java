package cz.vutbr.fit.mulplayer.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * @author mlyko
 * @since 08.04.2016
 */
public class SongAdapter extends RecyclerViewSelectableAdapter<RecyclerView.ViewHolder> {
	public SongAdapter(Context context) {
		super(context);
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return null;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

	}

	@Override
	public int getItemCount() {
		return 0;
	}
}
