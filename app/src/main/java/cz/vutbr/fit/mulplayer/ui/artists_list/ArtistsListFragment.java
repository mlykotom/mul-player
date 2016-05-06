package cz.vutbr.fit.mulplayer.ui.artists_list;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.adapter.ArtistsListAdapter;
import cz.vutbr.fit.mulplayer.ui.BaseFragment;
import cz.vutbr.fit.mulplayer.ui.IBaseListView;
import cz.vutbr.fit.mulplayer.ui.IMenuGetter;
import cz.vutbr.fit.mulplayer.utils.SimpleDividerItemDecoration;

/**
 * @author mlyko
 * @since 16.04.2016
 */
public class ArtistsListFragment extends BaseFragment implements IBaseListView<ArtistsListAdapter>, IMenuGetter {
	public ArtistsListPresenter mPresenter;
	@Bind(R.id.artists_list) RecyclerView mAlbumsList;
	public ArtistsListAdapter mArtistsListAdapter;

	/**
	 * Constructor for fragment which can't be constructed classical way because android manages its lifecycle
	 *
	 * @return instance of this fragment
	 */
	public static ArtistsListFragment newInstance() {
		Bundle args = new Bundle();
		ArtistsListFragment fragment = new ArtistsListFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		mBasePresenter = mPresenter = new ArtistsListPresenter(this);
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_artists_list, container, false);
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

	/**
	 * Initializes adapter and RecyclerView for showing data
	 * @param projection
	 */
	public void initList(String[] projection) {
		mArtistsListAdapter = new ArtistsListAdapter(getActivity(), projection);
		mArtistsListAdapter.setOnItemClickListener(mPresenter);

		mAlbumsList.setLayoutManager(new LinearLayoutManager(getActivity()));
		mAlbumsList.setItemAnimator(new DefaultItemAnimator());
		mAlbumsList.setAdapter(mArtistsListAdapter);
		mAlbumsList.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), SimpleDividerItemDecoration.LINE_WHOLE));
	}

	/**
	 * Updates list with refreshed data (swaps the cursor)
	 * @param data
	 */
	public void updateList(Cursor data) {
		if (mArtistsListAdapter == null) return; // TODO should be always here, weird
		mArtistsListAdapter.changeCursor(data);
	}

	/**
	 * Returns adapter for getting data
	 *
	 * @return recyclerview adapter with specific type
	 */
	@Override
	public ArtistsListAdapter getListAdapter() {
		return mArtistsListAdapter;
	}

	/**
	 * Menu getter so that when player overlays toolbar, we can reinflate it
	 *
	 * @return menu resource
	 */
	@Override
	public int getMenuResource() {
		return R.menu.menu_list_albums; // TODO artists
	}
}
