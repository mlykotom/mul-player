package cz.vutbr.fit.mulplayer.mvp.player;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cz.vutbr.fit.mulplayer.entity.Song;
import cz.vutbr.fit.mulplayer.event.PlaybackEvent;
import cz.vutbr.fit.mulplayer.event.SongEvent;
import cz.vutbr.fit.mulplayer.model.AudioService;
import cz.vutbr.fit.mulplayer.mvp.BaseFragmentPresenter;
import cz.vutbr.fit.mulplayer.mvp.IBaseView;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class PlayerPresenter extends BaseFragmentPresenter {
	private static final String TAG = PlayerPresenter.class.getSimpleName();

	public IPlayerView mFragment;
	public Song mActualSong;

	int mEndTime;
	int mActualTime;

	public PlayerPresenter(IBaseView fragment) {
		mFragment = (IPlayerView) fragment;
		EventBus.getDefault().register(this);
	}

	public void onStop() {
		EventBus.getDefault().unregister(this);
	}

	public void playPauseSong() {
		AudioService.fireAction(mFragment.getActivity(), AudioService.PLAY_PAUSE);
	}

	public void nextSong() {
		AudioService.fireAction(mFragment.getActivity(), AudioService.NEXT);
	}

	public void previousSong() {
		AudioService.fireAction(mFragment.getActivity(), AudioService.PREVIOUS);
	}

	public void playbackSeekbarChanged(int actualTime) {
		if (actualTime == mActualTime) return;
		AudioService.fireAction(mFragment.getActivity(), AudioService.SEEK_TO, actualTime);
	}

	/**
	 * When new song should start/pause playing.
	 *
	 * @param event containing information about song and playback info
	 */
	@Subscribe
	public void onEvent(SongEvent event) {
		mActualSong = event.song;
		mFragment.setPlayPauseButton(!event.isPlaying);
		mFragment.setPlaybackArtistTitle(mActualSong.artist, mActualSong.title);
		mFragment.setPlaybackSeekbarMax(mActualSong.duration);
//		mActualTime = !event.isPlaying ? 0 : mActualTime; // TODO so that time stays the same when pause but from 0 when new song
		mEndTime = mActualSong.duration;
	}

	/**
	 * When song is playing and time changed (called every 100ms) so that UI can be refreshed
	 *
	 * @param event
	 */
	@Subscribe
	public void onEvent(PlaybackEvent event) {
		mActualTime = event.time;
		mFragment.setPlaybackTime(mActualTime, mEndTime);
	}
}
