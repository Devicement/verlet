package org.verlet.demo;

import android.app.Activity;
import android.os.Bundle;
import org.verlet.*;

public class ShapesExampleActivity extends Activity {
    private VerletView verletView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verletView = new VerletView(this);
        setContentView(verletView);

        final Verlet verlet = verletView.getVerlet();
        verlet.friction = 1;

        Composite segment = Objects.lineSegments(new Vec2[]{new Vec2(20, 10), new Vec2(40, 10),
                new Vec2(60, 10), new Vec2(80, 10), new Vec2(100, 10)}, 0.02);
        segment.pin(0);
        segment.pin(4);
        verlet.addComposite(segment);

        Composite box = Objects.box(new Vec2(300, 40), 70, 120, 0.3);
        verlet.addComposite(box);
        Composite tire2 = Objects.tire(new Vec2(400, 50), 70, 7, 0.1, 0.2);
        verlet.addComposite(tire2);
        Composite tire3 = Objects.tire(new Vec2(600, 50), 70, 3, 1, 1);
        verlet.addComposite(tire3);
    }

    @Override
    protected void onResume() {
        super.onResume();
        verletView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        verletView.pause();
    }
}