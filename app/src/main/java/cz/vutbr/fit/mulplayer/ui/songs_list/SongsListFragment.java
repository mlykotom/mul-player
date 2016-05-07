package cz.vutbr.fit.mulplayer.ui.songs_list;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.adapter.SongsListAdapter;
import cz.vutbr.fit.mulplayer.ui.BaseFragment;
import cz.vutbr.fit.mulplayer.ui.IMenuGetter;
import cz.vutbr.fit.mulplayer.utils.SimpleDividerItemDecoration;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class SongsListFragment extends BaseFragment implements ISongsListView, IMenuGetter {
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
}
