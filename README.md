# Is Your App Ready To Live?

This repo is created to mention security measures before making your application live

## Table of Content

1) SSL Pinning
2) Native code for securing keys and implementation
3) Root Access
4) Obfuscation
5) Encryption of Local Storage
6) No use of implicit broadcast
7) No hard coded strings
8) Unused Permission
9) Stack trace
10) Improper Content Provider Permissions 
11) MediaProjection: ( Screenshots and screen protecting)
12) Unprotected Exported Service


## 1) SSL Pinning 

SSL Pinning is used to prevent MITM. 


What is an MITM attack?

Man in the Middle, abbreviated as (MITM), is where the attacker tries to intercept the communication between Client and Server. It gives the attacker full control of the sensitive data which is being passed and to manipulate it in anyway they want. In this attack the sender and receiver are unaware that they are being monitored or their session is being intercepted by a third person. This attack is also referred as session high-jacking



[Pinned Certificate in Android](https://androidsecurity.info/certificate-pinning-in-android/)

[Get sha256 key for your server](https://www.ssllabs.com/ssltest/)

Check if pinned certificate is working ? 

[Configure Burp Suit](https://support.portswigger.net/customer/portal/articles/1841101-configuring-an-android-device-to-work-with-burp)


## 2) Native Code to hide hard coded keys

[Basic Setup NDK](https://medium.com/@abhi007tyagi/storing-api-keys-using-android-ndk-6abb0adcadad)

Getting 2D array keys in NDK( in C):

```

jobjectArray processData(JNIEnv *env,int column,char *data[])
{
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
Java_com_scottyab_rootbeer_RootBeer_getSuPaths(JNIEnv *env, jobject instance) {

   
    char *suPaths[] ={
            "/data/local/",
            "/data/local/bin/",
            "/data/local/xbin/",
            "/sbin/",
            "/su/bin/",
            "/system/bin/",
            "/system/bin/.ext/",
            "/system/bin/failsafe/",
            "/system/sd/xbin/",
            "/system/usr/we-need-root/",
            "/system/xbin/"
    };
    return (processData(env,sizeof(suPaths)/ sizeof(char*),suPaths));
}
```

## 3) Root Access

Root Access, is when a device user has access to unlimited control over device. Its file system, OS (Roms), themes, systems apps etc. Normally, devices are locked by OEMs and Carriers depending on the features they want to cater to the consumers. This may cause device to perform below par when its on-board specifications allows it to do more. Custom Roms for more features and improved battery life is one of the most common reason to root. But it gives a serious headache to app developers if the app is using features like database or file storage.

Generally, app’s internal data directory cannot be accessed on devices (unless its debug version, which can be accessed from Android Studio/DDMS tools for testing purposes). This is the directory where the app’s critical information like database and preferences files are saved.

[Root Detection Android Library](https://github.com/scottyab/rootbeer)


## 4) Obfuscation

The purpose of obfuscation is to reduce your app size by shortening the names of your app’s classes, methods, and fields. If your code relies on predictable naming for your app’s methods and classes—when using reflection, for example, you should treat those signatures as entry points and specify keep rules for them.

[More Details on Obfuscation](https://developer.android.com/studio/build/shrink-code#obfuscate)

Sample Proguard Fle:
```
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

## Square Picasso specific rules ##
-dontwarn com.squareup.okhttp.**

# Retrofit
-dontwarn javax.naming.**
-dontwarn retrofit.appengine.**
-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }
-keep class retrofit.** { *; }
-dontwarn okio.**
-dontwarn com.squareup.okhttp.*
-dontwarn retrofit.appengine.UrlFetchClient
-keepattributes Annotation
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# support v4
-dontwarn android.support.v7.**
-keep class android.support.v7.** { *; }
-keep interface android.support.v7.** { *; }

# square_OkHttp
-keepattributes Signature
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn com.squareup.okhttp.**

# square-Okio
-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-ignorewarnings
-renamesourcefileattribute SourceFile
-keepattributes SourceFile,LineNumberTable,*Annotation*,Signature

# keep classes called from android framework
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.Fragment
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class com.android.vending.licensing.ILicensingService

-keep class com.google.analytics.tracking.android.Tracker

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keep public class android.**
-keep public class android.** { *; }
-keep public class android.content.res.Resources.Theme
-keep public class android.content.res.Resources.Theme { *; }
-keep public class android.content.res.Resources
-keep public class android.content.res.Resources { *; }
-keep public class com.google.** { *; }
-keep public class com.google.**
-keep public class android.support.**
-keep public interface android.support.**

-keepclasseswithmembers class * { native <methods>; }
-keepclasseswithmembers class * {
    public <init> (android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init> (android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * implements android.os.Parcelable { static android.os.Parcelable$Creator *; }
-keepclassmembers class **.R$* { public static <fields>; }
-keepclasseswithmembernames class * { native <methods>; }

-dontwarn com.google.android.maps.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.CheckReturnValue
-dontwarn javax.annotation.concurrent.GuardedBy
-dontwarn javax.annotation.ParametersAreNonnullByDefault
-dontwarn sun.misc.Unsafe
-dontwarn javax.annotation.CheckForNull
-ignorewarnings

# Play Services
-keep public class com.google.android.gms.ads.** {public *;}
-keep public class com.google.ads.** {public *;}

-keep class com.crashlytics.android.**

# retrofit 2 rules
# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

# looks like this is only required for proguard-android-optimize
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-dontwarn org.apache.commons.logging.**

# Android 6.0 release removes support for the Apache HTTP client
-dontwarn org.apache.http.**


# Remove Logging
-assumenosideeffects class android.util.Log {
   public static *** e(...);
   public static *** w(...);
   public static *** wtf(...);
   public static *** d(...);
   public static *** v(...);
   public static *** i(...);
}

# for logback https://github.com/tony19/logback-android/wiki
-keep class ch.qos.** { *; }
-keep class org.slf4j.** { *; }
-keepattributes *Annotation*

#keeping exceptions
# added for rx 1
-keepnames class rx.exceptions.** extends java.lang.Throwable { *; }
# added for rx 2
-keepnames class io.reactivex.exceptions.** extends java.lang.Throwable { *; }

-keepattributes Exceptions, Signature, InnerClasses, LineNumberTable
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**

-keep class com.squareup.okhttp.* { *; }
-dontwarn com.squareup.okhttp.**
-dontwarn okio.**

-keep class okhttp3.** { *; }
-dontwarn okhttp3.**
-dontwarn okio.**

-keep class retrofit2.** { *; }
-dontwarn retrofit2.**

-keepattributes Signature
-keepattributes InnerClasses

-dontwarn retrofit.**
-dontwarn okio.**
-dontwarn com.squareup.okhttp.**

-keepattributes *Annotation*, Signature, Exception

-keep class com.github.mikephil.charting.** { *; }

#Chuck Library
-keep class com.readystatesoftware.chuck.internal.data.HttpTransaction { *; }


# Keep custom model classes
-keep class com.google.firebase.example.fireeats.model.** { *; }

#-keep class crystalapps.net.mint.** { *; }
#-keep interface crystalapps.net.mint.** { *; }
#-keepclasseswithmembers class * {
#    @crystalapps.net.mint* <methods>;
#}

# keep everything in this package from being removed or renamed
-keep class crystalapps.** { *; }

# keep everything in this package from being renamed only
-keepnames class crystalapps.** { *; }
-dontshrink


```

## 5) Encryption of Local Storage through KeyStore

The Android Keystore system lets you store cryptographic keys in a container to make it more difficult to extract from the device. Once keys are in the keystore, they can be used for cryptographic operations with the key material remaining non-exportable. Moreover, it offers facilities to restrict when and how keys can be used, such as requiring user authentication for key use or restricting keys to be used only in certain cryptographic modes. 

[Encryption Helper Library](https://github.com/talhahasanzia/android-encryption-helper)


