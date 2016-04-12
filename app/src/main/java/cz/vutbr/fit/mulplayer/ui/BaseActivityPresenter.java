package cz.vutbr.fit.mulplayer.ui;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class BaseActivityPresenter extends BasePresenter {
	protected BaseActivity mBaseActivity; // TODO IBaseView
	
	public BaseActivityPresenter(BaseActivity activity) {
		mBaseActivity = activity;
	}
}
