package com.verlet_android;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Constrains particle to a fixed position
 */
public class PinConstraint implements Constraint {
    public Particle a;
    public Vec2 pos;

    public PinConstraint(Particle a, Vec2 pos) {
        this.a = a;
        this.pos = pos.clone();
    }

    @Override
    public void relax(double stepCoef) {
        a.pos.mutableSet(pos);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        p.setARGB(25, 0, 153, 255);
        canvas.drawCircle((float) pos.x, (float) pos.y, 6, p);
    }
}
