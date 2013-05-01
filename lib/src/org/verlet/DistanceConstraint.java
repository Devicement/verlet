package org.verlet;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Constrains to initial distance
 */
public class DistanceConstraint implements Constraint {
    public Particle a;
    public Particle b;
    private double stiffness;
    public double distance;
    private final Paint paint;

    public DistanceConstraint(Particle a, Particle b, double stiffness, double distance) {
        this.a = a;
        this.b = b;
        this.stiffness = stiffness;
        this.distance = distance;

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        paint.setColor(0xffd8dde2);
        paint.setStyle(Paint.Style.STROKE);
    }

    public DistanceConstraint(Particle a, Particle b, double stiffness) {
        this(a, b, stiffness, a.pos.sub(b.pos).length());
    }

    @Override
    public void relax(double stepCoef) {
        Vec2 normal = NativeVector.vectorSub(a.pos, b.pos);//a.pos.sub(b.pos);
        double m = normal.length2();
        if (m < 1E-5) return; //for performance purposes
        normal.mutableScale(((distance * distance - m) / m) * stiffness * stepCoef);
        a.pos.mutableAdd(normal);
        b.pos.mutableSub(normal);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawLine((float) a.pos.x, (float) a.pos.y, (float) b.pos.x, (float) b.pos.y, paint);
    }
}