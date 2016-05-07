package cz.vutbr.fit.mulplayer.ui;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public abstract class BaseFragmentPresenter extends BasePresenter {
	private static final String TAG = BaseFragmentPresenter.class.getSimpleName();
	BaseFragment mBaseFragment;

	public BaseFragmentPresenter(BaseFragment fragment) {
		mBaseFragment = fragment;
	}

	public void onAttach(){
	}

	public void onCreateView() {
	}

	public void onDestroyView() {
	}

	@Override
	public BaseActivity getBaseActivity() {
		FragmentActivity activity = mBaseFragment.getActivity();
		if (!(activity instanceof BaseActivity)) {
			Log.e(TAG, "Holding activity is not instance of BaseActivity!");
			return null;    // TODO some check or different approach
		}
		return (BaseActivity) activity;
	}
}
