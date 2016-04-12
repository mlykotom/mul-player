package cz.vutbr.fit.mulplayer.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author mlyko
 * @since 10.04.2016
 */
public abstract class BaseFragment extends Fragment {
	protected BaseFragmentPresenter mPresenter;

	public void onCreate(BaseFragmentPresenter presenter, @Nullable Bundle savedInstanceState) {
		mPresenter = presenter;
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mPresenter != null) mPresenter.onResume();
	}

	@Override
	public void onPause() {
		if (mPresenter != null) mPresenter.onPause();
		super.onPause();
	}

	@Override
	public void onStop() {
		if (mPresenter != null) mPresenter.onStop();
		super.onStop();
	}

	@Override
	public void onDestroy() {
		if (mPresenter != null) mPresenter.onDestroy();
		super.onDestroy();
	}
}
