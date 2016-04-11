package cz.vutbr.fit.mulplayer.mvp.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.model.AudioService;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.mvp.BaseActivity;
import cz.vutbr.fit.mulplayer.view.adapter.BaseFragmentPagerAdapter;
import cz.vutbr.fit.mulplayer.mvp.player.PlayerFragment;

public class MainActivity extends BaseActivity {

	private BaseFragmentPagerAdapter mBaseFragmentPagerAdapter;
	@Bind(R.id.container) ViewPager mViewPager;
	@Bind(R.id.tabs) TabLayout mTabLayout;

	PlayerFragment mPlayerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		mPresenter = new MainPresenter(this);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mBaseFragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager());

		mPlayerFragment = PlayerFragment.newInstance();
		mBaseFragmentPagerAdapter.addFragment(mPlayerFragment, "Now playing");

		// Set up the ViewPager with the sections adapter.
		mViewPager.setAdapter(mBaseFragmentPagerAdapter);
		mTabLayout.setupWithViewPager(mViewPager);
	}

}
