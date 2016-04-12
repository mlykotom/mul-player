package cz.vutbr.fit.mulplayer.entity;

/**
 * @author mlyko
 * @since 08.04.2016
 */
public class Song extends BaseModel {

	public String artist;
	public String title;
	public String filepath;
	public int duration;

	public Song(long id, String artist, String title, int duration, String filepath) {
		super(id);

		this.artist = artist;
		this.title = title;
		this.duration = duration;
		this.filepath = filepath;
	}
}
