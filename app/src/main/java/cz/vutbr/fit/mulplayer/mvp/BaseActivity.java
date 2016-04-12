package cz.vutbr.fit.mulplayer.mvp;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import cz.vutbr.fit.mulplayer.R;

/**
 * @author mlyko
 * @since 10.04.2016
 */
public abstract class BaseActivity extends AppCompatActivity {
	protected ActionBar mActionBar;
	protected BaseActivityPresenter mPresenter;

	@Override
	protected void onResume() {
		super.onResume();
		mPresenter.onResume();
	}

	@Override
	protected void onPause() {
		mPresenter.onPause();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mPresenter.onDestroy();
		super.onDestroy();
	}

	/**
	 * Helper for initializing app_toolbar and actionbar
	 *
	 * @param title      string of title
	 * @param backButton if set to true adds "<-" arrow to title
	 * @return app_toolbar or null
	 */
	@Nullable
	public Toolbar setupToolbar(String title, boolean backButton) {
		Toolbar toolbar = (Toolbar) findViewById(R.id.app_toolbar);
		if (toolbar != null) {
			setSupportActionBar(toolbar);
			toolbar.setTitle(title);
		}
		mActionBar = getSupportActionBar();
		if (mActionBar != null) {
			mActionBar.setTitle(title);

			if (backButton) {
				mActionBar.setHomeButtonEnabled(true);
				mActionBar.setDisplayHomeAsUpEnabled(true);
			}
		}

		return toolbar;
	}

	public Toolbar setupToolbar(String title) {
		return setupToolbar(title, false);
	}

	public Toolbar setupToolbar(@StringRes int titleResId, boolean backButton) {
		String title = getString(titleResId);
		return setupToolbar(title, backButton);
	}

	public Toolbar setupToolbar(@StringRes int titleResId) {
		return setupToolbar(titleResId, false);
	}

	public void setToolbarTitle(String title) {
		ActionBar actionBar = getSupportActionBar();

		if (actionBar != null) {
			actionBar.setTitle(title);
		}
	}
}
