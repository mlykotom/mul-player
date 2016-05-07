package cz.vutbr.fit.mulplayer.ui.dialogs;

/**
 * @author mlyko
 * @since 07.05.2016
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avast.android.dialogs.core.BaseDialogFragment;
import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.INegativeButtonDialogListener;

import cz.vutbr.fit.mulplayer.R;

/**
 * @author mlyko
 * @since 27.04.2016
 */
public class SongDetailDialog extends SimpleDialogFragment {
	private static final String ARG_SONG_ID = "song_id";
	private static final String ARG_SONG_TITLE = "song_title";
	private static final String ARG_SONG_ARTIST = "song_artist";
	private static final String ARG_SONG_ALBUM = "song_album";

	private static final String TAG = SongDetailDialog.class.getSimpleName();
	private long mSongId;
	protected View mParentView;
	private TextInputLayout mSongNameView;
	private TextInputLayout mSongArtistView;
	private TextInputLayout mSongAlbumView;

	public static SongDetailDialogBuilder build(Context context, FragmentManager fragmentManager) {
		SongDetailDialogBuilder builder = new SongDetailDialogBuilder(context, fragmentManager);

		builder
				.setTitle(R.string.songs_detail_title)
				.setPositiveButtonText(R.string.dialog_edit)
				.setNegativeButtonText(R.string.dialog_cancel);

		return builder;
	}

	public long getSongId() {
		return mSongId;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSongId = this.getArguments().getLong(ARG_SONG_ID);
	}

	/**
	 * Key method for using {@link BaseDialogFragment}.
	 * Customized dialogs need to be set up via provided builder.
	 *
	 * @param builder Provided builder for setting up customized dialog
	 * @return Updated builder
	 */
	@SuppressLint("InflateParams")
	@Override
	protected Builder build(Builder builder) {
		mParentView = builder.getLayoutInflater().inflate(R.layout.dialog_song_detail, null, false);
		builder.setView(mParentView);

		final CharSequence title = getTitle();
		if (!TextUtils.isEmpty(title)) {
			builder.setTitle(title);
		}

		// positive button
		final CharSequence positiveButtonText = getPositiveButtonText();
		// only shown when user is author
		if (!TextUtils.isEmpty(positiveButtonText)) {
			builder.setPositiveButton(positiveButtonText, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					for (IPositiveButtonDialogListener listener : getDialogListeners(IPositiveButtonDialogListener.class)) {
						listener.onPositiveButtonClicked(mRequestCode, mParentView, SongDetailDialog.this);
					}
					// dismiss should be called inside of listener
				}
			});
		}

		// negative button
		final CharSequence negativeButtonText = getNegativeButtonText();
		if (!TextUtils.isEmpty(negativeButtonText)) {
			builder.setNegativeButton(negativeButtonText, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					for (INegativeButtonDialogListener listener : getNegativeButtonDialogListeners()) {
						listener.onNegativeButtonClicked(mRequestCode);
					}
					dismiss();
				}
			});
		}

		// delete button
		final CharSequence neutralButtonText = getNeutralButtonText();
		if (!TextUtils.isEmpty(neutralButtonText)) {
			builder.setNeutralButton(neutralButtonText, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					for (IDeleteButtonDialogListener listener : getDialogListeners(IDeleteButtonDialogListener.class)) {
						listener.onDeleteButtonClicked(mRequestCode, mParentView, SongDetailDialog.this);
					}
					dismiss();
				}
			});
		}

		// song name
		mSongNameView = (TextInputLayout) mParentView.findViewById(R.id.song_detail_name);

		EditText songNameEditText = mSongNameView.getEditText();
		if (songNameEditText != null) {
			songNameEditText.setText(this.getArguments().getString(ARG_SONG_TITLE));
		} else {
			Log.e(TAG, "EditText in TextInputLayout missing!");
		}

		// song artist
		mSongArtistView = (TextInputLayout) mParentView.findViewById(R.id.song_detail_artist);

		EditText songArtistEditText = mSongArtistView.getEditText();
		if (songArtistEditText != null) {
			songArtistEditText.setText(this.getArguments().getString(ARG_SONG_ARTIST));
		} else {
			Log.e(TAG, "EditText in TextInputLayout missing!");
		}


		// song album
		mSongAlbumView = (TextInputLayout) mParentView.findViewById(R.id.song_detail_album);

		EditText songAlbumEditText = mSongAlbumView.getEditText();
		if (songAlbumEditText != null) {
			songAlbumEditText.setText(this.getArguments().getString(ARG_SONG_ALBUM));
		} else {
			Log.e(TAG, "EditText in TextInputLayout missing!");
		}


		return builder;
	}

	/**
	 * Changes colors of dialog's buttons.
	 * Because builder does not have access to whole view, we have to change colors here
	 *
	 * @param savedInstanceState
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// setup delete button as red
		Button neutralButton = (Button) getDialog().findViewById(R.id.sdl_button_neutral);
		if (neutralButton != null) {
			neutralButton.setTextColor(getResources().getColor(R.color.red));
		}

		Button negativeButton = (Button) getDialog().findViewById(R.id.sdl_button_negative);
		if (negativeButton != null) {
			negativeButton.setTextColor(getResources().getColor(R.color.gray));
		}
	}

	public static class SongDetailDialogBuilder extends SimpleDialogFragment.SimpleDialogBuilder {
		private long mSongId;
		private String mSongTitle;
		private String mSongArtist;
		private String mSongAlbum;

		public SongDetailDialogBuilder(Context context, FragmentManager fragmentManager) {
			super(context, fragmentManager, SongDetailDialog.class);
		}

		@Override
		protected SongDetailDialogBuilder self() {
			return this;
		}

		public SongDetailDialogBuilder setSongId(long songId) {
			mSongId = songId;
			return this;
		}

		public SongDetailDialogBuilder setSongTitle(String songTitle) {
			mSongTitle = songTitle;
			return this;
		}

		public SongDetailDialogBuilder setSongArtist(String songArtist) {
			mSongArtist = songArtist;
			return this;
		}

		public SongDetailDialogBuilder setSongAlbum(String songAlbum) {
			mSongAlbum = songAlbum;
			return this;
		}

		@Override
		protected Bundle prepareArguments() {
			Bundle args = super.prepareArguments();
			args.putLong(ARG_SONG_ID, mSongId);
			args.putString(ARG_SONG_TITLE, mSongTitle);
			args.putString(ARG_SONG_ARTIST, mSongArtist);
			args.putString(ARG_SONG_ALBUM, mSongAlbum);
			return args;
		}
	}

	public interface IPositiveButtonDialogListener {
		void onPositiveButtonClicked(int requestCode, View view, SongDetailDialog dialog);
	}

	public interface IDeleteButtonDialogListener {
		void onDeleteButtonClicked(int requestCode, View view, SongDetailDialog dialog);
	}
}

