package cz.vutbr.fit.mulplayer.ui.player;

import android.net.Uri;

import cz.vutbr.fit.mulplayer.ui.IBaseView;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public interface IPlayerView extends IBaseView{
    void playPause();
    void previousSong();
    void nextSong();

    void setAlbumArtwork(Uri albumArtwork);

    void setPlayerButtonPlayPause(boolean isPlaying);
    void setPlaybackArtistTitle(String artist, String title);
    void setPlaybackTime(int actualTime, int endTime);

    void setPlaybackSeekbarMax(int duration);
}
