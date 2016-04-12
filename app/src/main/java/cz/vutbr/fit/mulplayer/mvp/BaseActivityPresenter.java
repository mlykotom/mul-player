package cz.vutbr.fit.mulplayer.mvp;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class BaseActivityPresenter extends BasePresenter {
	protected BaseActivity mBaseActivity; // TODO IBaseView

	/**
	 * Behaves like constructor + onCreate()
	 *
	 * @param activity bound to
	 */
	public BaseActivityPresenter(BaseActivity activity) {
		mBaseActivity = activity;
	}
}
