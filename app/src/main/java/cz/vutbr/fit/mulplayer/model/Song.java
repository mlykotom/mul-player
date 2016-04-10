package cz.vutbr.fit.mulplayer.model;

/**
 * @author mlyko
 * @since 08.04.2016
 */
public class Song extends BaseModel {

	public String mArtist;
	public String mTitle;
	public String mDuration;
	public String mData;

	public Song(String artist, String title, String duration, String data) {
		super("ASDFGHJ"); // TODO

		this.mArtist = artist;
		this.mTitle = title;
		this.mDuration = duration;
		this.mData = data;
	}
}
