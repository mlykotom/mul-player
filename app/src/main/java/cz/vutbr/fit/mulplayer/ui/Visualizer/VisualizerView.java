package cz.vutbr.fit.mulplayer.ui.Visualizer;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Roman on 5/7/2016.
 */
public class VisualizerView extends View {

    private byte[] mBytes;
    private byte[] mFFTBytes;
    private Rect mRect = new Rect();
    private Visualizer mVisualizer;

    private Paint mFlashPaint = new Paint();
    private Paint mFadePaint = new Paint();

    private Set<Renderer> mRenderers;
    private MediaPlayer mPlayer;

    public VisualizerView(Context context) {
        super(context);
    }

    public VisualizerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VisualizerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VisualizerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        //min. API level required 21
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mBytes = null;
        mFFTBytes = null;

        mFlashPaint.setColor(Color.argb(122, 255, 255, 255));
        mFadePaint.setColor(Color.argb(238, 255, 255, 255)); // Adjust alpha to change how quickly the image fades
        mFadePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));

        mRenderers = new HashSet<Renderer>();

    }

    public void addRenderer(Renderer renderer)
    {
        if(renderer != null)
        {
            mRenderers.add(renderer);
        }
    }

    public MediaPlayer getPlayer(){ return mPlayer; }

    public void link(MediaPlayer player)
    {
        mPlayer = player;
        if(player == null)
        {
            throw new NullPointerException("Cannot link to null MediaPlayer");
        }

        // Create the Visualizer object and attach it to our media player.
        mVisualizer = new Visualizer(player.getAudioSessionId());
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);

        // Pass through Visualizer data to VisualizerView
        Visualizer.OnDataCaptureListener captureListener = new Visualizer.OnDataCaptureListener()
        {
            @Override
            public void onWaveFormDataCapture(Visualizer visualizer, byte[] bytes,
                                              int samplingRate)
            {
                updateVisualizer(bytes);
            }

            @Override
            public void onFftDataCapture(Visualizer visualizer, byte[] bytes,
                                         int samplingRate)
            {
                updateVisualizerFFT(bytes);
            }
        };

        mVisualizer.setDataCaptureListener(captureListener,
                Visualizer.getMaxCaptureRate() / 2, true, true);

        // Enabled Visualizer and disable when we're done with the stream
        mVisualizer.setEnabled(true);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer)
            {
                mVisualizer.setEnabled(false);
            }
        });
    }

    public void updateVisualizer(byte[] bytes) {
        mBytes = bytes;
        invalidate();
    }

    public void updateVisualizerFFT(byte[] bytes) {
        mFFTBytes = bytes;
        invalidate();
    }

    boolean mFlash = false;

    /**
     * Call this to make the visualizer flash. Useful for flashing at the start
     * of a song/loop etc...
     */
    public void flash() {
        mFlash = true;
        invalidate();
    }


    Bitmap mCanvasBitmap;
    Canvas mCanvas;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Create canvas once we're ready to draw
        mRect.set(0, 0, getWidth(), getHeight());

        if(mCanvasBitmap == null )
        {
            mCanvasBitmap = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), Bitmap.Config.ARGB_8888);
        }
        if(mCanvas == null )
        {
            mCanvas = new Canvas(mCanvasBitmap);
        }

        if (mBytes != null) {

            if (mRenderers.isEmpty()) return;
            // Render all audio renderers
            AudioData audioData = new AudioData(mBytes);
            for(Renderer r : mRenderers)
            {
                r.render(mCanvas, audioData, mRect);
            }
        }

        if (mFFTBytes != null) {
            // Render all FFT renderers
            FFTData fftData = new FFTData(mFFTBytes);

            if (mRenderers.isEmpty()) return;

            for(Renderer r : mRenderers)
            {
                r.render(mCanvas, fftData, mRect);
            }
        }

        // Fade out old contents
       // mCanvas.drawPaint(mFadePaint);

//        if(mFlash)
//        {
//            mFlash = false;
//            mCanvas.drawPaint(mFlashPaint);
//        }

        canvas.drawBitmap(mCanvasBitmap, new Matrix(), null);
    }

}