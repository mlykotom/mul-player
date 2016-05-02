package cz.vutbr.fit.mulplayer.model.event;

import cz.vutbr.fit.mulplayer.Constants;

/**
 * @author mlyko
 * @since 29.04.2016
 */
public class PermissionEvent {
	public @Constants.PermissionCode int permissionCode;
	public boolean isGranted;

	public PermissionEvent(int permissionCode, boolean granted) {
		this.permissionCode = permissionCode;
		this.isGranted = granted;
	}
}
