package org.verlet;

/**
 * Some basic objects representation
 */
public class Objects {
    public static Composite point(Vec2 pos) {
        Composite composite = new Composite();
        composite.particles.add(new Particle(pos));
        return composite;
    }

    public static Composite lineSegments(Vec2[] vertices, double stiffness) {
        Composite composite = new Composite();

        for (int i = 0; i < vertices.length; i++) {
            composite.particles.add(new Particle(vertices[i]));
            if (i > 0) {
                composite.constraints.add(new DistanceConstraint(composite.particles.get(i), composite.particles.get(i - 1), stiffness));
            }
        }

        return composite;
    }

    public static Composite cloth(Vec2 origin, int width, int height, int segments, int pinMod, double stiffness) {

        Composite composite = new Composite();

        double xStride = width / segments;
        double yStride = height / segments;

        for (int y = 0; y < segments; ++y) {
            for (int x = 0; x < segments; ++x) {
                double px = origin.x + x * xStride - width / 2 + xStride / 2;
                double py = origin.y + y * yStride - height / 2 + yStride / 2;
                composite.particles.add(new Particle(new Vec2(px, py)));

                if (x > 0)
                    composite.constraints.add(new DistanceConstraint(composite.particles.get(y * segments + x), composite.particles.get(y * segments + x - 1), stiffness));

                if (y > 0)
                    composite.constraints.add(new DistanceConstraint(composite.particles.get(y * segments + x), composite.particles.get((y - 1) * segments + x), stiffness));
            }
        }

        for (int x = 0; x < segments; ++x) {
            if (x % pinMod == 0) composite.pin(x);
        }

        return composite;
    }

    public static Composite tire(Vec2 origin, double radius, int segments, double spokeStiffness, double treadStiffness) {
        double stride = (2 * Math.PI) / segments;
        Composite composite = new Composite();

        // particles
        for (int i = 0; i < segments; ++i) {
            double theta = i * stride;
            composite.particles.add(new Particle(new Vec2(origin.x + Math.cos(theta) * radius, origin.y + Math.sin(theta) * radius)));
        }

        Particle center = new Particle(origin);
        composite.particles.add(center);

        // constraints
        for (int i = 0; i < segments; ++i) {
            composite.constraints.add(new HookeDistanceConstraint(composite.particles.get(i), composite.particles.get((i + 1) % segments), treadStiffness));
            composite.constraints.add(new HookeDistanceConstraint(composite.particles.get(i), center, spokeStiffness));
            composite.constraints.add(new HookeDistanceConstraint(composite.particles.get(i), composite.particles.get((i + 5) % segments), treadStiffness));
        }

        return composite;
    }

    public static Composite box(Vec2 origin, double width, double height, double stiffness) {
        Composite composite = new Composite();

        composite.particles.add(new Particle(origin.add(origin.add(new Vec2(-width / 2, -height / 2)))));
        composite.particles.add(new Particle(origin.add(origin.add(new Vec2(-width / 2, height / 2)))));
        composite.particles.add(new Particle(origin.add(origin.add(new Vec2(width / 2, -height / 2)))));
        composite.particles.add(new Particle(origin.add(origin.add(new Vec2(width / 2, height / 2)))));

        composite.constraints.add(new DistanceConstraint(composite.particles.get(0), composite.particles.get(1), stiffness));
        composite.constraints.add(new DistanceConstraint(composite.particles.get(0), composite.particles.get(2), stiffness));
        composite.constraints.add(new DistanceConstraint(composite.particles.get(0), composite.particles.get(3), stiffness));
        composite.constraints.add(new DistanceConstraint(composite.particles.get(1), composite.particles.get(2), stiffness));
        composite.constraints.add(new DistanceConstraint(composite.particles.get(1), composite.particles.get(3), stiffness));
        composite.constraints.add(new DistanceConstraint(composite.particles.get(2), composite.particles.get(3), stiffness));

        return composite;
    }
}
