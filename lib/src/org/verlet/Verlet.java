package org.verlet;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.List;

public class Verlet implements View.OnTouchListener {
    public int width, height;
    private Canvas canvas;
    private Vec2 mouse = new Vec2();
    private boolean mouseDown = false;
    private Vec2 draggedEntity = null;
    private double selectionRadius = 20.0;
    private int highlightColor = 0xff4f545c;
    public Vec2 gravity = new Vec2(0, 0.1);
    public double friction = 0.99;
    private double groundFriction = 0.8;
    private LinkedList<Composite> composites = new LinkedList<Composite>();
    private final double epsilon = 1E-5;
    Paint highlighPaint;

    public Verlet() {
        highlighPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlighPaint.setAntiAlias(true);
        highlighPaint.setStyle(Paint.Style.STROKE);
        highlighPaint.setColor(highlightColor);
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void addComposite(Composite composite) {
        composites.add(composite);
    }

    public void bounds(Particle particle) {
        if (particle.pos.y < 0) particle.pos.y = 0;
        if (particle.pos.y > height - 1) particle.pos.y = height - 1;
        if (particle.pos.x < 0) particle.pos.x = 0;
        if (particle.pos.x > width - 1) particle.pos.x = width - 1;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        mouse.x = motionEvent.getX();
        mouse.y = motionEvent.getY();
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mouseDown = true;
                Vec2 nearest = nearestEntity();
                if (nearest != null) {
                    draggedEntity = nearest;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                mouseDown = false;
                draggedEntity = null;
                break;
        }
        return true;
    }

    public void process(int step) {
        for (Composite c : composites) {
            for (Particle p : c.particles) {

                // calculate velocity
                Vec2 velocity = p.pos.sub(p.lastPos);

                // ground friction
                if (Math.abs(p.pos.y - (height - 1)) <= epsilon && velocity.length2() > epsilon) {
                    velocity.mutableScale(groundFriction);
                } else {
                    velocity.mutableScale(friction);
                }

                // save last good state
                p.lastPos.mutableSet(p.pos);

                // gravity
                p.pos.mutableAdd(gravity);

                // inertia
                p.pos.mutableAdd(velocity);
            }
        }

        if (draggedEntity != null) this.draggedEntity.mutableSet(this.mouse);

        // relax
        double stepCoef = 1.0 / step;
        for (Composite c : composites) {
            for (int i = 0; i < step; ++i) {
                for (Constraint j : c.constraints)  j.relax(stepCoef);

                // one more cycle to make pins be at the same points as their particles
                for (Constraint j : c.constraints) if(j instanceof PinConstraint) j.relax(stepCoef);
            }
        }

        // bounds checking
        for (Composite c : composites) {
            List<Particle> particles = c.particles;
            for (Particle p : particles) this.bounds(p);
        }

    }

    public void draw() {
        canvas.drawColor(0xFFFFFFFF);

        for (Composite c : this.composites) {
            c.draw(canvas);
        }

        // Highlight dragged entity.
        if (draggedEntity != null) {
            canvas.drawCircle((float) draggedEntity.x, (float) draggedEntity.y, 8, highlighPaint);
        }
    }

    private Vec2 nearestEntity() {
        Vec2 entity = null;
        List<Constraint> constraintsNearest = null;
        double d2Nearest = 0.0;

        // find nearest point
        for (Composite c : this.composites) {
            for (Particle p : c.particles) {
                double d2 = p.pos.dist2(mouse);
                if (d2 <= this.selectionRadius * this.selectionRadius && (entity == null || d2 < d2Nearest)) {
                    entity = p.pos;
                    constraintsNearest = c.constraints;
                    d2Nearest = d2;
                }
            }
        }

        // search for pinned constraints for this entity
        if (constraintsNearest != null) {
            for (Constraint c : constraintsNearest)
                if (c instanceof PinConstraint && ((PinConstraint) c).a.pos == entity) entity = ((PinConstraint) c).pos;
        }

        return entity;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }
}