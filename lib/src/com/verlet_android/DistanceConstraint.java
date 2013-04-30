package com.verlet_android;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Constrains to initial distance
 */
public class DistanceConstraint implements Constraint {
    private Particle a, b;
    private double stiffness;
    private double distance;

    public DistanceConstraint(Particle a, Particle b, double stiffness, double distance) {
        this.a = a;
        this.b = b;
        this.stiffness = stiffness;
        this.distance = distance;
    }

    public DistanceConstraint(Particle a, Particle b, double stiffness) {
        this.a = a;
        this.b = b;
        this.stiffness = stiffness;
        distance = a.pos.sub(b.pos).length();
    }

    @Override
    public void relax(double stepCoef) {
        Vec2 normal = a.pos.sub(b.pos);
        double m = normal.length2();
        normal.mutableScale(((distance*distance - m)/m) * stiffness * stepCoef);
        a.pos.mutableAdd(normal);
        b.pos.mutableSub(normal);
    }

    @Override
    public void draw(Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setAntiAlias(true);
        p.setColor(0xffd8dde2);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawLine((float)a.pos.x, (float)a.pos.y, (float)b.pos.x, (float)b.pos.y, p);
    }
}