package cz.vutbr.fit.mulplayer.ui;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public abstract class BaseActivityPresenter extends BasePresenter {
	BaseActivity mBaseActivity;

	public BaseActivityPresenter(BaseActivity activity) {
		mBaseActivity = activity;
	}

	@Override
	public BaseActivity getBaseActivity() {
		return mBaseActivity;
	}
}
