# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
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


# keep everything in this package from being removed or renamed
-keep class crystalapps.** { *; }

# keep everything in this package from being renamed only
-keepnames class crystalapps.** { *; }
