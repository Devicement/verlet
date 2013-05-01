#include <stdio.h>
#include <string.h>
#include <jni.h>
#include <android/log.h>

JNIEXPORT jobject JNICALL Java_org_verlet_NativeVector_vectorSub(JNIEnv *env, jobject callingObject, jobject a, jobject b) {
    jclass vec2class = (*env)->FindClass(env, "org/verlet/Vec2");
    jmethodID getXMethod = (*env)->GetMethodID(env, vec2class, "getX", "()D");
    jmethodID getYMethod = (*env)->GetMethodID(env, vec2class, "getY", "()D");
    double x1 =  (*env)->CallDoubleMethod(env, a, getXMethod);
    double x2 =  (*env)->CallDoubleMethod(env, b, getXMethod);
    double y1 =  (*env)->CallDoubleMethod(env, a, getYMethod);
    double y2 =  (*env)->CallDoubleMethod(env, b, getYMethod);

    jmethodID midConstructor = (*env)->GetMethodID(env, vec2class, "<init>", "(DD)V");
    jobject employeeObject = (*env)->NewObject(env, vec2class, midConstructor, x1-x2, y1-y2);
    return employeeObject;
}
