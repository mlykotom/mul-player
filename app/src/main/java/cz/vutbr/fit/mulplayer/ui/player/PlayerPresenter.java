package cz.vutbr.fit.mulplayer.ui.player;

import android.content.ContentUris;
import android.net.Uri;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import cz.vutbr.fit.mulplayer.model.MusicService;
import cz.vutbr.fit.mulplayer.model.entity.Song;
import cz.vutbr.fit.mulplayer.model.event.PlaybackEvent;
import cz.vutbr.fit.mulplayer.model.event.SongEvent;
import cz.vutbr.fit.mulplayer.ui.BaseFragmentPresenter;
import cz.vutbr.fit.mulplayer.ui.IBaseView;

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
	}

	@Override
	public void onResume() {
		super.onResume();
		EventBus.getDefault().register(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		EventBus.getDefault().unregister(this);
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
	 * When new song should start/pause playing.
	 *
	 * @param event containing information about song and playback info
	 */
	@Subscribe
	public void onEvent(SongEvent event) {
		mFragment.setPlayerButtonPlayPause(!event.isPlaying);
		mActualSong = event.song;
		if (mActualSong == null) return;
		mFragment.setPlaybackArtistTitle(mActualSong.artist, mActualSong.title);
		mFragment.setPlaybackSeekbarMax(mActualSong.duration);
//		mActualTime = !event.isPlaying ? 0 : mActualTime; // TODO so that time stays the same when pause but from 0 when new song
		mEndTime = mActualSong.duration;
		// getting URI for album artwork
		Uri uri = ContentUris.withAppendedId(sArtworkUri, mActualSong.albumId);
		mFragment.setAlbumArtwork(uri);
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
