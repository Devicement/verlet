package com.verlet_android;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Particle {
    public Vec2 pos;
    public Vec2 lastPos;

    public Particle(Vec2 pos) {
        this.pos = pos.clone();
        this.lastPos = pos.clone();
    }

    public void draw(Canvas canvas) {

        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        p.setColor(0xff2dad8f);
//        p.setARGB(0xFF,0,0,0);
        canvas.drawCircle((float)pos.x, (float)pos.y, 2, p);
    }

    @Override
    public String toString() {
        return pos.toString();
    }
}