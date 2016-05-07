package cz.vutbr.fit.mulplayer.ui.player;

import android.content.ContentUris;
import android.media.audiofx.Visualizer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cz.vutbr.fit.mulplayer.model.MusicService;
import cz.vutbr.fit.mulplayer.model.Playback;
import cz.vutbr.fit.mulplayer.model.entity.Song;
import cz.vutbr.fit.mulplayer.model.event.PlaybackEvent;
import cz.vutbr.fit.mulplayer.ui.BaseFragmentPresenter;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class PlayerPresenter extends BaseFragmentPresenter {
	private static final String TAG = PlayerPresenter.class.getSimpleName();

	public PlayerFragment mFragment;
	public Song mActualSong;
	private Visualizer mVisualizer;

	int mEndTime;
	int mActualTime;

	public PlayerPresenter(PlayerFragment fragment) {
		super(fragment);
		mFragment = fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupVisualizerFxAndUI();
	}

	private void setupVisualizerFxAndUI() {
		// Create the Visualizer object and attach it to our media player.
		mVisualizer = new Visualizer(Playback.getInstance().getMediaPlayer().getAudioSessionId());
		mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
		mVisualizer.setDataCaptureListener(
				new Visualizer.OnDataCaptureListener() {
					public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
						mFragment.mVisualizerView.updateVisualizer(bytes);
					}

					public void onFftDataCapture(Visualizer visualizer, byte[] bytes, int samplingRate) {
					}
				}, Visualizer.getMaxCaptureRate() / 2, true, false);
	}

	@Override
	public void onStart() {
		super.onStart();
		EventBus.getDefault().register(this);
		mVisualizer.setEnabled(true);
	}

	@Override
	public void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(this);
//		mVisualizer.release();
	}

	public void playPauseSong() {
		MusicService.fireAction(mFragment.getActivity(), MusicService.CMD_PLAY_PAUSE);
	}

	public void nextSong() {
		MusicService.fireAction(mFragment.getActivity(), MusicService.CMD_NEXT);
	}

	public void previousSong() {
		MusicService.fireAction(mFragment.getActivity(), MusicService.CMD_PREVIOUS);
	}

	public void playbackSeekbarChanged(int actualTime) {
		if (actualTime == mActualTime) return;
		MusicService.fireAction(mFragment.getActivity(), MusicService.CMD_SEEK_TO, actualTime);
	}

	final public static Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

	/**
	 * When song is playing and time changed (called every 100ms) so that UI can be refreshed
	 * If the same song was sent only updates time
	 *
	 * @param event containing information about song and playback info
	 */
	@Subscribe
	public void onEvent(PlaybackEvent event) {
		if (event.song == null) {
			Log.w(TAG, "Song is null");
			return;
		}

		if (!event.song.equals(mActualSong)) {
			mActualSong = event.song;
			mFragment.setPlaybackArtistTitle(mActualSong.artist, mActualSong.title);
			mFragment.setPlaybackSeekbarMax(mActualSong.duration);
			mEndTime = mActualSong.duration;
			// getting URI for album artwork
			Uri uri = ContentUris.withAppendedId(sArtworkUri, mActualSong.albumId);
			mFragment.setAlbumArtwork(uri);
		}

		// if time is -1 -> we paused the song
		if (event.time >= 0) {
			mActualTime = event.time;
			mFragment.setPlaybackTime(mActualTime, mEndTime);
		}

		// setup if its playing or stopped
		mFragment.setPlayerButtonPlayPause(event.isPlaying);
	}
}
