package cz.vutbr.fit.mulplayer.ui.album;

import cz.vutbr.fit.mulplayer.ui.BaseActivity;
import cz.vutbr.fit.mulplayer.ui.BaseActivityPresenter;

/**
 * @author mlyko
 * @since 18.04.2016
 */
public class AlbumPresenter extends BaseActivityPresenter{
	AlbumActivity mActivity;
	public AlbumPresenter(BaseActivity activity) {
		mActivity = (AlbumActivity) activity;
	}
}
