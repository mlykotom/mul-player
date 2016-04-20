package cz.vutbr.fit.mulplayer.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.adapter.base.ClickableRecyclerAdapter;
import cz.vutbr.fit.mulplayer.adapter.base.CursorRecyclerAdapter;
import cz.vutbr.fit.mulplayer.utils.Utils;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class SongsListAdapter extends CursorRecyclerAdapter<ClickableRecyclerAdapter.ViewHolder> {
	protected int[] mFrom;
	protected String[] mOriginalFrom;
	protected Context mContext;
	protected ClickableRecyclerAdapter.OnItemClickListener mOnItemClickListener;

	public SongsListAdapter(Context context, Cursor c, String[] from, ClickableRecyclerAdapter.OnItemClickListener itemClickListener) {
		super(c);
		mContext = context;
		mOriginalFrom = from;
		mOnItemClickListener = itemClickListener;
		findColumns(c, from);
	}

	@Override
	public void onBindViewHolder(final ClickableRecyclerAdapter.ViewHolder viewHolder, Cursor cursor) {
		final int[] from = mFrom;

		// we know its only song adapter
		SongViewHolder holder = (SongViewHolder) viewHolder;

		// title
		String title = cursor.getString(from[2]);
		holder.mTitle.setText(title);
		// album
		String album = cursor.getString(from[6]);
		holder.mAlbum.setText(album);
		// artist
		String artist = cursor.getString(from[1]);
		holder.mArtist.setText(artist);
		// duration
		int duration = cursor.getInt(from[4]);
		holder.mDuration.setText(String.format("%s", Utils.formatTime(duration)));
	}

	@Override
	public ClickableRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_song, parent, false);
		SongViewHolder vh = new SongViewHolder(v);
		vh.setOnItemClickListener(mOnItemClickListener);
		return vh;
	}


	/**
	 * Create a map from an array of strings to an array of column-id integers in cursor c.
	 * If c is null, the array will be discarded.
	 *
	 * @param c    the cursor to find the columns from
	 * @param from the Strings naming the columns of interest
	 */
	protected void findColumns(Cursor c, String[] from) {
		if (c != null) {
			int i;
			int count = from.length;
			if (mFrom == null || mFrom.length != count) {
				mFrom = new int[count];
			}
			for (i = 0; i < count; i++) {
				mFrom[i] = c.getColumnIndexOrThrow(from[i]);
			}
		} else {
			mFrom = null;
		}
	}

	@Override
	public Cursor swapCursor(Cursor c) {
		findColumns(c, mOriginalFrom);
		return super.swapCursor(c);
	}

	/**
	 * Album viewholder
	 */
	static class SongViewHolder extends ClickableRecyclerAdapter.ViewHolder {
		@Bind(R.id.item_title)
		public TextView mTitle;
		@Bind(R.id.item_subtitle)
		public TextView mArtist;
		@Bind(R.id.item_album)
		public TextView mAlbum;
		@Bind(R.id.item_additional)
		public TextView mDuration;

		public SongViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
