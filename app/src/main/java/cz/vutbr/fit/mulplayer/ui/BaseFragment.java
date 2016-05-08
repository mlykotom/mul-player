package cz.vutbr.fit.mulplayer.ui;

import android.content.Context;
import android.content.SharedPreferences;
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
	protected BaseFragmentPresenter mBasePresenter;

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (mBasePresenter != null) mBasePresenter.onAttach();
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mBasePresenter != null) mBasePresenter.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		if (mBasePresenter != null) mBasePresenter.onCreateView();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mBasePresenter != null) mBasePresenter.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mBasePresenter != null) mBasePresenter.onStart();
	}

	@Override
	public void onPause() {
		if (mBasePresenter != null) mBasePresenter.onPause();
		super.onPause();
	}

	@Override
	public void onStop() {
		if (mBasePresenter != null) mBasePresenter.onStop();
		super.onStop();
	}

	@Override
	public void onDestroy() {
		if (mBasePresenter != null) mBasePresenter.onDestroy();
		super.onDestroy();
	}

	public SharedPreferences getPreferences(int mode) {
		return getActivity().getPreferences(mode);
	}
}
