package cz.vutbr.fit.mulplayer.ui;

import android.os.Bundle;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public abstract class BasePresenter {
	public void onCreate(Bundle savedInstanceState) {
	}

	public void onStart() {
	}

	public void onResume() {
	}

	public void onPause() {
	}

	public void onStop() {
	}

	public void onDestroy() {
	}

	public abstract BaseActivity getBaseActivity();
}
