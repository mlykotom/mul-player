package cz.vutbr.fit.mulplayer.mvp.main;

import android.os.Bundle;

import cz.vutbr.fit.mulplayer.model.AudioService;
import cz.vutbr.fit.mulplayer.mvp.BaseActivity;
import cz.vutbr.fit.mulplayer.mvp.BaseActivityPresenter;

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
		AudioService.fireAction(mBaseActivity, AudioService.INIT);
	}
}
