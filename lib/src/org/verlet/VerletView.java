package org.verlet;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class VerletView extends SurfaceView implements Runnable {
    public interface OnFrameListener {
        public void onFrame();
    }

    private OnFrameListener onFrameListener = null;
    private Thread t = null;
    private SurfaceHolder holder;
    boolean isItOk = false;
    protected Verlet verlet;

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

    @Override
    protected void onDraw(Canvas canvas) {
        if (verlet != null && verlet.width != 0 && verlet.height != 0) {
            if (onFrameListener != null) onFrameListener.onFrame();

            verlet.setCanvas(canvas);
            verlet.frame(16);
            verlet.draw();
        }

        super.onDraw(canvas);
    }

    public void pause() {
        isItOk = false;
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t = null;
    }

    public void resume() {
        isItOk = true;
        t = new Thread(this);
        t.start();
    }

    public Verlet getVerlet() {
        return verlet;
    }

    public void setOnFrameListener(OnFrameListener onFrameListener) {
        this.onFrameListener = onFrameListener;
    }
}