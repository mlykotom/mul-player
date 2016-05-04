package cz.vutbr.fit.mulplayer.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.adapter.base.ClickableRecyclerAdapter;
import cz.vutbr.fit.mulplayer.utils.Utils;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class ArtistsListAdapter extends SongsListAdapter {
	private Context mContext;

	public ArtistsListAdapter(Context context, String[] from) {
		super(context, from);
		mContext = context;
	}

	@Override
	public void onBindViewHolder(ClickableRecyclerAdapter.ViewHolder viewHolder, Cursor cursor) {
		final int[] from = mFrom;

		// we know its only song adapter
		ArtistViewHolder holder = (ArtistViewHolder) viewHolder;

		// artist
		String artist = cursor.getString(from[1]);

		holder.mIcon.setText(Utils.getFirstLetters(artist, 2)); // TODO somehow optimize?

		holder.mAlbum.setText(artist);

		// albums | song count
		int albumCount = cursor.getInt(from[2]);
		int songCount = cursor.getInt(from[3]);
		String albumQuantityString = mContext.getResources().getQuantityString(R.plurals.albums_count, albumCount, albumCount);
		String songQuantityString = mContext.getResources().getQuantityString(R.plurals.songs_count, songCount, songCount);
		holder.mArtist.setText(String.format("%s | %s", albumQuantityString, songQuantityString));
	}

	@Override
	public ClickableRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = mInflater.inflate(R.layout.item_list_artist, parent, false);
		ArtistViewHolder vh = new ArtistViewHolder(v);
		vh.setOnItemClickListener(mOnItemClickListener);
		vh.setOnLongItemClickListener(mOnLongItemClickListener);
		return vh;
	}

	/**
	 * Album viewholder
	 */
	public static class ArtistViewHolder extends ClickableRecyclerAdapter.ViewHolder {
		@Bind(R.id.item_icon)
		public TextView mIcon;
		@Bind(R.id.item_title)
		public TextView mAlbum;
		@Bind(R.id.item_subtitle)
		public TextView mArtist;

		public ArtistViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
