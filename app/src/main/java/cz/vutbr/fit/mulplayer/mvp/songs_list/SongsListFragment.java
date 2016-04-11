package cz.vutbr.fit.mulplayer.mvp.songs_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.R;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class SongsListFragment extends Fragment {
	SongsListPresenter mPresenter;

	@Bind(R.id.songs_list)
	RecyclerView mSongsRecyclerView;
	SongsListAdapter mSongsListAdapter;

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
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPresenter = new SongsListPresenter(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_songs_list, container, false);
		ButterKnife.bind(this, view);

		mSongsListAdapter = new SongsListAdapter(getActivity());
		mSongsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		mSongsRecyclerView.setItemAnimator(new DefaultItemAnimator());
		mSongsRecyclerView.setAdapter(mSongsListAdapter);

		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mPresenter.onActivityCreated(savedInstanceState);
	}
}
