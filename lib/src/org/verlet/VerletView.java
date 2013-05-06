package org.verlet;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class VerletView extends SurfaceView {
    public interface OnFrameListener {
        public void onFrame();
    }

    private OnFrameListener onFrameListener = null;
    private Thread drawThread = null;
    private Thread processThread = null;
    private SurfaceHolder holder;
    boolean isItOk = false;
    protected Verlet verlet;
    private final long processPeriod = 1000/50;
    private long lastProcessTime = System.currentTimeMillis();

    public VerletView(Context context) {
        super(context);
        init();
    }

    private void init() {
        holder = getHolder();
        verlet = new Verlet();
        setOnTouchListener(verlet);
        post(new Runnable() {
            @Override
            public void run() {
                verlet.setSize(getWidth(), getHeight());
            }
        });
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        if (verlet != null) {
            verlet.setCanvas(canvas);
            verlet.draw();
        }
        super.onDraw(canvas);
    }

    public void pause() {
        isItOk = false;
        try {
            drawThread.join();
            processThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        drawThread = null;
        processThread = null;
    }

    public void resume() {
        isItOk = true;
        drawThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isItOk) {
                    if (!holder.getSurface().isValid()) {
                        continue;
                    }
                    Canvas c = holder.lockCanvas();
                    onDraw(c);
                    holder.unlockCanvasAndPost(c);
                }
            }
        });
        drawThread.start();

        processThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isItOk) {
                    process();

                    long elapsedTime = System.currentTimeMillis() - lastProcessTime;
                    lastProcessTime = System.currentTimeMillis();


                    try {
                        // sleep for 1 millisecond is made because if we have no sleep - all time in consumed
                        // by process thread, leaving no time for draw thread (so we won't see anything on slow systems)
                        Thread.sleep(elapsedTime < processPeriod ? processPeriod - elapsedTime: 1);
                    } catch (InterruptedException e) {
                        Log.e("Exception", e.toString());
                    }

                }
            }
        });
        processThread.start();
    }

    private synchronized void process() {
        if (verlet != null && verlet.width != 0 && verlet.height != 0) {
            if (onFrameListener != null) onFrameListener.onFrame();
            verlet.process(8);
        }
    }

    public Verlet getVerlet() {
        return verlet;
    }

    public void setOnFrameListener(OnFrameListener onFrameListener) {
        this.onFrameListener = onFrameListener;
    }
}