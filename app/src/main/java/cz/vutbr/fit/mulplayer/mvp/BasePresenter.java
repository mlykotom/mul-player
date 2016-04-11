package cz.vutbr.fit.mulplayer.mvp;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class BasePresenter {
	protected BaseActivity mActivity;

	/**
	 * Behaves like constructor + onCreate()
	 * @param activity bound to
	 */
	public BasePresenter(BaseActivity activity) {
		mActivity = activity;
	}

	public void onResume() {
	}

	public void onStop(){

	}
}
