package cz.vutbr.fit.mulplayer.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mlyko
 * @since 08.04.2016
 */
public class Artist extends BaseModel {
	public List<Album> mAlbums = new ArrayList<>();

	public Artist(long mId) {
		super(mId);
	}
}
