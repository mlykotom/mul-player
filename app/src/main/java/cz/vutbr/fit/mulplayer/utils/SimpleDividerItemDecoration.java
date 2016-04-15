package cz.vutbr.fit.mulplayer.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import cz.vutbr.fit.mulplayer.R;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
	@IntDef({LINE_WHOLE, LINE_PADDED})
	@interface PaddingType {
	}

	public static final int LINE_PADDED = 72;
	public static final int LINE_WHOLE = 0;
	private final int mPaddingLeft;
	private Drawable mDivider;

	public SimpleDividerItemDecoration(Context context, @PaddingType int paddingLeft) {
		mDivider = context.getResources().getDrawable(R.drawable.line_divider);
		mPaddingLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, paddingLeft, context.getResources().getDisplayMetrics());
	}

	public SimpleDividerItemDecoration(Context context) {
		this(context, LINE_PADDED);
	}

	@Override
	public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
		int left = parent.getPaddingLeft() + mPaddingLeft;
		int right = parent.getWidth() - parent.getPaddingRight();

		int childCount = parent.getChildCount();
		for (int i = 0; i < childCount; i++) {
			View child = parent.getChildAt(i);

			RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

			int top = child.getBottom() + params.bottomMargin;
			int bottom = top + mDivider.getIntrinsicHeight();

			mDivider.setBounds(left, top, right, bottom);
			mDivider.draw(c);
		}
	}
}