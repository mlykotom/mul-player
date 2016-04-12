package cz.vutbr.fit.mulplayer.mvp.albums_list;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.model.adapter.ClickableRecyclerAdapter;
import cz.vutbr.fit.mulplayer.model.adapter.CursorRecyclerAdapter;
import cz.vutbr.fit.mulplayer.utils.CircleTransform;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class AlbumsListAdapter extends CursorRecyclerAdapter<AlbumsListAdapter.AlbumViewHolder> {
	private int[] mFrom;
	private String[] mOriginalFrom;
	private Context mContext;
	private Picasso mPicasso;
	private Transformation mCircleTransform = new CircleTransform();
	private ClickableRecyclerAdapter.OnItemClickListener mOnItemClickListener;


	public AlbumsListAdapter(Context context, Cursor c, String[] from, ClickableRecyclerAdapter.OnItemClickListener itemClickListener) {
		super(c);
		mContext = context;
		mOriginalFrom = from;
		mOnItemClickListener = itemClickListener;
		mPicasso = Picasso.with(mContext);
		findColumns(c, from);
	}

	@Override
	public void onBindViewHolder(final AlbumViewHolder holder, Cursor cursor) {
		final int[] from = mFrom;

		String artPath = cursor.getString(from[1]);

		if (artPath != null) {
			if (holder.mArtworkFile == null) {
				holder.mArtworkFile = new File(artPath); // TODO should check if artPath hasn't changed
			}

			mPicasso.load(holder.mArtworkFile).transform(mCircleTransform).into(holder.mIcon);
		} else {
//			holder.mIcon.setImageResource(); // TODO "no album image resource"
		}

		// album
		String album = cursor.getString(from[2]);
		holder.mAlbum.setText(album);

		// artist
		String artist = cursor.getString(from[3]);
		holder.mArtist.setText(artist);

		// song count
		int songCount = cursor.getInt(from[4]);
		String quantityString = mContext.getResources().getQuantityString(R.plurals.albums_songs, songCount, songCount);
		holder.mSongCount.setText(quantityString);
	}

	@Override
	public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_album, parent, false);
		AlbumViewHolder vh = new AlbumViewHolder(v);
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
	 * Album viewholder
	 */
	class AlbumViewHolder extends ClickableRecyclerAdapter.ViewHolder {
		@Bind(R.id.item_icon)
		public ImageView mIcon;
		@Bind(R.id.item_title)
		public TextView mAlbum;
		@Bind(R.id.item_subtitle)
		public TextView mArtist;
		@Bind(R.id.item_additional)
		public TextView mSongCount;

		public File mArtworkFile;

		public AlbumViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
