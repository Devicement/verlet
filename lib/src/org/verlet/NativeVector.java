package org.verlet;

/**
 * Created with IntelliJ IDEA.
 * User: i20
 * Date: 01.05.13
 * Time: 18:31
 * To change this template use File | Settings | File Templates.
 */
public class NativeVector {
    static {
        System.loadLibrary("ndk-vector");
    }

    public static native Vec2 vectorSub(Vec2 a, Vec2 b);
}
