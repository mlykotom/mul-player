package cz.vutbr.fit.mulplayer.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import cz.vutbr.fit.mulplayer.R;

/**
 * @author mlyko
 * @since 10.04.2016
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseView {
	protected ActionBar mActionBar;
	protected @Nullable Toolbar mToolbar;
	protected BaseActivityPresenter mBasePresenter;

	@IntDef({INDICATOR_NONE, INDICATOR_BACK, INDICATOR_DISCARD, INDICATOR_ACCEPT})
	@interface IndicatorType {
	}

	public static final int INDICATOR_NONE = 0;
	public static final int INDICATOR_BACK = 1;
	public static final int INDICATOR_DISCARD = R.drawable.ic_close_white_24dp;
	public static final int INDICATOR_ACCEPT = R.drawable.ic_done_black_24dp;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mBasePresenter != null) mBasePresenter.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mBasePresenter != null) mBasePresenter.onResume();
	}

	@Override
	protected void onPause() {
		if (mBasePresenter != null) mBasePresenter.onPause();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (mBasePresenter != null) mBasePresenter.onDestroy();
		super.onDestroy();
	}

	@Override
	public Activity getActivity() {
		return this;
	}

	/**
	 * Helper for initializing toolbar and actionbar
	 *
	 * @param title         string of title
	 * @param indicatorType if set to true adds "<-" arrow to title
	 * @return toolbar or null, if no R.id.app_toolbar was found in layout
	 */
	@Nullable
	public Toolbar setupToolbar(String title, @IndicatorType int indicatorType) {
		mToolbar = (Toolbar) findViewById(R.id.app_toolbar);
		if (mToolbar != null) {
			setSupportActionBar(mToolbar);
			mToolbar.setTitle(title);
		}
		mActionBar = getSupportActionBar();
		if (mActionBar != null) {
			mActionBar.setTitle(title);
			setIndicator(indicatorType);
		}

		return mToolbar;
	}

	public void setIndicator(@IndicatorType int indicatorType) {
		if (indicatorType != INDICATOR_NONE) {
			mActionBar.setHomeButtonEnabled(true);
			mActionBar.setDisplayHomeAsUpEnabled(true);
			if (indicatorType > INDICATOR_BACK) {
				mActionBar.setHomeAsUpIndicator(indicatorType);
			}
		} else {
			mActionBar.setHomeButtonEnabled(false);
			mActionBar.setDisplayHomeAsUpEnabled(false);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public Toolbar setupToolbar(String title) {
		return setupToolbar(title, INDICATOR_NONE);
	}

	public Toolbar setupToolbar(@StringRes int titleResId, @IndicatorType int indicatorType) {
		String title = getString(titleResId);
		return setupToolbar(title, indicatorType);
	}

	public Toolbar setupToolbar(@StringRes int titleResId) {
		return setupToolbar(titleResId, INDICATOR_NONE);
	}

	public void setToolbarTitle(String title) {
		ActionBar actionBar = getSupportActionBar();

		if (actionBar != null) {
			actionBar.setTitle(title);
		}
	}
}
