package cz.vutbr.fit.mulplayer.ui.player;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
public class PlayerFragment extends BaseFragment implements IPlayerView, Visualizer.OnDataCaptureListener {
	private static final String TAG = PlayerFragment.class.getSimpleName();
	private static Transformation sCircleTransformation = new CircleTransform();
	private static final String PREF_VISUALIZER = "pref_visualizer";

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
	PlayerPresenter mPresenter;
	protected SharedPreferences mPreferences;
	public int mVisualizerType;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		mBasePresenter = mPresenter = new PlayerPresenter(this);
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		mPreferences = getPreferences(Context.MODE_PRIVATE);
		mVisualizerType = mPreferences.getInt(PREF_VISUALIZER, R.id.visualizer_bars);
		PlayerFragmentPermissionsDispatcher.setupVisualizerFxAndUIWithCheck(this);
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
	public void onDestroy() {
		super.onDestroy();
		if (mVisualizer != null) mVisualizer.release();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		PlayerFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_player, container, false);
		ButterKnife.bind(this, view);
		mPlayerSeekbar.setOnSeekBarChangeListener(mOnSeekBarChangeListener);
		setHasOptionsMenu(true);
		mVisualizerView.onVisualizerTypeChanged(mVisualizerType);
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.unbind(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		switch (itemId) {
			case R.id.visualizer_line:
			case R.id.visualizer_bars:
				item.setChecked(!item.isChecked());
				mVisualizerType = itemId;
				mPreferences.edit().putInt(PREF_VISUALIZER, mVisualizerType).apply();
				mVisualizerView.onVisualizerTypeChanged(itemId);
				return true;
		}

		return super.onOptionsItemSelected(item);
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
		Picasso.with(getActivity()).load(albumArtwork).placeholder(R.drawable.ic_audio_placeholder).transform(sCircleTransformation).into(mPlayerAlbumArt);
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
		mVisualizer.setDataCaptureListener(this, Visualizer.getMaxCaptureRate() / 2, true, true);
	}

	/**
	 * Method called when a new waveform capture is available.
	 * <p>Data in the waveform buffer is valid only within the scope of the callback.
	 * Applications which needs access to the waveform data after returning from the callback
	 * should make a copy of the data instead of holding a reference.
	 *
	 * @param visualizer   Visualizer object on which the listener is registered.
	 * @param waveform     array of bytes containing the waveform representation.
	 * @param samplingRate sampling rate of the audio visualized.
	 */
	@Override
	public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
		if (mVisualizerView == null) {
			Log.w(TAG, "attempt to update visualizer on empty view");
			return;
		}
		mVisualizerView.updateVisualizer(waveform);
	}

	/**
	 * Method called when a new frequency capture is available.
	 * <p>Data in the fft buffer is valid only within the scope of the callback.
	 * Applications which needs access to the fft data after returning from the callback
	 * should make a copy of the data instead of holding a reference.
	 *
	 * @param visualizer   Visualizer object on which the listener is registered.
	 * @param fft          array of bytes containing the frequency representation.
	 * @param samplingRate sampling rate of the audio visualized.
	 */
	@Override
	public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
		if (mVisualizerView == null) {
			Log.w(TAG, "attempt to update visualizer on empty view");
			return;
		}
		mVisualizerView.updateVisualizerFFT(fft);
	}
}
