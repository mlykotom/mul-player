package cz.vutbr.fit.mulplayer.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.model.adapter.BaseFragmentPagerAdapter;
import cz.vutbr.fit.mulplayer.ui.BaseActivity;
import cz.vutbr.fit.mulplayer.ui.IMenuGetter;
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
	@Bind(R.id.bottom_player)
	View mBottomSheet;
	ArtistsListFragment mArtistsListFragment;
	PlayerFragment mPlayerFragment;
	SongsListFragment mSongsListFragment;
	AlbumsListFragment mAlbumsListFragment;

	Menu mMenu;
	private BottomSheetBehavior mBottomSheetBehavior;

	private void showPlayer() {
		setIndicator(INDICATOR_DISCARD);
		mMenu.clear();
		getMenuInflater().inflate(R.menu.player_menu, mMenu);
	}

	private void hidePlayer() {
		Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + mViewPager.getCurrentItem());
		mMenu.clear();
		setIndicator(INDICATOR_NONE);
		if (page instanceof IMenuGetter) {
			IMenuGetter menuGetter = ((IMenuGetter) page);
			if (menuGetter.getMenuResource() > 0) {
				getMenuInflater().inflate(menuGetter.getMenuResource(), mMenu);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mBasePresenter = mPresenter = new MainPresenter(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = setupToolbar(""); //R.string.app_name
//		toolbar.setLogo(R.mipmap.ic_launcher);
		// TODO do this in XML ?
		AppBarLayout.LayoutParams p = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
		p.setScrollFlags(0);
		toolbar.setLayoutParams(p);

		ButterKnife.bind(this);

		mPlayerFragment = (PlayerFragment) getSupportFragmentManager().findFragmentById(R.id.player_fragment);

		mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
		mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
			@Override
			public void onStateChanged(@NonNull View bottomSheet, int newState) {
				switch (newState) {
					case BottomSheetBehavior.STATE_COLLAPSED:
						hidePlayer();
						break;

					case BottomSheetBehavior.STATE_EXPANDED:
						showPlayer();
						break;
				}
			}

			@Override
			public void onSlide(@NonNull View bottomSheet, float slideOffset) {

			}
		});

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		mBaseFragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager());

		mArtistsListFragment = ArtistsListFragment.newInstance();
		mBaseFragmentPagerAdapter.addFragment(mArtistsListFragment, getString(R.string.artists_title));

		mAlbumsListFragment = AlbumsListFragment.newInstance();
		mBaseFragmentPagerAdapter.addFragment(mAlbumsListFragment, getString(R.string.albums_title));

		mSongsListFragment = SongsListFragment.newInstance();
		mBaseFragmentPagerAdapter.addFragment(mSongsListFragment, getString(R.string.songs_title));

		// Set up the ViewPager with the sections adapter.
		mViewPager.setAdapter(mBaseFragmentPagerAdapter);
		mTabLayout.setupWithViewPager(mViewPager);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mMenu = menu;
		return true;
	}
}
