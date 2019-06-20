#include <jni.h>
#include <string.h>
#include <stdio.h>

#define  LOG_TAG    "ANDROID_JNI"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG, __VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

jobjectArray processData(JNIEnv *env, int column, char *data[]) {
    jsize arrayLen = column;
    jstring str;
    jobjectArray ret;
    int i;
    jclass classString = (*env)->FindClass(env, "java/lang/String");
    ret = (jobjectArray) (*env)->NewObjectArray(env, arrayLen, classString, NULL);
    for (i = 0; i < arrayLen; i++) {
        str = (*env)->NewStringUTF(env, data[i]);
        (*env)->SetObjectArrayElement(env, ret, i, str);

    }
    return ret;
}


JNIEXPORT jobjectArray JNICALL
Java_com_android_androidsecurity_implementation_NativeKeyHiding_getArray(JNIEnv *env, jobject instance) {

    //char *data[] = {"NULL", "NULL"};
    char *knownRootAppsPackages[] = {
            "a",
            "b",
            "c",
            "d",
            "e",
            "f",
            "g"
    };
    return (processData(env, sizeof(knownRootAppsPackages) / sizeof(char *), knownRootAppsPackages));
}

JNIEXPORT jstring JNICALL
Java_com_android_androidsecurity_implementation_NativeKeyHiding_getString(JNIEnv *env, jobject instance) {


    return (*env)->NewStringUTF(env, "abcdew");
}

