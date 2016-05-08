package cz.vutbr.fit.mulplayer.ui.player;


import android.Manifest;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import cz.vutbr.fit.mulplayer.model.Playback;
import cz.vutbr.fit.mulplayer.ui.BaseFragment;
import cz.vutbr.fit.mulplayer.ui.Visualizer.VisualizerView;
import cz.vutbr.fit.mulplayer.utils.CircleTransform;
import cz.vutbr.fit.mulplayer.utils.Utils;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * @author mlyko
 * @since 10.04.2016
 */
@RuntimePermissions
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
	@Bind(R.id.player_song_mime) TextView mPlayerSongMimeType;

	@Bind(R.id.player_visualizer) public VisualizerView mVisualizerView;
	private Visualizer mVisualizer;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		mBasePresenter = mPresenter = new PlayerPresenter(this);
		super.onCreate(savedInstanceState);
		PlayerFragmentPermissionsDispatcher.setupVisualizerFxAndUIWithCheck(this);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		PlayerFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mVisualizer != null) mVisualizer.setEnabled(true);
	}

	@Override
	public void onStop() {
		super.onStop();
		if (mVisualizer != null) mVisualizer.setEnabled(false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_player, container, false);
		ButterKnife.bind(this, view);
		mPlayerSeekbar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);

		addLineRenderer();

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
		Picasso.with(getActivity()).load(albumArtwork).transform(sCircleTransformation).placeholder(R.drawable.ic_audio_placeholder).into(mPlayerAlbumArt);
		// TODO maybe optimize for loading only once
		Picasso.with(getActivity()).load(albumArtwork).placeholder(R.drawable.ic_audio_placeholder).into(mMiniPlayerAlbumArt);
	}

	public void setPlayerButtonPlayPause(boolean isPlaying) {
		if (!isPlaying) {
			mPlayerButtonPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
			mMiniPlayerButtonPlayPause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
		} else {
			mPlayerButtonPlayPause.setImageResource(R.drawable.ic_pause_black_24dp);
			mMiniPlayerButtonPlayPause.setImageResource(R.drawable.ic_pause_black_24dp);
		}
	}

	@Override
	public void setPlaybackArtistTitle(String artist, String title, String mimeType) {
		mMiniPlayerArtist.setText(artist);
		mMiniPlayerTitle.setText(title);
		mPlayerArtist.setText(artist);
		mPlayerTitle.setText(title);

		String[] mimeSong = mimeType.split("/");
		if (mimeSong.length > 0) {
			mPlayerSongMimeType.setText(mimeSong[1].toUpperCase());
		} else {
			mPlayerSongMimeType.setText(null);
		}

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


	@NeedsPermission(Manifest.permission.RECORD_AUDIO)
	public void setupVisualizerFxAndUI() {
		// Create the Visualizer object and attach it to our media player.
		mVisualizer = new Visualizer(Playback.getInstance().getMediaPlayer().getAudioSessionId());
		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		mVisualizer.setDataCaptureListener(
				new Visualizer.OnDataCaptureListener() {
					public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
						mVisualizerView.updateVisualizer(bytes);
					}

					public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
					}
				}, Visualizer.getMaxCaptureRate() / 2, true, false);

		mVisualizer.setEnabled(true);
	}

	private void addLineRenderer() {
//		Paint linePaint = new Paint();
//		linePaint.setStrokeWidth(1f);
//		linePaint.setAntiAlias(true);
//		linePaint.setColor(Color.argb(88, 0, 128, 255));
//
//		Paint lineFlashPaint = new Paint();
//		lineFlashPaint.setStrokeWidth(5f);
//		lineFlashPaint.setAntiAlias(true);
//		lineFlashPaint.setColor(Color.argb(188, 255, 255, 255));
//		LineRenderer lineRenderer = new LineRenderer(linePaint, lineFlashPaint, true);
//		mVisualizerView.addRenderer(lineRenderer);
		//mVisualizer.link(Playback.getInstance(null).getMediaPlayer());
	}

}
