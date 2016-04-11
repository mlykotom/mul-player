package cz.vutbr.fit.mulplayer.mvp.songs_list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.entity.Song;
import cz.vutbr.fit.mulplayer.view.adapter.RecyclerViewSelectableAdapter;

/**
 * @author mlyko
 * @since 08.04.2016
 */
public class SongsListAdapter extends RecyclerViewSelectableAdapter<RecyclerView.ViewHolder> {
	private List<Song> mObjects = new ArrayList<>();
	private IItemClickListener mClickListener;

	public SongsListAdapter(Context context) {
		super(context);
//		mClickListener =
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View v = inflater.inflate(R.layout.item_list_song, parent, false);
		return new SongViewHolder(v, mClickListener);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		Song song = (Song) getItem(position);
		if (song == null) return; // TODO should show error view or loading or sth

		SongViewHolder viewHolder = (SongViewHolder) holder;
		viewHolder.mTitle.setText(song.artist);
		viewHolder.mSubTitle.setText(song.title);
	}

	@Override
	public int getItemCount() {
		return mObjects.size();
	}

	public Object getItem(int position) {
		return mObjects.get(position);
	}

	/**
	 * Updates data with new & tries to animate
	 *
	 * @param songList
	 */
	public void updateData(List<Song> songList) {
		mObjects = songList;
		notifyDataSetChanged();
		// TODO this should notify whole list
		notifyItemRangeChanged(0, mObjects.size());
	}

	public class SongViewHolder extends SelectableViewHolder implements View.OnClickListener, View.OnLongClickListener {
		@Bind(R.id.item_icon)
		public ImageView mIcon;
		@Bind(R.id.item_title)
		public TextView mTitle;
		@Bind(R.id.item_subtitle)
		public TextView mSubTitle;

		public IItemClickListener mListener;

		public SongViewHolder(View itemView, IItemClickListener listener) {
			super(itemView);
			ButterKnife.bind(this, itemView);
			mListener = listener;

			itemView.setOnClickListener(this);
			itemView.setOnLongClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if (mListener != null) {
				mListener.onRecyclerViewItemClick(getAdapterPosition(), getItemViewType());
			}
		}

		@Override
		public boolean onLongClick(View v) {
			if (mListener != null && mListener.onRecyclerViewItemLongClick(getAdapterPosition(), getItemViewType())) {
				v.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
				return true;
			}
			return false;
		}
	}

}
