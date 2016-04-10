package cz.vutbr.fit.mulplayer.view.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.view.adapter.BaseFragmentPagerAdapter;
import cz.vutbr.fit.mulplayer.view.fragment.PlayerFragment;

public class MainActivity extends BaseActivity implements PlayerFragment.IAudioPlayerListener{

	private BaseFragmentPagerAdapter mBaseFragmentPagerAdapter;
	@Bind(R.id.container) ViewPager mViewPager;
	@Bind(R.id.tabs) TabLayout mTabLayout;

	PlayerFragment mPlayerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
//		setupToolbar("AHOJKY"); // TODO

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mBaseFragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager());

		mPlayerFragment = PlayerFragment.newInstance();
		mBaseFragmentPagerAdapter.addFragment(mPlayerFragment, "Now playing");

		// Set up the ViewPager with the sections adapter.
		mViewPager.setAdapter(mBaseFragmentPagerAdapter);
		mTabLayout.setupWithViewPager(mViewPager);
	}

	@Override
	public void playPause() {

	}

	@Override
	public void stop() {

	}

	@Override
	public void next() {

	}

	@Override
	public void previous() {

	}
}
