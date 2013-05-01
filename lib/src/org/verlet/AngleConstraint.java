package org.verlet;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Constrains to initial angle between 3 particles with some stiffness (angle is a-b-c)
 */
public class AngleConstraint implements Constraint {
    private Particle a, b, c;
    private double angle;
    private double stiffness;
    private final Paint paint;

    public AngleConstraint(Particle a, Particle b, Particle c, double stiffness) {
        this.a = a;
        this.b = b;
        this.c = c;
        angle = b.pos.angle2(a.pos, c.pos);
        this.stiffness = stiffness;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5f);
        paint.setARGB(51, 255, 255, 0);
    }

    @Override
    public void relax(double stepCoef) {
        double angle = this.b.pos.angle2(this.a.pos, this.c.pos);
        double diff = angle - this.angle;
        if (diff < 1E-5) return; //for performance purposes

        if (diff <= -Math.PI)
            diff += 2*Math.PI;
        else if (diff >= Math.PI)
            diff -= 2*Math.PI;

        diff *= stepCoef * this.stiffness;

        this.a.pos.mutableRotate(this.b.pos, diff);
        this.c.pos.mutableRotate(this.b.pos, -diff);
        this.b.pos.mutableRotate(this.a.pos, diff);
        this.b.pos.mutableRotate(this.c.pos, -diff);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine((float) a.pos.x, (float) a.pos.y, (float) b.pos.x, (float) b.pos.y, paint);
        canvas.drawLine((float) b.pos.x, (float) b.pos.y, (float) c.pos.x, (float) c.pos.y, paint);
    }
}