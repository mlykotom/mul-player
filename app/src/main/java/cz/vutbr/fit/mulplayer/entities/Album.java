package cz.vutbr.fit.mulplayer.entities;

import java.util.ArrayList;
import java.util.List;

import cz.vutbr.fit.mulplayer.entities.BaseModel;
import cz.vutbr.fit.mulplayer.entities.Song;

/**
 * @author mlyko
 * @since 08.04.2016
 */
public class Album extends BaseModel {
	public List<Song> mSongs = new ArrayList<>();

	public Album(String mId) {
		super(mId);
	}
}
