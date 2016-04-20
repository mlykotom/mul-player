package cz.vutbr.fit.mulplayer.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.model.MusicService;
import cz.vutbr.fit.mulplayer.ui.BaseActivity;
import cz.vutbr.fit.mulplayer.ui.BaseActivityPresenter;
import cz.vutbr.fit.mulplayer.ui.IMenuGetter;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class MainPresenter extends BaseActivityPresenter {
	MainActivity mActivity;

	public MainPresenter(MainActivity activity) {
		mActivity = activity;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MusicService.fireAction(mActivity, MusicService.CMD_REBUILD); // TODO necessary?
	}
}
