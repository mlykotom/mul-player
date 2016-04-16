package cz.vutbr.fit.mulplayer.ui.artists_list;

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
import cz.vutbr.fit.mulplayer.ui.songs_list.SongsListAdapter;
import cz.vutbr.fit.mulplayer.utils.CircleTransform;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class ArtistsListAdapter extends SongsListAdapter {
	private Picasso mPicasso;
	private Transformation mCircleTransform = new CircleTransform();

	public ArtistsListAdapter(Context context, Cursor c, String[] from, ClickableRecyclerAdapter.OnItemClickListener itemClickListener) {
		super(context, c, from, itemClickListener);
		mPicasso = Picasso.with(mContext);
	}

	@Override
	public void onBindViewHolder(ClickableRecyclerAdapter.ViewHolder viewHolder, Cursor cursor) {
		final int[] from = mFrom;

		// we know its only song adapter
		ArtistViewHolder holder = (ArtistViewHolder) viewHolder;

//		String artPath = cursor.getString(from[1]);
//
//		if (artPath != null) {
//			if (holder.mArtworkFile == null) {
//				holder.mArtworkFile = new File(artPath); // TODO should check if artPath hasn't changed
//			}
//
//			mPicasso.load(holder.mArtworkFile).transform(mCircleTransform).into(holder.mIcon);
//		} else {
////			holder.mIcon.setImageResource(); // TODO "no album image resource"
//		}

		// album
		String album = cursor.getString(from[1]);
		holder.mAlbum.setText(album);


		// albums | song count
		int albumCount = cursor.getInt(from[2]);
		int songCount = cursor.getInt(from[3]);
		String quantityString = mContext.getResources().getQuantityString(R.plurals.albums_songs, songCount, songCount);
		holder.mArtist.setText(String.format("%s | %s", albumCount, quantityString));
	}

	@Override
	public ClickableRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_album, parent, false);
		ArtistViewHolder vh = new ArtistViewHolder(v);
		vh.setOnItemClickListener(mOnItemClickListener);
		return vh;
	}

	/**
	 * Album viewholder
	 */
	class ArtistViewHolder extends ClickableRecyclerAdapter.ViewHolder {
		@Bind(R.id.item_icon)
		public ImageView mIcon;
		@Bind(R.id.item_title)
		public TextView mAlbum;
		@Bind(R.id.item_subtitle)
		public TextView mArtist;

		public File mArtworkFile;

		public ArtistViewHolder(View itemView) {
			super(itemView);
			ButterKnife.bind(this, itemView);
		}
	}
}
