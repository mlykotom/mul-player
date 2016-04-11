package cz.vutbr.fit.mulplayer.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mlyko
 * @since 08.04.2016
 */
public class Album extends BaseModel {
	public List<Song> mSongs = new ArrayList<>();

	public Album(long mId) {
		super(mId);
	}
}
