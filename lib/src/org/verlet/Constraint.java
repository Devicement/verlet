package org.verlet;

import android.graphics.Canvas;

public interface Constraint {
    public void relax(double stepCoef);

    public void draw(Canvas canvas);
}
