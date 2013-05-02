package org.verlet;

import android.graphics.Canvas;

import java.util.ArrayList;

/**
 * Representation of single object on scene.
 */
public class Composite {
    public interface DrawInterface {
        public void draw(Canvas canvas, Composite composite);
    }

    public ArrayList<Particle> particles = new ArrayList<Particle>();
    public ArrayList<Constraint> constraints = new ArrayList<Constraint>();
    public DrawInterface drawParticles = null;
    public DrawInterface drawConstraints = null;

    public PinConstraint pin(int index, Vec2 pos) {
        PinConstraint pc = new PinConstraint(particles.get(index), pos);
        constraints.add(pc);
        return pc;
    }

    public PinConstraint pin(int index) {
        return pin(index, particles.get(index).pos);
    }

    public void draw(Canvas canvas) {
        // draw constraints
        if (drawConstraints != null) {
            drawConstraints.draw(canvas, this);
        } else {
            for (Constraint constraint : constraints) constraint.draw(canvas);
        }

        // draw particles
        if (drawParticles != null) {
            drawParticles.draw(canvas, this);
        } else {
            for (Particle p : particles) {
                p.draw(canvas);
            }
        }
    }
}
