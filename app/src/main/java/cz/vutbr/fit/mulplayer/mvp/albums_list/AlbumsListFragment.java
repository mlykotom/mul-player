package cz.vutbr.fit.mulplayer.mvp.albums_list;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.model.adapter.ClickableRecyclerAdapter;
import cz.vutbr.fit.mulplayer.mvp.BaseFragment;
import cz.vutbr.fit.mulplayer.utils.SimpleDividerItemDecoration;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class AlbumsListFragment extends BaseFragment implements IAlbumsListView {

	@Bind(R.id.artists_list)
	RecyclerView mAlbumsList;

	public AlbumsListPresenter mPresenter;
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
		mPresenter = new AlbumsListPresenter(this);
		super.onCreate(mPresenter, savedInstanceState);
		mPresenter.onCreate(savedInstanceState);
	}

	@Nullable
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
		mAlbumsListAdapter = new AlbumsListAdapter(getActivity(), null, projection, new ClickableRecyclerAdapter.OnItemClickListener() {
			@Override
			public void onRecyclerViewItemClick(int position, int viewType) {
				Toast.makeText(getActivity(), "ASDFGHJ", Toast.LENGTH_LONG).show();
			}
		});
		mAlbumsList.setLayoutManager(new LinearLayoutManager(getActivity()));
		mAlbumsList.setItemAnimator(new DefaultItemAnimator());
		mAlbumsList.setAdapter(mAlbumsListAdapter);
		mAlbumsList.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
	}

	public void updateList(Cursor data){
		mAlbumsListAdapter.changeCursor(data);
	}
}
