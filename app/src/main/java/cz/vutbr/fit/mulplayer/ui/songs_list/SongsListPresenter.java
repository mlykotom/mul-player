package cz.vutbr.fit.mulplayer.ui.songs_list;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;

import cz.vutbr.fit.mulplayer.Constants;
import cz.vutbr.fit.mulplayer.adapter.SongsListAdapter;
import cz.vutbr.fit.mulplayer.adapter.base.ClickableRecyclerAdapter;
import cz.vutbr.fit.mulplayer.model.MusicService;
import cz.vutbr.fit.mulplayer.ui.BaseActivity;
import cz.vutbr.fit.mulplayer.ui.SortableListPresenter;
import cz.vutbr.fit.mulplayer.ui.dialogs.SongDetailDialog;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class SongsListPresenter extends SortableListPresenter implements Loader.OnLoadCompleteListener<Cursor>, ClickableRecyclerAdapter.OnItemClickListener, ClickableRecyclerAdapter.OnLongItemClickListener {
	private static final int LOADER_SONGS_MUSIC = 0;
	private static final int SONG_DETAIL_CODE = 0;
	private static final String TAG = SongsListPresenter.class.getSimpleName();

	SongsListFragment mFragment;
	CursorLoader mCursorLoader;
	BaseActivity.IPlayerVisibilityControl mPlayerVisibilityCallback;

	public SongsListPresenter(SongsListFragment songsListFragment) {
		super(songsListFragment);
		mFragment = songsListFragment;
	}

	@Override
	public void onAttach() {
		super.onAttach();

		try {
			mPlayerVisibilityCallback = (BaseActivity.IPlayerVisibilityControl) mFragment.getActivity();
		} catch (ClassCastException e) {
			throw new ClassCastException(mFragment.getActivity().toString() + " must implement IPlayerVisibilityControll");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mCursorLoader = new CursorLoader(
				getBaseActivity(),
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				Constants.SONG_PROJECTOR,
				Constants.MUSIC_SELECTOR,
				null,
				mOrderKey + mOrderAscDesc
		);
		mCursorLoader.registerListener(LOADER_SONGS_MUSIC, this);
	}

	@Override
	public String getDefaultOrderKey() {
		return MediaStore.Audio.Media.TITLE_KEY;
	}

	@Override
	public String getKeyPrefix() {
		return "songs";
	}

	@Override
	public void onStart() {
		super.onStart();
		mCursorLoader.startLoading();
	}

	@Override
	public void onCreateView() {
		super.onCreateView();
		mFragment.initList(Constants.SONG_PROJECTOR);
	}


	@Override
	public void onStop() {
		super.onStop();
		mCursorLoader.reset();

	}

	@Override
	public void onLoadComplete(Loader<Cursor> loader, Cursor cursor) {
		mFragment.updateList(cursor);
	}

	/**
	 * When user clicked on item in list
	 *
	 * @param holder
	 * @param position
	 * @param viewType
	 */
	@Override
	public void onRecyclerViewItemClick(ClickableRecyclerAdapter.ViewHolder holder, int position, int viewType) {
		SongsListAdapter adapter = mFragment.getSongsListAdapter();
		MusicService.fireAction(mFragment.getActivity(), MusicService.CMD_PLAY_ALL_SONGS, adapter.getItemId(position));
//		mPlayerVisibilityCallback.showPlayer();
	}

	@Override
	public void onSortChanged() {
		mCursorLoader.reset();
		mCursorLoader.setSortOrder(mOrderKey + mOrderAscDesc);
		mCursorLoader.startLoading();
	}

	@Override
	public boolean onRecyclerViewItemLongClick(ClickableRecyclerAdapter.ViewHolder holder, int position, int viewType) {
		SongsListAdapter adapter = mFragment.getSongsListAdapter();

		Cursor data = adapter.getCursor();
		data.moveToPosition(position);

//		String filePath = data.getString(data.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
//
//		MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//		retriever.setDataSource(filePath);
//
//		String artist = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
//		Toast.makeText(getBaseActivity(), artist, Toast.LENGTH_LONG).show();
//		retriever.release();

		SongDetailDialog
				.build(getBaseActivity(), getBaseActivity().getSupportFragmentManager())
				.setSongId(adapter.getItemId(position))
				.setSongTitle(data.getString(data.getColumnIndex(MediaStore.Audio.Media.TITLE)))
				.setSongArtist(data.getString(data.getColumnIndex(MediaStore.Audio.Media.ARTIST)))
				.setSongAlbum(data.getString(data.getColumnIndex(MediaStore.Audio.Media.ALBUM)))
				.setTargetFragment(mFragment, SONG_DETAIL_CODE)
				.show();

		return true;
	}

	/**
	 * When song detail dialog was submitted with specified values
	 *
	 * @param songId   which will be updated
	 * @param songName new song name
	 */
	public void onSongMetadataChanged(long songId, String songName, String songArtist, String songAlbum) {
		ContentValues values = new ContentValues(6);
		values.put(MediaStore.Audio.Media.TITLE, songName);
		values.put(MediaStore.Audio.Media.TITLE_KEY, MediaStore.Audio.keyFor(songName));
		values.put(MediaStore.Audio.Media.ARTIST, songArtist);
		values.put(MediaStore.Audio.Media.ARTIST_KEY, MediaStore.Audio.keyFor(songArtist));
		values.put(MediaStore.Audio.Media.ALBUM, songAlbum);
		values.put(MediaStore.Audio.Media.ALBUM_KEY, MediaStore.Audio.keyFor(songAlbum));

		int rowsUpdated = getBaseActivity().getContentResolver().update(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				values,
				BaseColumns._ID + " = ?",
				new String[]{String.valueOf(songId)}
		);

		Log.d(TAG, "Updated " + rowsUpdated);

		// TODO scan updated file!
//		getBaseActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, ));
	}
}
