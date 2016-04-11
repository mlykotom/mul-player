package cz.vutbr.fit.mulplayer.model;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public interface IAudioService {
    void play();
    void pause();
    void stop();
    void next();
    void previous();
    void seekTo();
}
