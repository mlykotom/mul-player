package cz.vutbr.fit.mulplayer.ui.albums_list;

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
import cz.vutbr.fit.mulplayer.adapter.AlbumsListAdapter;
import cz.vutbr.fit.mulplayer.ui.IBaseListView;
import cz.vutbr.fit.mulplayer.ui.BaseFragment;
import cz.vutbr.fit.mulplayer.ui.IMenuGetter;
import cz.vutbr.fit.mulplayer.utils.SimpleDividerItemDecoration;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class AlbumsListFragment extends BaseFragment implements IBaseListView<AlbumsListAdapter>, IMenuGetter {

	public AlbumsListPresenter mPresenter;
	@Bind(R.id.albums_list) RecyclerView mAlbumsList;
	public AlbumsListAdapter mAlbumsListAdapter;

	/**
	 * Constructor for fragment which can't be constructed classical way because android manages its lifecycle
	 *
	 * @return instance of this fragment
	 */
	public static AlbumsListFragment newInstance() {
		Bundle args = new Bundle();
		AlbumsListFragment fragment = new AlbumsListFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		mBasePresenter = mPresenter = new AlbumsListPresenter(this);
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	/**
	 * Menu getter so that when player overlays toolbar, we can reinflate it
	 *
	 * @return menu resource
	 */
	public int getMenuResource() {
		return R.menu.menu_list_albums;
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

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_albums_list, container, false);
		ButterKnife.bind(this, view);
		mPresenter.onCreateView();
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.unbind(this);
	}

	// ------ UI setters (presenter -> ui) ------ //

	public void initList(String[] projection) {
		mAlbumsListAdapter = new AlbumsListAdapter(getActivity(), projection);
		mAlbumsListAdapter.setOnItemClickListener(mPresenter);
		mAlbumsList.setLayoutManager(new LinearLayoutManager(getActivity()));
		mAlbumsList.setItemAnimator(new DefaultItemAnimator());
		mAlbumsList.setAdapter(mAlbumsListAdapter);
		mAlbumsList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
	}

	public void updateList(Cursor data) {
		mAlbumsListAdapter.changeCursor(data);
	}

	@Override
	public AlbumsListAdapter getListAdapter() {
		return mAlbumsListAdapter;
	}
}
