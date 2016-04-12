package cz.vutbr.fit.mulplayer.mvp.main;

import cz.vutbr.fit.mulplayer.model.AudioService;
import cz.vutbr.fit.mulplayer.mvp.BaseActivity;
import cz.vutbr.fit.mulplayer.mvp.BaseActivityPresenter;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class MainPresenter extends BaseActivityPresenter {
	/**
	 * Behaves like constructor + onCreate()
	 *
	 * @param activity bound to
	 */
	public MainPresenter(BaseActivity activity) {
		super(activity);
		AudioService.fireAction(mBaseActivity, AudioService.INIT);
	}
}
