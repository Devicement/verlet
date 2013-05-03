package org.verlet;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Constrains particle to a fixed position
 */
public class PinConstraint implements Constraint {
    public Particle a;
    public Vec2 pos;
    private final Paint p;

    public PinConstraint(Particle a, Vec2 pos) {
        this.a = a;
        this.pos = pos.clone();
        p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        p.setARGB(25, 0, 153, 255);
    }

    @Override
    public void relax(double stepCoef) {
        a.pos.mutableSet(pos);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle((float) a.pos.x, (float) a.pos.y, 6, p);
    }
}
