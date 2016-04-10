package cz.vutbr.fit.mulplayer.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.R;

/**
 * @author mlyko
 * @since 10.04.2016
 */
public class PlayerFragment extends Fragment {

	public static PlayerFragment newInstance() {
		Bundle args = new Bundle();
		PlayerFragment fragment = new PlayerFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
//			mParam1 = getArguments().getString(ARG_PARAM1);
//			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_player, container, false);
		ButterKnife.bind(this, view);

		// TODO use views
		return view;
	}

}
