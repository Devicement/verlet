package org.verlet.demo;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import org.verlet.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

class Spider extends Composite {
    public Particle thorax;
    public Particle head;
    public Particle abdomen;
    public ArrayList<Particle> legs = new ArrayList<Particle>();
}

public class SpiderExampleActivity extends Activity {
    private Spider spider(Vec2 origin) {
        double legSeg1Stiffness = 0.99;
        double legSeg2Stiffness = 0.99;
        double legSeg3Stiffness = 0.99;
        double legSeg4Stiffness = 0.99;

        double joint1Stiffness = 1;
        double joint2Stiffness = 0.4;
        double joint3Stiffness = 0.9;

        double bodyStiffness = 1;
        double bodyJointStiffness = 1;

        Spider composite = new Spider();

        composite.thorax = new Particle(origin);
        composite.head = new Particle(origin.add(new Vec2(0, -5)));
        composite.abdomen = new Particle(origin.add(new Vec2(0, 10)));

        composite.particles.add(composite.thorax);
        composite.particles.add(composite.head);
        composite.particles.add(composite.abdomen);

        composite.constraints.add(new DistanceConstraint(composite.head, composite.thorax, bodyStiffness));
        composite.constraints.add(new DistanceConstraint(composite.abdomen, composite.thorax, bodyStiffness));
        composite.constraints.add(new AngleConstraint(composite.abdomen, composite.thorax, composite.head, 0.4));

        // legs
        for (int i = 0; i < 4; ++i) {
            composite.particles.add(new Particle(composite.particles.get(0).pos.add(new Vec2(3, (i - 1.5) * 3))));
            composite.particles.add(new Particle(composite.particles.get(0).pos.add(new Vec2(-3, (i - 1.5) * 3))));

            int len = composite.particles.size();

            composite.constraints.add(new DistanceConstraint(composite.particles.get(len - 2), composite.thorax, legSeg1Stiffness));
            composite.constraints.add(new DistanceConstraint(composite.particles.get(len - 1), composite.thorax, legSeg1Stiffness));

            double lenCoef = 1;
            if (i == 1 || i == 2) lenCoef = 0.7;
            else if (i == 3) lenCoef = 0.9;

            composite.particles.add(new Particle(composite.particles.get(len - 2).pos.add((new Vec2(20, (i - 1.5) * 30)).normal().mutableScale(20 * lenCoef))));
            composite.particles.add(new Particle(composite.particles.get(len - 1).pos.add((new Vec2(-20, (i - 1.5) * 30)).normal().mutableScale(20 * lenCoef))));

            len = composite.particles.size();
            composite.constraints.add(new DistanceConstraint(composite.particles.get(len - 4), composite.particles.get(len - 2), legSeg2Stiffness));
            composite.constraints.add(new DistanceConstraint(composite.particles.get(len - 3), composite.particles.get(len - 1), legSeg2Stiffness));

            composite.particles.add(new Particle(composite.particles.get(len - 2).pos.add((new Vec2(20, (i - 1.5) * 50)).normal().mutableScale(20 * lenCoef))));
            composite.particles.add(new Particle(composite.particles.get(len - 1).pos.add((new Vec2(-20, (i - 1.5) * 50)).normal().mutableScale(20 * lenCoef))));

            len = composite.particles.size();
            composite.constraints.add(new DistanceConstraint(composite.particles.get(len - 4), composite.particles.get(len - 2), legSeg3Stiffness));
            composite.constraints.add(new DistanceConstraint(composite.particles.get(len - 3), composite.particles.get(len - 1), legSeg3Stiffness));

            Particle rightFoot = new Particle(composite.particles.get(len - 2).pos.add((new Vec2(20, (i - 1.5) * 100)).normal().mutableScale(12 * lenCoef)));
            Particle leftFoot = new Particle(composite.particles.get(len - 1).pos.add((new Vec2(-20, (i - 1.5) * 100)).normal().mutableScale(12 * lenCoef)));
            composite.particles.add(rightFoot);
            composite.particles.add(leftFoot);

            composite.legs.add(rightFoot);
            composite.legs.add(leftFoot);

            len = composite.particles.size();
            composite.constraints.add(new DistanceConstraint(composite.particles.get(len - 4), composite.particles.get(len - 2), legSeg4Stiffness));
            composite.constraints.add(new DistanceConstraint(composite.particles.get(len - 3), composite.particles.get(len - 1), legSeg4Stiffness));


            composite.constraints.add(new AngleConstraint(composite.particles.get(len - 6), composite.particles.get(len - 4), composite.particles.get(len - 2), joint3Stiffness));
            composite.constraints.add(new AngleConstraint(composite.particles.get(len - 6 + 1), composite.particles.get(len - 4 + 1), composite.particles.get(len - 2 + 1), joint3Stiffness));

            composite.constraints.add(new AngleConstraint(composite.particles.get(len - 8), composite.particles.get(len - 6), composite.particles.get(len - 4), joint2Stiffness));
            composite.constraints.add(new AngleConstraint(composite.particles.get(len - 8 + 1), composite.particles.get(len - 6 + 1), composite.particles.get(len - 4 + 1), joint2Stiffness));

            composite.constraints.add(new AngleConstraint(composite.particles.get(0), composite.particles.get(len - 8), composite.particles.get(len - 6), joint1Stiffness));
            composite.constraints.add(new AngleConstraint(composite.particles.get(0), composite.particles.get(len - 8 + 1), composite.particles.get(len - 6 + 1), joint1Stiffness));

            composite.constraints.add(new AngleConstraint(composite.particles.get(1), composite.particles.get(0), composite.particles.get(len - 8), bodyJointStiffness));
            composite.constraints.add(new AngleConstraint(composite.particles.get(1), composite.particles.get(0), composite.particles.get(len - 8 + 1), bodyJointStiffness));
        }

        return composite;
    }

    private Composite spiderweb(Vec2 origin, double radius, int segments, int depth) {
        double stiffness = 0.6;
        double tensor = 0.3;
        double stride = (2 * Math.PI) / segments;
        int n = segments * depth;
        double radiusStride = radius / n;

        Composite composite = new Composite();

        // particles
        for (int i = 0; i < n; ++i) {
            double theta = i * stride + Math.cos(i * 0.4) * 0.05 + Math.cos(i * 0.05) * 0.2;
            double shrinkingRadius = radius - radiusStride * i + Math.cos(i * 0.1) * 20;

            double offy = Math.cos(theta * 2.1) * (radius / depth) * 0.2;
            composite.particles.add(new Particle(new Vec2(origin.x + Math.cos(theta) * shrinkingRadius, origin.y + Math.sin(theta) * shrinkingRadius + offy)));
        }

        for (int i = 0; i < segments; i += 4)
            composite.pin(i);

        // constraints
        for (int i = 0; i < n - 1; ++i) {
            // neighbor
            composite.constraints.add(new DistanceConstraint(composite.particles.get(i), composite.particles.get(i + 1), stiffness));

            // span rings
            int off = i + segments;
            if (off < n - 1)
                composite.constraints.add(new DistanceConstraint(composite.particles.get(i), composite.particles.get(off), stiffness));
            else
                composite.constraints.add(new DistanceConstraint(composite.particles.get(i), composite.particles.get(n - 1), stiffness));
        }


        composite.constraints.add(new DistanceConstraint(composite.particles.get(0), composite.particles.get(segments - 1), stiffness));

        for (Constraint c : composite.constraints)
            if (c instanceof DistanceConstraint) {
                ((DistanceConstraint) c).distance *= tensor;
            }

        return composite;
    }

    private void crawl(Composite spiderweb, Spider spider, int leg) {
        int stepRadius = 100;
        int minStepRadius = 35;

        double theta = spider.particles.get(0).pos.angle2(spider.particles.get(0).pos.add(new Vec2(1, 0)), spider.particles.get(1).pos);

        Vec2 boundry1 = new Vec2(Math.cos(theta), Math.sin(theta));
        Vec2 boundry2 = new Vec2(Math.cos(theta + Math.PI / 2), Math.sin(theta + Math.PI / 2));


        int flag1 = leg < 4 ? 1 : -1;
        int flag2 = leg % 2 == 0 ? 1 : 0;

        LinkedList<Particle> paths = new LinkedList<Particle>();

        for (Particle particle : spiderweb.particles) {
            if (particle.pos.sub(spider.particles.get(0).pos).dot(boundry1) * flag1 >= 0
                    && particle.pos.sub(spider.particles.get(0).pos).dot(boundry2) * flag2 >= 0) {
                double d2 = particle.pos.dist2(spider.particles.get(0).pos);

                if (!(d2 >= minStepRadius * minStepRadius && d2 <= stepRadius * stepRadius))
                    continue;

                boolean leftFoot = false;
                for (Constraint constraint : spider.constraints) {
                    for (int k = 0; k < 8; ++k) {
                        if (constraint instanceof DistanceConstraint
                                && ((DistanceConstraint) constraint).a == spider.legs.get(k)
                                && ((DistanceConstraint) constraint).b == particle) {
                            leftFoot = true;
                        }
                    }
                }

                if (!leftFoot) paths.add(particle);
            }
        }

        Iterator it = spider.constraints.iterator();

        while (it.hasNext()) {
            Constraint constraint = (Constraint) it.next();
            if (constraint instanceof DistanceConstraint && ((DistanceConstraint) constraint).a == spider.legs.get(leg)) {
                it.remove();
                break;
            }
        }

        if (paths.size() > 0) {
            Collections.shuffle(paths);
            spider.constraints.add(new DistanceConstraint(spider.legs.get(leg), paths.getFirst(), 1, 0));
        }
    }

    private VerletView verletView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        verletView = new VerletView(this);
        setContentView(verletView);

        final Verlet verlet = verletView.getVerlet();
        verlet.gravity = new Vec2(0., 0.);
        verlet.friction = 1.0;
        verletView.post(new Runnable() {
            @Override
            public void run() {
                initScene(verlet);
            }
        });
    }

    private void initScene(Verlet verlet) {
        final Composite spiderweb = spiderweb(new Vec2(verletView.getWidth() / 2, verletView.getHeight() / 2),
                Math.min(verletView.getWidth(), verletView.getHeight()) / 2, 20, 7);
//        final Composite spiderweb = Objects.tire(new Vec2(verletView.getWidth() / 2, verletView.getHeight() / 2),
//                150, 10, 0.8, 0.8);
        spiderweb.pin(spiderweb.particles.size()-1);
        final Spider spider = spider(new Vec2(verletView.getWidth() / 2, 300));
        verlet.addComposite(spiderweb);
        verlet.addComposite(spider);

        final Paint spiderwebPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        spiderwebPaint.setAntiAlias(true);
        spiderwebPaint.setColor(0xff2dad8f);
        spiderwebPaint.setStyle(Paint.Style.FILL);

        spiderweb.drawParticles = new Composite.DrawInterface() {
            @Override
            public void draw(Canvas canvas, Composite composite) {
                for (Particle p : composite.particles) {
                    canvas.drawCircle((float) p.pos.x, (float) p.pos.y, 1.3f, spiderwebPaint);
                }
            }
        };

        final Paint spederPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        spederPaint.setAntiAlias(true);
        spederPaint.setColor(Color.BLACK);
        spider.drawConstraints = new Composite.DrawInterface() {
            @Override
            public void draw(Canvas canvas, Composite composite) {
                spederPaint.setStyle(Paint.Style.FILL);

                canvas.drawCircle((float) spider.head.pos.x, (float) spider.head.pos.y, 4, spederPaint);
                canvas.drawCircle((float) spider.thorax.pos.x, (float) spider.thorax.pos.y, 4, spederPaint);
                canvas.drawCircle((float) spider.abdomen.pos.x, (float) spider.abdomen.pos.y, 8, spederPaint);

                spederPaint.setStyle(Paint.Style.STROKE);
                for (int i = 3; i < composite.constraints.size(); ++i) {
                    Constraint constraint = composite.constraints.get(i);
                    if (constraint instanceof DistanceConstraint) {
//                        // draw legs
                        if (
                                (i >= 2 && i <= 4)
                                        || (i >= (2 * 9) + 1 && i <= (2 * 9) + 2)
                                        || (i >= (2 * 17) + 1 && i <= (2 * 17) + 2)
                                        || (i >= (2 * 25) + 1 && i <= (2 * 25) + 2)
                                ) {

                            spederPaint.setStrokeWidth(3);
                        } else if (
                                (i >= 4 && i <= 6)
                                        || (i >= (2 * 9) + 3 && i <= (2 * 9) + 4)
                                        || (i >= (2 * 17) + 3 && i <= (2 * 17) + 4)
                                        || (i >= (2 * 25) + 3 && i <= (2 * 25) + 4)
                                ) {
                            spederPaint.setStrokeWidth(2);
                        } else if (
                                (i >= 6 && i <= 8)
                                        || (i >= (2 * 9) + 5 && i <= (2 * 9) + 6)
                                        || (i >= (2 * 17) + 5 && i <= (2 * 17) + 6)
                                        || (i >= (2 * 25) + 5 && i <= (2 * 25) + 6)
                                ) {
                            spederPaint.setStrokeWidth(1.5f);
                        } else {
                            spederPaint.setStrokeWidth(1f);
                        }

                        DistanceConstraint distanceConstraint = (DistanceConstraint) constraint;
                        canvas.drawLine(
                                (float) distanceConstraint.a.pos.x, (float) distanceConstraint.a.pos.y,
                                (float) distanceConstraint.b.pos.x, (float) distanceConstraint.b.pos.y, spederPaint);
                    }
                }
            }
        };

        spider.drawParticles = new Composite.DrawInterface() {
            @Override
            public void draw(Canvas canvas, Composite composite) {
            }
        };

        final int[] legIndex = {0};

        verletView.setOnFrameListener(new VerletView.OnFrameListener() {
            @Override
            public void onFrame() {
                if (Math.floor(Math.random() * 4) == 0) {
                    crawl(spiderweb, spider, ((legIndex[0]++) * 3) % 8);
                }
            }
        });
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