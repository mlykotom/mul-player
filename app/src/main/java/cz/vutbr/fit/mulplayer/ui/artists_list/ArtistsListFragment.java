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
import cz.vutbr.fit.mulplayer.adapter.base.ClickableRecyclerAdapter;
import cz.vutbr.fit.mulplayer.ui.BaseFragment;
import cz.vutbr.fit.mulplayer.ui.IMenuGetter;
import cz.vutbr.fit.mulplayer.utils.SimpleDividerItemDecoration;

/**
 * @author mlyko
 * @since 16.04.2016
 */
public class ArtistsListFragment extends BaseFragment implements IArtistsListView,IMenuGetter {
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

	public void initList(String[] projection) {
		mArtistsListAdapter = new ArtistsListAdapter(getActivity(), null, projection, new ClickableRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onRecyclerViewItemClick(int position, int viewType) {
				mPresenter.setOnRecyclerItemClick(position, viewType);
			}
		});
		mAlbumsList.setLayoutManager(new LinearLayoutManager(getActivity()));
		mAlbumsList.setItemAnimator(new DefaultItemAnimator());
		mAlbumsList.setAdapter(mArtistsListAdapter);
		mAlbumsList.addItemDecoration(new SimpleDividerItemDecoration(getActivity(), SimpleDividerItemDecoration.LINE_WHOLE));
	}

	public void updateList(Cursor data) {
		if(mArtistsListAdapter == null) return; // TODO should be always here, weird
		mArtistsListAdapter.changeCursor(data);
	}

	@Override
	public int getMenuResource() {
		return R.menu.albums_list_menu; // TODO artists
	}
}
