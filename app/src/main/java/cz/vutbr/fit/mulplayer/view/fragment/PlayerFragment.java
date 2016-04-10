package cz.vutbr.fit.mulplayer.view.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.model.AudioService;

/**
 * @author mlyko
 * @since 10.04.2016
 */
public class PlayerFragment extends Fragment {

//	IAudioPlayerListener mListener;

	@Bind(R.id.button_play_pause)
	ImageView mPlayPauseButton;
	@Bind(R.id.button_next)
	ImageView mNextButton;
	@Bind(R.id.button_prev)
	ImageView mPreviousButton;

	@OnClick(R.id.button_play_pause)
	public void playPause(ImageView view) {
		AudioService.setAction(getActivity(), AudioService.PLAY_PAUSE);
	}

	@OnClick(R.id.button_next)
	public void nextSong(ImageView view){

	}

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

//	@Override
//	public void onAttach(Context context) {
//		super.onAttach(context);
//		if (context instanceof IAudioPlayerListener) {
//			mListener = (AudioService.IAudioPlayerListener) context;
//		} else {
//			throw new RuntimeException(context.toString() + " must implement IAudioPlayerListener");
//		}
//	}

	@Override
	public void onDetach() {
		super.onDetach();
//		mListener = null;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_player, container, false);
		ButterKnife.bind(this, view);

		// TODO Inflate layout
		return view;
	}
}
