package cz.vutbr.fit.mulplayer.mvp.player;

import android.graphics.Bitmap;

import cz.vutbr.fit.mulplayer.mvp.IBaseView;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public interface IPlayerView extends IBaseView{
    void playPause();
    void previousSong();
    void nextSong();

    void setAlbumArtwork(Bitmap bitmap);
    void setPlayPauseButton(boolean isPlaying);
    void setPlaybackArtistTitle(String artist, String title);
    void setPlaybackTime(int actualTime, int endTime);

    void setPlaybackSeekbarMax(int duration);
}
