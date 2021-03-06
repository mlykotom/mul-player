package cz.vutbr.fit.mulplayer.ui.main;

import android.support.design.widget.BottomSheetBehavior;
import android.view.View;

import cz.vutbr.fit.mulplayer.ui.BaseActivityPresenter;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class MainPresenter extends BaseActivityPresenter {
	MainActivity mActivity;

	public MainPresenter(MainActivity activity) {
		super(activity);
		mActivity = activity;
	}


	public void bottomSheetStateChanged(View bottomSheet, int newState) {
		switch (newState) {
			case BottomSheetBehavior.STATE_COLLAPSED:
				mActivity.hidePlayer();
				break;

			case BottomSheetBehavior.STATE_EXPANDED:
				mActivity.showPlayer();
				break;
		}
	}
}
