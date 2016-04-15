package cz.vutbr.fit.mulplayer.ui.main;

import android.os.Bundle;

import cz.vutbr.fit.mulplayer.model.MusicService;
import cz.vutbr.fit.mulplayer.ui.BaseActivity;
import cz.vutbr.fit.mulplayer.ui.BaseActivityPresenter;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class MainPresenter extends BaseActivityPresenter {
	public MainPresenter(BaseActivity activity) {
		super(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MusicService.fireAction(mBaseActivity, MusicService.CMD_REBUILD); // TODO necessary?
	}
}
