package cz.vutbr.fit.mulplayer.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author mlyko
 * @since 08.05.2016
 */
public class ImageViewSquare extends ImageView {
	public ImageViewSquare(Context context) {
		super(context);
	}

	public ImageViewSquare(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ImageViewSquare(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public ImageViewSquare(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getMeasuredWidth();
		setMeasuredDimension(width, width);
	}
}
