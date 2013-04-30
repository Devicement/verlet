package com.verlet_android;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Constrains to initial angle between 3 particles with some stiffness (angle is a-b-c)
 */
public class AngleConstraint implements Constraint {
    private Particle a, b, c;
    private double angle;
    private double stiffness;

    public AngleConstraint(Particle a, Particle b, Particle c, double stiffness) {
        this.a = a;
        this.b = b;
        this.c = c;
        angle = b.pos.angle2(a.pos, c.pos);
        this.stiffness = stiffness;
    }

    @Override
    public void relax(double stepCoef) {
        double angle = b.pos.angle2(a.pos, c.pos);
        double diff = angle - angle;

        if (diff <= -Math.PI)
            diff += 2*Math.PI;
        else if (diff >= Math.PI)
            diff -= 2*Math.PI;

        diff *= stepCoef*stiffness;

        a.pos = a.pos.rotate(b.pos, diff);
        c.pos = c.pos.rotate(b.pos, -diff);
        b.pos = b.pos.rotate(a.pos, diff);
        b.pos = b.pos.rotate(c.pos, -diff);

    }

    @Override
    public void draw(Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(5f);
        p.setARGB(51, 255, 255, 0);
        canvas.drawLine((float)a.pos.x, (float)a.pos.y, (float)b.pos.x, (float)b.pos.y, p);
        canvas.drawLine((float)b.pos.x, (float)b.pos.y, (float)c.pos.x, (float)c.pos.y, p);
    }
}