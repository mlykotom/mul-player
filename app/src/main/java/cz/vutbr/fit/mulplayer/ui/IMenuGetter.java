package cz.vutbr.fit.mulplayer.ui;

import android.support.annotation.MenuRes;

/**
 * @author mlyko
 * @since 20.04.2016
 */
public interface IMenuGetter {
	/**
	 * Menu getter so that when player overlays toolbar, we can reinflate it
	 *
	 * @return menu resource
	 */
	@MenuRes
	int getMenuResource();
}
