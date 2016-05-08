/**
 * Copyright 2011, Felix Palmer
 *
 * Licensed under the MIT license:
 * http://creativecommons.org/licenses/MIT/
 */

package cz.vutbr.fit.mulplayer.ui.Visualizer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import cz.vutbr.fit.mulplayer.R;

public class VisualizerView extends View {

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
		init();
	}

	public VisualizerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public VisualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		mBytes = null;
		mBytesFFT = null;


		mForePaint.setStrokeWidth(1f);
		mForePaint.setAntiAlias(true);
		mForePaint.setColor(Color.rgb(0, 128, 255));
		mSelectedVisualizerType = R.id.visualizer_line;
//		mForePaint.setStrokeWidth(50f);
//		mForePaint.setAntiAlias(true);
//		mForePaint.setColor(Color.argb(200, 56, 138, 252));
		mDivisions=16;
	}

	public void updateVisualizer(byte[] bytes) {
		mBytes = bytes;
		invalidate();
	}

	public void updateVisualizerFFT(byte[] bytes) {
		mBytesFFT = bytes;
		invalidate();
	}

	public void onVisualizerTypeChanged(int id){
		if (id == R.id.visualizer_line){
			mForePaint.setStrokeWidth(1f);
			mForePaint.setAntiAlias(true);
			mForePaint.setColor(Color.rgb(0, 128, 255));
		}

		if (id == R.id.visualizer_bars){
			mForePaint.setStrokeWidth(50f);
			mForePaint.setAntiAlias(true);
			mForePaint.setColor(Color.argb(200, 56, 138, 252));
		}

		mSelectedVisualizerType = id;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (mSelectedVisualizerType == R.id.visualizer_line) {
			drawAudioData(canvas);
		}
		if (mSelectedVisualizerType == R.id.visualizer_bars) {
			drawFFTData(canvas);
		}

	}

	private void drawAudioData(Canvas canvas){
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

	private void drawFFTData(Canvas canvas){
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
			mFFTPoints[i * 4 + 3] = mRect.height() - (dbValue * 2 - 10);

		}

		canvas.drawLines(mFFTPoints, mForePaint);
	}

}