package com.verlet_android;

import android.graphics.Canvas;

import java.util.LinkedList;
import java.util.List;

public class Composite {
    public interface DrawInterface {
        public void draw(Canvas canvas, Composite composite);
    }

    List<Particle> particles = new LinkedList<Particle>();
    List<Constraint> constraints = new LinkedList<Constraint>();
    DrawInterface drawParticles = null;
    DrawInterface drawConstraints = null;

    public PinConstraint pin(int index, Vec2 pos) {
        PinConstraint pc = new PinConstraint(particles.get(index), pos);
        constraints.add(pc);
        return pc;
    }

    public PinConstraint pin(int index) {
        return pin(index, particles.get(index).pos);
    }
}
