package cz.vutbr.fit.mulplayer.ui.splash_screen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import cz.vutbr.fit.mulplayer.Constants;
import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.model.event.PermissionEvent;
import cz.vutbr.fit.mulplayer.ui.BaseActivity;
import cz.vutbr.fit.mulplayer.ui.main.MainActivity;

/**
 * @author mlyko
 * @since 29.04.2016
 */
public class SplashScreenActivity extends BaseActivity {
	private static final String TAG = SplashScreenActivity.class.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
	}


	@Override
	protected void onStart() {
		super.onStart();
		if (checkPermissionStorage()) {
			continueToApp();
		}
	}

	private void continueToApp() {
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	public boolean checkPermissionStorage() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			// explanation shown
			ActivityCompat.requestPermissions(this, new String[]{
					Manifest.permission.READ_EXTERNAL_STORAGE,
					Manifest.permission.WRITE_EXTERNAL_STORAGE,
			}, Constants.PERMISSION_CODE_READ_STORAGE);
			return false;
		}
		return true;
	}

	@Override
	public void onRequestPermissionsResult(@Constants.PermissionCode int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		switch (requestCode) {
			case Constants.PERMISSION_CODE_READ_STORAGE:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					// permission granted
					EventBus.getDefault().post(new PermissionEvent(Constants.PERMISSION_CODE_READ_STORAGE, true));
					continueToApp();
				} else {
					// permission denied
					Toast.makeText(this, R.string.permission_warning_read_storage, Toast.LENGTH_LONG).show();
//					EventBus.getDefault().post(new PermissionEvent(Constants.PERMISSION_CODE_READ_STORAGE, false));
				}
				break;

			default:
				Log.i(TAG, "Result for not handled permission check");
				break;
		}
	}
}
