package cz.vutbr.fit.mulplayer.ui.player;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.ui.BaseFragment;
import cz.vutbr.fit.mulplayer.utils.CircleTransform;
import cz.vutbr.fit.mulplayer.utils.Utils;

/**
 * @author mlyko
 * @since 10.04.2016
 */
public class PlayerFragment extends BaseFragment implements IPlayerView {
	PlayerPresenter mPresenter;
	private static Transformation sCircleTransformation = new CircleTransform();
	// mini player
	@Bind(R.id.mini_player_album_art) ImageView mMiniPlayerAlbumArt;
	@Bind(R.id.mini_player_artist) TextView mMiniPlayerArtist;
	@Bind(R.id.mini_player_title) TextView mMiniPlayerTitle;
	@Bind(R.id.mini_player_button_play_pause) FloatingActionButton mMiniPlayerButtonPlayPause;
	// large player
	@Bind(R.id.player_button_play_pause) FloatingActionButton mPlayerButtonPlayPause;
	@Bind(R.id.player_button_next) FloatingActionButton mPlayerButtonNext;
	@Bind(R.id.player_button_previous) FloatingActionButton mPlayerButtonPrevious;
	@Bind(R.id.player_seekbar) SeekBar mPlayerSeekbar;
	@Bind(R.id.player_artist) TextView mPlayerArtist;
	@Bind(R.id.player_title) TextView mPlayerTitle;
	@Bind(R.id.player_playback_time) TextView mPlayerPlaybackTime;
	@Bind(R.id.player_album_art) ImageView mPlayerAlbumArt;

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
		mBasePresenter = mPresenter = new PlayerPresenter(this);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_player, container, false);
		ButterKnife.bind(this, view);
		mPlayerSeekbar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.unbind(this);
	}

	// ------ UI actions (ui -> presenter) ------ //

	@OnClick({R.id.mini_player_button_play_pause, R.id.player_button_play_pause})
	public void playPause() {
		mPresenter.playPauseSong();
	}

	@OnClick(R.id.player_button_previous)
	public void previousSong() {
		mPresenter.previousSong();
	}

	@OnClick(R.id.player_button_next)
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

	@Override
	public void setAlbumArtwork(Uri albumArtwork) {
		Picasso.with(getActivity()).load(albumArtwork).transform(sCircleTransformation).into(mPlayerAlbumArt);
		// TODO maybe optimize for loading only once
		Picasso.with(getActivity()).load(albumArtwork).into(mMiniPlayerAlbumArt);
	}

	public void setPlayerButtonPlayPause(boolean isPlaying) {
		if (isPlaying) {
			mPlayerButtonPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
			mMiniPlayerButtonPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
		} else {
			mPlayerButtonPlayPause.setImageResource(R.drawable.ic_pause_black_24dp);
			mMiniPlayerButtonPlayPause.setImageResource(R.drawable.ic_pause_black_24dp);
		}
	}

	@Override
	public void setPlaybackArtistTitle(String artist, String title) {
		mMiniPlayerArtist.setText(artist);
		mMiniPlayerTitle.setText(title);
		mPlayerArtist.setText(artist);
		mPlayerTitle.setText(title);
	}

	@Override
	public void setPlaybackTime(int actualTime, int endTime) {
		mPlayerSeekbar.setProgress(actualTime);
		mPlayerPlaybackTime.setText(String.format(
				"%s / %s",
				Utils.formatTime(actualTime),
				Utils.formatTime(endTime)
		));
	}

	@Override
	public void setPlaybackSeekbarMax(int duration) {
		mPlayerSeekbar.setMax(duration);
	}
}
