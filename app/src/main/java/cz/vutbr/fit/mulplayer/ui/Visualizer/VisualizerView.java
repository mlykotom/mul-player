/**
 * Copyright 2011, Felix Palmer
 * <p/>
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */

package cz.vutbr.fit.mulplayer.ui.Visualizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import cz.vutbr.fit.mulplayer.R;

public class VisualizerView extends View {

	private Context mContext;
	private byte[] mBytes;
	private byte[] mBytesFFT;
	private float[] mPoints;
	private float[] mFFTPoints;
	private Rect mRect = new Rect();
	private Paint mForePaint = new Paint();
	private int mDivisions;
	private int mSelectedVisualizerType;

	public VisualizerView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public VisualizerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	public VisualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		init();
	}

	private void init() {
		mBytes = null;
		mBytesFFT = null;

		mForePaint.setStrokeWidth(1f);
		mForePaint.setAntiAlias(true);
		mForePaint.setColor(ContextCompat.getColor(mContext, R.color.primary));
		mSelectedVisualizerType = R.id.visualizer_line;
		mDivisions = 16;
	}

	public void updateVisualizer(byte[] bytes) {
		mBytes = bytes;
		invalidate();
	}

	public void updateVisualizerFFT(byte[] bytes) {
		mBytesFFT = bytes;
		invalidate();
	}

	public void onVisualizerTypeChanged(int id) {
		int color = ContextCompat.getColor(mContext, R.color.primary);

		switch (id) {
			case R.id.visualizer_line:
				mForePaint.setStrokeWidth(3f);
				mForePaint.setAntiAlias(true);
				mForePaint.setColor(color);
				break;

			default:
			case R.id.visualizer_bars:
				mForePaint.setStrokeWidth(50f);
				mForePaint.setAntiAlias(true);
				mForePaint.setColor(color);
				break;
		}

		mSelectedVisualizerType = id;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		switch (mSelectedVisualizerType) {
			case R.id.visualizer_line:
				drawAudioData(canvas);
				break;

			default: // if we dont have type, set this
			case R.id.visualizer_bars:
				drawFFTData(canvas);
				break;
		}
	}

	private void drawAudioData(Canvas canvas) {
		if (mBytes == null) {
			return;
		}
		if (mPoints == null || mPoints.length < mBytes.length * 4) {
			mPoints = new float[mBytes.length * 4];
		}
		mRect.set(0, 0, getWidth(), getHeight());
		for (int i = 0; i < mBytes.length - 1; i++) {
			mPoints[i * 4] = mRect.width() * i / (mBytes.length - 1);
			mPoints[i * 4 + 1] = mRect.height() / 2
					+ ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;
			mPoints[i * 4 + 2] = mRect.width() * (i + 1) / (mBytes.length - 1);
			mPoints[i * 4 + 3] = mRect.height() / 2
					+ ((byte) (mBytes[i + 1] + 128)) * (mRect.height() / 2)
					/ 128;
		}
		canvas.drawLines(mPoints, mForePaint);
	}

	private void drawFFTData(Canvas canvas) {
		if (mBytesFFT == null) {
			return;
		}
		if (mFFTPoints == null || mFFTPoints.length < mBytesFFT.length * 4) {
			mFFTPoints = new float[mBytesFFT.length * 4];
		}

		mRect.set(0, 0, getWidth(), getHeight());

		for (int i = 0; i < mBytesFFT.length / mDivisions; i++) {
			mFFTPoints[i * 4] = i * 4 * mDivisions;
			mFFTPoints[i * 4 + 2] = i * 4 * mDivisions;
			byte rfk = mBytesFFT[mDivisions * i];
			byte ifk = mBytesFFT[mDivisions * i + 1];
			float magnitude = (rfk * rfk + ifk * ifk);
			int dbValue = (int) (10 * Math.log10(magnitude));

			mFFTPoints[i * 4 + 1] = mRect.height();
			mFFTPoints[i * 4 + 3] = mRect.height() - (dbValue * 2);
		}

		canvas.drawLines(mFFTPoints, mForePaint);
	}

}