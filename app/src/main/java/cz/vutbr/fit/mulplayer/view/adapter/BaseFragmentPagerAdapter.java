package cz.vutbr.fit.mulplayer.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import cz.vutbr.fit.mulplayer.view.fragment.PlaceholderFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class BaseFragmentPagerAdapter extends FragmentPagerAdapter {
	private final List<Fragment> mFragments = new ArrayList<>();
	private final List<String> mFragmentTitles = new ArrayList<>();

	public BaseFragmentPagerAdapter(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	@Override
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	/**
	 * Adds fragment to adapter
	 * @param fragment
	 * @param title
	 */
	public void addFragment(Fragment fragment, String title) {
		mFragments.add(fragment);
		mFragmentTitles.add(title);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mFragmentTitles.get(position);
	}
}
