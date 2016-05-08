package cz.vutbr.fit.mulplayer.ui.main;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.adapter.base.BaseFragmentPagerAdapter;
import cz.vutbr.fit.mulplayer.model.event.AnySongInQueueEvent;
import cz.vutbr.fit.mulplayer.model.persistance.DataRepository;
import cz.vutbr.fit.mulplayer.ui.BaseActivity;
import cz.vutbr.fit.mulplayer.ui.IMenuGetter;
import cz.vutbr.fit.mulplayer.ui.Visualizer.LineRenderer;
import cz.vutbr.fit.mulplayer.ui.Visualizer.VisualizerView;
import cz.vutbr.fit.mulplayer.ui.albums_list.AlbumsListFragment;
import cz.vutbr.fit.mulplayer.ui.artists_list.ArtistsListFragment;
import cz.vutbr.fit.mulplayer.ui.player.PlayerFragment;
import cz.vutbr.fit.mulplayer.ui.songs_list.SongsListFragment;

public class MainActivity extends BaseActivity implements BaseActivity.IPlayerVisibilityControl {
	private static final String TAG = MainActivity.class.getSimpleName();
	private MainPresenter mPresenter;
	@Bind(R.id.container)
	ViewPager mViewPager;
	@Bind(R.id.tabs)
	TabLayout mTabLayout;
	@Bind(R.id.bottom_player)
	View mBottomSheet;
	// fragments
	ArtistsListFragment mArtistsListFragment;
	PlayerFragment mPlayerFragment;
	SongsListFragment mSongsListFragment;
	AlbumsListFragment mAlbumsListFragment;

	Menu mMenu;
	private BottomSheetBehavior mBottomSheetBehavior;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		mBasePresenter = mPresenter = new MainPresenter(this);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupToolbar("");
		ButterKnife.bind(this);

		mPlayerFragment = (PlayerFragment) getSupportFragmentManager().findFragmentById(R.id.player_fragment);

		mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
		mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
			@Override
			public void onStateChanged(@NonNull View bottomSheet, int newState) {
				mPresenter.bottomSheetStateChanged(bottomSheet, newState);
			}

			@Override
			public void onSlide(@NonNull View bottomSheet, float slideOffset) {

			}
		});

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		BaseFragmentPagerAdapter baseFragmentPagerAdapter = new BaseFragmentPagerAdapter(getSupportFragmentManager());

		mArtistsListFragment = ArtistsListFragment.newInstance();
		baseFragmentPagerAdapter.addFragment(mArtistsListFragment, getString(R.string.artists_title));

		mAlbumsListFragment = AlbumsListFragment.newInstance();
		baseFragmentPagerAdapter.addFragment(mAlbumsListFragment, getString(R.string.albums_title));

		mSongsListFragment = SongsListFragment.newInstance();
		baseFragmentPagerAdapter.addFragment(mSongsListFragment, getString(R.string.songs_title));

		// Set up the ViewPager with the sections adapter.
		mViewPager.setAdapter(baseFragmentPagerAdapter);
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

	@Override
	protected void onStart() {
		super.onStart();
		if(DataRepository.getInstance().mQueueSongs.size() > 0){
			onAnySongInQueueEvent(new AnySongInQueueEvent());
		}

		EventBus.getDefault().register(this);
	}

	@Subscribe
	public void onAnySongInQueueEvent(AnySongInQueueEvent event){
		mBottomSheet.setVisibility(View.VISIBLE);
	}

	@Override
	protected void onStop() {
		super.onStop();
		// TODO somehow prevent leaking menu
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onBackPressed() {
		if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
			mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
		} else {
			super.onBackPressed();
		}
	}

	// -- ui actions -- //

	/**
	 * Shows player fragment and set's its menu
	 */
	@Override
	public void showPlayer() {
		setIndicator(INDICATOR_DISCARD);
		mMenu.clear();
		getMenuInflater().inflate(R.menu.menu_player, mMenu);
	}

	/**
	 * Hides shown player and setups actual visible fragment's menu back
	 */
	@Override
	public void hidePlayer() {
		Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.container + ":" + mViewPager.getCurrentItem());
		mMenu.clear();
		setIndicator(INDICATOR_NONE);
		if (page instanceof IMenuGetter) {
			IMenuGetter menuGetter = ((IMenuGetter) page);
			if (menuGetter.getMenuResource() <= 0) return;
			getMenuInflater().inflate(menuGetter.getMenuResource(), mMenu);
		}
	}


}
