package cz.vutbr.fit.mulplayer.ui.main;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.ui.BaseActivity;
import cz.vutbr.fit.mulplayer.ui.albums_list.AlbumsListFragment;
import cz.vutbr.fit.mulplayer.ui.player.PlayerFragment;
import cz.vutbr.fit.mulplayer.ui.songs_list.SongsListFragment;
import cz.vutbr.fit.mulplayer.model.adapter.BaseFragmentPagerAdapter;

public class MainActivity extends BaseActivity {
	private BaseFragmentPagerAdapter mBaseFragmentPagerAdapter;
	@Bind(R.id.container)
	ViewPager mViewPager;
	@Bind(R.id.tabs)
	TabLayout mTabLayout;

	PlayerFragment mPlayerFragment;
	SongsListFragment mSongsListFragment;
	AlbumsListFragment mAlbumsListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mPresenter = new MainPresenter(this);
		super.onCreate(savedInstanceState);
		mPresenter.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupToolbar(R.string.app_name);
		ButterKnife.bind(this);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mBaseFragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager());

		mAlbumsListFragment = AlbumsListFragment.newInstance();
		mBaseFragmentPagerAdapter.addFragment(mAlbumsListFragment, getString(R.string.albums_title));

		mSongsListFragment = SongsListFragment.newInstance();
		mBaseFragmentPagerAdapter.addFragment(mSongsListFragment, getString(R.string.songs_title));

		mPlayerFragment = PlayerFragment.newInstance();
		mBaseFragmentPagerAdapter.addFragment(mPlayerFragment, getString(R.string.playback_title));

		// Set up the ViewPager with the sections adapter.
		mViewPager.setAdapter(mBaseFragmentPagerAdapter);
		mTabLayout.setupWithViewPager(mViewPager);
	}
}
