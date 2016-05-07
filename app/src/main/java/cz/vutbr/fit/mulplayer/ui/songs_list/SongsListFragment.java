package cz.vutbr.fit.mulplayer.ui.songs_list;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.adapter.SongsListAdapter;
import cz.vutbr.fit.mulplayer.ui.BaseFragment;
import cz.vutbr.fit.mulplayer.ui.IMenuGetter;
import cz.vutbr.fit.mulplayer.ui.dialogs.SongDetailDialog;
import cz.vutbr.fit.mulplayer.utils.SimpleDividerItemDecoration;
import cz.vutbr.fit.mulplayer.utils.Validator;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class SongsListFragment extends BaseFragment implements ISongsListView, IMenuGetter, SongDetailDialog.IPositiveButtonDialogListener {
	private static final String TAG = SongsListFragment.class.getSimpleName();
	SongsListPresenter mPresenter;
	@Bind(R.id.songs_list) RecyclerView mSongsRecyclerView;
	public SongsListAdapter mSongsListAdapter;

	/**
	 * Constructor for fragment which can't be constructed classical way because android manages its lifecycle
	 *
	 * @return instance of this fragment
	 */
	public static SongsListFragment newInstance() {
		Bundle args = new Bundle();
		SongsListFragment fragment = new SongsListFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onAttach(Context context) {
		mBasePresenter = mPresenter = new SongsListPresenter(this);
		super.onAttach(context);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_songs_list, container, false);
		ButterKnife.bind(this, view);
		mPresenter.onCreateView();
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.unbind(this);
	}

	/**
	 * Menu getter so that when player overlays toolbar, we can reinflate it
	 *
	 * @return menu resource
	 */
	@Override
	public int getMenuResource() {
		return R.menu.menu_list_songs;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(getMenuResource(), menu);
		mPresenter.onCreateOptionsMenu(menu, inflater);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return mPresenter.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
	}

	// ------ UI setters (presenter -> ui) ------ //

	public void initList(String[] projection) {
		mSongsListAdapter = new SongsListAdapter(getActivity(), projection);
		mSongsListAdapter.setOnItemClickListener(mPresenter);
		mSongsListAdapter.setOnLongItemClickListener(mPresenter);

		mSongsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mSongsRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mSongsRecyclerView.setAdapter(mSongsListAdapter);
		mSongsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), SimpleDividerItemDecoration.LINE_WHOLE));
	}

	public void updateList(Cursor data) {
		mSongsListAdapter.changeCursor(data);
	}

	@Override
	public SongsListAdapter getSongsListAdapter() {
		return mSongsListAdapter;
	}

	@Override
	public void onPositiveButtonClicked(int requestCode, View view, SongDetailDialog dialog) {
		TextInputLayout songNameTextLayout = (TextInputLayout) view.findViewById(R.id.song_detail_name);
		TextInputLayout songArtistTextLayout = (TextInputLayout) view.findViewById(R.id.song_detail_artist);
		TextInputLayout songAlbumTextLayout = (TextInputLayout) view.findViewById(R.id.song_detail_album);
		if (!Validator.validate(songNameTextLayout) || !Validator.validate(songArtistTextLayout) || !Validator.validate(songAlbumTextLayout)) {
			return;
		}

		EditText songNameView = songNameTextLayout.getEditText();
		EditText songArtistView = songArtistTextLayout.getEditText();
		EditText songAlbumView = songAlbumTextLayout.getEditText();
		if (songNameView == null || songArtistView == null || songAlbumView == null) {
			Log.e(TAG, "There is none EditText inside TextInputLayout (project name)!");
			return;
		}

		mPresenter.onSongMetadataChanged(
				dialog.getSongId(),
				songNameView.getText().toString().trim(),
				songArtistView.getText().toString().trim(),
				songAlbumView.getText().toString().trim()
		);

		dialog.dismiss();
	}
}
