package cz.vutbr.fit.mulplayer.model.event;

import cz.vutbr.fit.mulplayer.model.entity.Song;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class PlaybackEvent {
	public Song song;
	public boolean isPlaying;
	public int time;

	public PlaybackEvent(Song song, boolean isPlaying, int time) {
		this.song = song;
		this.isPlaying = isPlaying;
		this.time = time;
	}

	/**
	 * Start / stop of song
	 *
	 * @param song
	 * @param isPlaying
	 */
	public PlaybackEvent(Song song, boolean isPlaying) {
		this.song = song;
		this.isPlaying = isPlaying;
		this.time = -1;
	}
}
