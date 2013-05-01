package org.verlet;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Particle {
    public Vec2 pos;
    public Vec2 lastPos;
    private Paint paint;

    public Particle(Vec2 pos) {
        this.pos = pos.clone();
        this.lastPos = pos.clone();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xff2dad8f);
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle((float) pos.x, (float) pos.y, 2, paint);
    }
}