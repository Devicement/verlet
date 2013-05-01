package org.verlet;

/**
 * Simple two-dimensional vector implementation
 */
public class Vec2 {
    public double x, y;

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2() {
    }

    public Vec2 add(Vec2 vec) {
        return new Vec2(x + vec.x, y + vec.y);
    }

    public Vec2 sub(Vec2 vec) {
        return new Vec2(x - vec.x, y - vec.y);
    }

    public Vec2 mul(Vec2 vec) {
        return new Vec2(x * vec.x, y * vec.y);
    }

    public Vec2 div(Vec2 vec) {
        return new Vec2(x / vec.x, y / vec.y);
    }

    public Vec2 scale(double k) {
        return new Vec2(x * k, y * k);
    }

    public Vec2 mutableSet(Vec2 vec) {
        x = vec.x;
        y = vec.y;
        return this;
    }

    public Vec2 mutableAdd(Vec2 vec) {
        x += vec.x;
        y += vec.y;
        return this;
    }

    public Vec2 mutableSub(Vec2 vec) {
        x -= vec.x;
        y -= vec.y;
        return this;
    }

    public Vec2 mutableMul(Vec2 vec) {
        x *= vec.x;
        y *= vec.y;
        return this;
    }

    public Vec2 mutableDiv(Vec2 vec) {
        x /= vec.x;
        y /= vec.y;
        return this;
    }

    public Vec2 mutableScale(double k) {
        x *= k;
        y *= k;
        return this;
    }

    public boolean equals(Vec2 vec) {
        return x == vec.x && y == vec.y;
    }

    public boolean epsilonEquals(Vec2 vec, double eps) {
        return Math.abs(x - vec.x) <= eps && Math.abs(y - vec.y) <= eps;
    }

    public double length() {
        return Math.sqrt(x * x + y * y);
    }

    public double length2() {
        return x * x + y * y;
    }

    public double dist(Vec2 vec) {
        return Math.sqrt(dist2(vec));
    }

    public double dist2(Vec2 vec) {
        double x = this.x - vec.x;
        double y = this.y - vec.y;
        return x * x + y * y;
    }

    public Vec2 normal() {
        double invLen = 1.0 / length();
        return new Vec2(invLen * x, invLen * y);
    }

    public double dot(Vec2 vec) {
        return x * vec.x + y * vec.y;
    }

    public double angle(Vec2 vec) {
        return Math.atan2(x * vec.y - y * vec.x, x * vec.x + y * vec.y);
    }

    public double angle2(Vec2 left, Vec2 right) {
        return left.sub(this).angle(right.sub(this));
    }

    public Vec2 rotate(Vec2 origin, double theta) {
        double x = this.x - origin.x;
        double y = this.y - origin.y;
        return new Vec2(x*Math.cos(theta) - y*Math.sin(theta) + origin.x, x*Math.sin(theta) + y*Math.cos(theta) + origin.y);
    }

    public Vec2 mutableRotate(Vec2 origin, double ang) {
        double x = this.x - origin.x;
        double y = this.y - origin.y;
        this.x = x * Math.cos(ang) - y * Math.sin(ang) + origin.x;
        this.y = x * Math.sin(ang) + y * Math.cos(ang) + origin.y;
        return this;
    }

    protected Vec2 clone() {
        return new Vec2(x, y);
    }

    @Override
    public String toString() {
        return "Vec2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
