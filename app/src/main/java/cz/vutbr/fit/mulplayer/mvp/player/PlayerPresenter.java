package cz.vutbr.fit.mulplayer.mvp.player;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cz.vutbr.fit.mulplayer.entity.Song;
import cz.vutbr.fit.mulplayer.event.PlaybackEvent;
import cz.vutbr.fit.mulplayer.event.SongEvent;
import cz.vutbr.fit.mulplayer.model.AudioService;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class PlayerPresenter {
	private static final String TAG = PlayerPresenter.class.getSimpleName();
	PlayerFragment mFragment;

	int mEndTime;
	int mActualTime;

	public PlayerPresenter(PlayerFragment fragment) {
		mFragment = fragment;
		EventBus.getDefault().register(this);
	}

	public void onStop() {
		EventBus.getDefault().unregister(this);
	}

	public void playPause() {
		AudioService.setAction(mFragment.getActivity(), AudioService.PLAY_PAUSE);
	}

	public void nextSong() {
		AudioService.setAction(mFragment.getActivity(), AudioService.NEXT);
	}

	public void previousSong() {
		AudioService.setAction(mFragment.getActivity(), AudioService.PREVIOUS);
	}

	public void playbackSeekbarChanged(int actualTime) {
		if (actualTime == mActualTime) return;
		AudioService.setAction(mFragment.getActivity(), AudioService.SEEK_TO, actualTime);
	}

	@Subscribe
	public void onEvent(SongEvent event) {
		Song song = event.song;
		mFragment.setPlayPauseButton(!event.isPlaying);
		mFragment.setPlaybackArtistTitle(song.artist, song.title);
		mFragment.mPlaybackSeekbar.setMax(song.duration);
		mEndTime = song.duration;
	}

	@Subscribe
	public void onEvent(PlaybackEvent event) {
		mActualTime = event.time;
		mFragment.setPlaybackTime(mActualTime, mEndTime);
	}
}
