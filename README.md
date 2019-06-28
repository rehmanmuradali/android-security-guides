# Is Your App Ready To Live?

This repo is created to mention security measures before making your application live

## Table of Content

1) SSL Pinning
2) Native code for securing keys and implementation
3) Root Access
4) Obfuscation
5) Encryption of Local Storage
6) No use of implicit broadcast
7) MediaProjection: ( Screenshots and screen protecting)
8) Android Component Hijacking via Intent
9) No Encryption Under Https
10) Add Custom Permissions to protect android components
11) Disable Stacktrace in Release Mode

## 1) SSL Pinning 

SSL Pinning is used to prevent MITM. 


What is an MITM attack?

Man in the Middle, abbreviated as (MITM), is where the attacker tries to intercept the communication between Client and Server. It gives the attacker full control of the sensitive data which is being passed and to manipulate it in anyway they want. In this attack the sender and receiver are unaware that they are being monitored or their session is being intercepted by a third person. This attack is also referred as session high-jacking

[Protect Transport Layer](https://medium.com/@ankit.sinhal/transport-layer-security-2d320b8891f2)

[Pinned Certificate in Android](https://androidsecurity.info/certificate-pinning-in-android/)

[Get sha256 key for your server](https://www.ssllabs.com/ssltest/)

Check if pinned certificate is working ? 

[Configure Burp Suit](https://support.portswigger.net/customer/portal/articles/1841101-configuring-an-android-device-to-work-with-burp)

### Additional Information

Applications should make sure that they do not send sensitive information to log output. If the app includes a third party library, the developer should make sure that the library does not send sensitive information to log output. One common solution is for an application to declare and use a custom log class, so that log output is automatically turned on/off based on Debug/Release. Developers can use
ProGuard to delete specific method calls. This assumes that the method contains no side effects.
Never use HTTP URL to download data. Instead, create a valid HTTPS request through which only sensitive data can be downloaded.

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

### Additional Information

Put your secrets in jni code, add some variable code to make your libraries bigger and more difficult to decompile. You might also split key string in few parts and keep them in various places

## 3) Root Access

Root Access, is when a device user has access to unlimited control over device. Its file system, OS (Roms), themes, systems apps etc. Normally, devices are locked by OEMs and Carriers depending on the features they want to cater to the consumers. This may cause device to perform below par when its on-board specifications allows it to do more. Custom Roms for more features and improved battery life is one of the most common reason to root. But it gives a serious headache to app developers if the app is using features like database or file storage.

Generally, app’s internal data directory cannot be accessed on devices (unless its debug version, which can be accessed from Android Studio/DDMS tools for testing purposes). This is the directory where the app’s critical information like database and preferences files are saved.

[Root Detection Android Library](https://github.com/scottyab/rootbeer)


## 4) Obfuscation

The purpose of obfuscation is to reduce your app size by shortening the names of your app’s classes, methods, and fields. If your code relies on predictable naming for your app’s methods and classes—when using reflection, for example, you should treat those signatures as entry points and specify keep rules for them. Use Proguard/Dexguard as a tool for obfuscation.

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


## 6) No use of implicit broadcast/ Use of LocalBroadcastManager

With apps targeting Android O, all implicit BroadcastReceivers registered in the manifest (except these) will stop working.

Use LocalBroadcastManager to register for and send broadcasts of Intents to local objects within your process. This has a number of advantages over sending global broadcasts with sendBroadcast(Intent):

1. You know that the data you are broadcasting won't leave your app, so don't need to worry about leaking private data.

2. It is not possible for other applications to send these broadcasts to your app, so you don't need to worry about having security holes they can exploit.

3. It is more efficient than sending a global broadcast through the system.


### Additional Protection

1. To protect broadcast, it needs to be registered with permissions when it is declare.

2. Never trust the data that is being sent with it. It's passing through a trust zone to arrive in the app, so it needs to be validated


## 7) MediaProjection: ( Screenshots and screen protecting)

Protect all sensitive windows within the App by enabling the FLAG_SECURE ﬂag. This ﬂag will prevent Apps from being able to record the protected windows. Also, the ﬂag will prevent users from taking screenshots of these windows (by pressing the VOLUME_DOWN and POWER buttons). [See for more details](https://developer.android.com/reference/android/view/WindowManager.LayoutParams) 

## 8) Android Component Hijacking via Intent

Android components ( Activity, Broadcast Receiver, Content Provider, Service App) have their own entry points and can be activated individually. These components can be exposed to other apps for flexible code and data sharing.

Android (mainly) uses Manifest XML file to define component exposure. Intentscome into play here because they are the main mechanism for communication between components. Intents are used to start activities and services, bind to services, and convey notifications to broadcast receivers. By default, a component can only receive intents from other components in the same application, but it can
be configured to accept intents from outside applications by setting the android:exported attribute in the manifest.
An intent can be classified as one of two types based on how it is addressed.

    1)  Implicit Intent
    2)  Explicit Intent

There are two main ways that the security of intents can be compromised:

    1) Intent interception involves a malicious app receiving an intent that was not intended for it. This can cause a leak of sensitive information, but more importantly, it can result in the malicious component being activated instead of the legitimate component. For example, if a malicious activity intercepted an intent then it would appear on the screen instead of the legitimate activity.

    2) Intent spoofing is an attack where a malicious application induces undesired behavior by forging an intent.
 

### Solution?

Android framework provides "PendingIntent" mechanism to safely perform the actions of an intent given by untrusted apps. In some situations, it can be a good measure for this kind of vulnerabilities.

```
// Explicit intent to wrap
Intent intent = new Intent(this, LoginActivity.class);
// Create pending intent and wrap our intent
PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
intent, PendingIntent.FLAG_CANCEL_CURRENT);
try {
 // Perform the operation associated with our pendingIntent
 pendingIntent.send();
} catch (PendingIntent.CanceledException e) {
 e.printStackTrace();
}
```


## 9) No Encryption Under Https

HTTPS avoids the eavesdropping of application traffic but internal malicious users can use specialized software to view the traffic under the layer of https and may exploit any vulnerabilities present in clear text communication under https so it is not recommended to transmit clear text under https. 

## 10) Add Custom Permissions to protect Android Components

Android components (Activity, Services, Broadcast Receivers, Content Provided) should defined permissions to restrict who can start the associated components. The permission is checked when the component is going to start. If the caller does not have the required permission then security exception is thrown for that call. 

[See how to define custom permission](https://developer.android.com/guide/topics/permissions/defining)


## 11) Disable Stacktrace in Release Mode

Stack trace will show the full trace right into the core, and will reveal details about what technologies you're using, and possible versions as well. This gives intruders valuable info on possible weaknesses that could be exploited. A stack trace can reveal

    1) What encryption algorithm you use?
    2) What some existing paths on your application server are?
    3) Whether you are properly sanitizing input or not?
    4) How your objects are referenced internally?
    5) what version and brand of database is behind your front-end?


## LICENSE
Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

## Author
**Rehman Murad Ali**
