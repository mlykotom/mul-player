package cz.vutbr.fit.mulplayer.mvp.player;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.Utils;

/**
 * @author mlyko
 * @since 10.04.2016
 */
public class PlayerFragment extends Fragment {
	PlayerPresenter mPresenter;

	@Bind(R.id.button_play_pause)
	ImageButton mPlayPauseButton;
	@Bind(R.id.button_next)
	ImageButton mNextButton;
	@Bind(R.id.button_prev)
	ImageButton mPreviousButton;
	@Bind(R.id.playback_seekbar)
	SeekBar mPlaybackSeekbar;
	@Bind(R.id.playback_artist_title)
	TextView mArtistTitle;
	@Bind(R.id.playback_time)
	TextView mPlaybackTime;

	/**
	 * Constructor for fragment which can't be constructed classical way because android manages its lifecycle
	 *
	 * @return instance of this fragment
	 */
	public static PlayerFragment newInstance() {
		Bundle args = new Bundle();
		PlayerFragment fragment = new PlayerFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPresenter = new PlayerPresenter(this);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_player, container, false);
		ButterKnife.bind(this, view);
		mPlaybackSeekbar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
		return view;
	}

	@Override
	public void onStop() {
		mPresenter.onStop();
		super.onStop();
	}

	// ------ UI actions (ui -> presenter) ------ //

	@OnClick(R.id.button_play_pause)
	public void playPause() {
		mPresenter.playPauseSong();
	}

	@OnClick(R.id.button_prev)
	public void previousSong() {
		mPresenter.previousSong();
	}

	@OnClick(R.id.button_next)
	public void nextSong() {
		mPresenter.nextSong();
	}

	SeekBar.OnSeekBarChangeListener mOnSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
			mPresenter.playbackSeekbarChanged(progress);
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
		}
	};

	// ------ UI setters (presenter -> ui) ------ //

	public void setPlayPauseButton(boolean isPlaying) {
		mPlayPauseButton.setImageResource(isPlaying ? R.drawable.ic_play_arrow_black_24dp : R.drawable.ic_pause_black_24dp);
	}

	public void setPlaybackArtistTitle(String artist, String title) {
		mArtistTitle.setText(String.format("%s - %s", artist, title));
	}

	public void setPlaybackTime(int actualTime, int endTime) {
		mPlaybackSeekbar.setProgress(actualTime);
		mPlaybackTime.setText(String.format(
				"%s / %s",
				Utils.formatTime(actualTime),
				Utils.formatTime(endTime)
		));
	}
}
