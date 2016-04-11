package cz.vutbr.fit.mulplayer.mvp;

import android.os.Bundle;

/**
 * @author mlyko
 * @since 11.04.2016
 */
public class BasePresenter {
    protected BaseActivity mBaseActivity;

    /**
     * Behaves like constructor + onCreate()
     *
     * @param activity bound to
     */
    public BasePresenter(BaseActivity activity) {
        mBaseActivity = activity;
    }

    public void onCreate(Bundle savedInstanceState) {
    }

    public void onResume() {
    }

    public void onPause() {
    }

    public void onDestroy() {
    }
}
