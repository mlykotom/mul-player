package cz.vutbr.fit.mulplayer.mvp.albums_list;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.mvp.CursorRecyclerAdapter;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class AlbumsListAdapter extends CursorRecyclerAdapter<AlbumsListAdapter.ArtistViewHolder> {
	private int[] mFrom;
	private String[] mOriginalFrom;
	private Context mContext;
	private Picasso mPicasso;

	public AlbumsListAdapter(Context context, Cursor c, String[] from) {
		super(c);
		mContext = context;
		mOriginalFrom = from;
		mPicasso = Picasso.with(mContext);
		findColumns(c, from);
	}

	@Override
	public void onBindViewHolder(final ArtistViewHolder holder, Cursor cursor) {
		final int[] from = mFrom;

		String artPath = cursor.getString(from[1]);
		if (artPath != null) {
			mPicasso.load(new File(artPath)).into(holder.mIcon);
		}

		String artist = cursor.getString(from[2]);
		holder.mTitle.setText(artist);
	}

	@Override
	public ArtistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_artist, parent, false);
		return new ArtistViewHolder(v);
	}


	/**
	 * Create a map from an array of strings to an array of column-id integers in cursor c.
	 * If c is null, the array will be discarded.
	 *
	 * @param c    the cursor to find the columns from
	 * @param from the Strings naming the columns of interest
	 */
	private void findColumns(Cursor c, String[] from) {
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
	 * Artist viewholder
	 */
	class ArtistViewHolder extends RecyclerView.ViewHolder {
		@Bind(R.id.item_icon)
		public ImageView mIcon;
		@Bind(R.id.item_title)
		public TextView mTitle;
		@Bind(R.id.item_subtitle)
		public TextView mSubTitle;

		public ArtistViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
