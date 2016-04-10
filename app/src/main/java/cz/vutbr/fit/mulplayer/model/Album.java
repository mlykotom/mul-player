package cz.vutbr.fit.mulplayer.model;

import java.util.ArrayList;
import java.util.List;

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
