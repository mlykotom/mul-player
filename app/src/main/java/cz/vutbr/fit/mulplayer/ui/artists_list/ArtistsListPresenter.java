package cz.vutbr.fit.mulplayer.ui.artists_list;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

import cz.vutbr.fit.mulplayer.Constants;
import cz.vutbr.fit.mulplayer.adapter.base.ClickableRecyclerAdapter;
import cz.vutbr.fit.mulplayer.ui.BaseFragmentPresenter;

/**
 * @author mlyko
 * @since 16.04.2016
 */
public class ArtistsListPresenter extends BaseFragmentPresenter implements Loader.OnLoadCompleteListener<Cursor>, ClickableRecyclerAdapter.OnItemClickListener {
	private static final int LOADER_ARTISTS_MUSIC = 0;

	IArtistsListView mFragment;
	CursorLoader mCursorLoader;

	String mOrderKey = MediaStore.Audio.Artists.ARTIST_KEY;

	public ArtistsListPresenter(ArtistsListFragment fragment) {
		super(fragment);
		mFragment = fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCursorLoader = new CursorLoader(
				mFragment.getActivity(),
				MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
				Constants.ARTISTS_PROJECTOR,
				null,
				null,
				mOrderKey
		);

		mCursorLoader.registerListener(LOADER_ARTISTS_MUSIC, this);
	}

	@Override
	public void onCreateView() {
		super.onCreateView();
		mFragment.initList(Constants.ARTISTS_PROJECTOR);
	}

	@Override
	public void onStart() {
		super.onStart();
		mCursorLoader.startLoading();
	}

	@Override
	public void onStop() {
		super.onStop();
		mCursorLoader.reset();
	}


	@Override
	public void onLoadComplete(Loader<Cursor> loader, Cursor data) {
		mFragment.updateList(data);
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
		Toast.makeText(getBaseActivity(), "TODO", Toast.LENGTH_LONG).show();
	}
}
