package cz.vutbr.fit.mulplayer.ui.main;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.model.adapter.BaseFragmentPagerAdapter;
import cz.vutbr.fit.mulplayer.ui.BaseActivity;
import cz.vutbr.fit.mulplayer.ui.albums_list.AlbumsListFragment;
import cz.vutbr.fit.mulplayer.ui.artists_list.ArtistsListFragment;
import cz.vutbr.fit.mulplayer.ui.player.PlayerFragment;
import cz.vutbr.fit.mulplayer.ui.songs_list.SongsListFragment;

public class MainActivity extends BaseActivity {
	private MainPresenter mPresenter;
	private BaseFragmentPagerAdapter mBaseFragmentPagerAdapter;
	@Bind(R.id.container)
	ViewPager mViewPager;
	@Bind(R.id.tabs)
	TabLayout mTabLayout;

	ArtistsListFragment mArtistsListFragment;
	PlayerFragment mPlayerFragment;
	SongsListFragment mSongsListFragment;
	AlbumsListFragment mAlbumsListFragment;
//	MusicPlayerFragment mMusicPlayerFragment;


	private BottomSheetBehavior mBottomSheetBehavior;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mBasePresenter = mPresenter = new MainPresenter(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = setupToolbar(R.string.app_name);
		// TODO do this in XML ?
		AppBarLayout.LayoutParams p = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
		p.setScrollFlags(0);
		toolbar.setLayoutParams(p);

		ButterKnife.bind(this);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mBaseFragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager());

		mArtistsListFragment = ArtistsListFragment.newInstance();
		mBaseFragmentPagerAdapter.addFragment(mArtistsListFragment, getString(R.string.artists_title));

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
