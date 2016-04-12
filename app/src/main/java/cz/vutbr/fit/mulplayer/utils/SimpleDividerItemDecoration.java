package cz.vutbr.fit.mulplayer.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import cz.vutbr.fit.mulplayer.R;

/**
 * @author mlyko
 * @since 12.04.2016
 */
public class SimpleDividerItemDecoration extends RecyclerView.ItemDecoration {
	private static final int PADDING_LEFT = 72;
	private final int mPaddingLeft;
	private Drawable mDivider;

	public SimpleDividerItemDecoration(Context context) {
		mDivider = context.getResources().getDrawable(R.drawable.line_divider);
		mPaddingLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, PADDING_LEFT, context.getResources().getDisplayMetrics());
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