package cz.vutbr.fit.mulplayer.utils;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;

import cz.vutbr.fit.mulplayer.R;
import cz.vutbr.fit.mulplayer.application.App;

/**
 * @author mlyko
 * @since 21.04.2016
 */
public class Validator {
	private static final String TAG = Validator.class.getSimpleName();

	@IntDef(flag = true, value = {INTEGER, DOUBLE, EMAIL, IP_ADDRESS})
	public @interface ValidationType {
	}

	public static final int INTEGER = 1;
	public static final int DOUBLE = 1 << 1;
	public static final int EMAIL = 1 << 2;
	public static final int IP_ADDRESS = 1 << 3;

	/**
	 * Validates specified text layout for emptiness and optional additionals
	 *
	 * @param textInputLayout view which will be validated
	 * @param flags           any additional validations
	 * @return success if input was validated correctly
	 */
	public static boolean validate(TextInputLayout textInputLayout, @ValidationType int flags) {
		EditText editText = textInputLayout.getEditText();
		if (editText == null) {
			Log.w(TAG, "Inside TextInputLayout is no EditText!");
			return false;
		}

		Context appContext = App.getContext();

		String input = editText.getText().toString().trim();
		if (input.length() == 0) {
			textInputLayout.requestFocus();
			textInputLayout.setError(appContext.getString(R.string.validation_empty));
			return false;
		}

		// validation integer/double
		if ((flags & INTEGER) == INTEGER || (flags & DOUBLE) == DOUBLE) {
			try {
				//noinspection ResultOfMethodCallIgnored
				Double.parseDouble(input);
				//noinspection ResultOfMethodCallIgnored
				Integer.parseInt(input);
			} catch (NumberFormatException e) {
				textInputLayout.requestFocus();
				textInputLayout.setError(appContext.getString(R.string.validation_number));
				return false;
			}
		}

		if ((flags & EMAIL) == EMAIL) {
			if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
				textInputLayout.requestFocus();
				textInputLayout.setError(appContext.getString(R.string.validation_email));
				return false;
			}
		}

		if ((flags & IP_ADDRESS) == IP_ADDRESS) {
			if (!Patterns.IP_ADDRESS.matcher(input).matches()) {
				textInputLayout.requestFocus();
				textInputLayout.setError(appContext.getString(R.string.validation_ip_address));
				return false;
			}
		}

		textInputLayout.setError(null);
		return true;
	}

	public static boolean validate(TextInputLayout textInputLayout) {
		return validate(textInputLayout, 0);
	}
}
