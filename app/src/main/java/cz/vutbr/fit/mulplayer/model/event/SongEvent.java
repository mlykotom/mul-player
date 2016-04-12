package cz.vutbr.fit.mulplayer.model.event;

import cz.vutbr.fit.mulplayer.model.entity.Song;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class SongEvent {
	public Song song;
	public boolean isPlaying;

	public SongEvent(Song song, boolean isPlaying){
		this.song = song;
		this.isPlaying = isPlaying;
	}
}