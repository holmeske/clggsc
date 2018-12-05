# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}



#指定要执行的优化的数量。默认情况下，执行一个单一的传递。多次传递可能会导致进一步的改进。如果在优化过后没有发现任何改进，那么优化就结束了。只有在优化时才适用。
-optimizationpasses 5
#指定为已重命名的类和类成员打印从旧名称到新名称的映射。映射被打印到标准输出或给定文件中。例如，对于后续的模糊处理，或者如果您想再次理解混淆的堆栈跟踪，就需要它。
-printmapping proguardMapping.txt
#把混淆类中的方法名也混淆了
-useuniqueclassmembernames
#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification
# 保持测试相关的代码
-dontnote junit.framework.**
-dontnote junit.runner.**
-dontwarn android.test.**
-dontwarn org.junit.**

-ignorewarnings

#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

#okio
-dontwarn okio.**
-keep class okio.**{*;}

#消除Gradlew build提示
-dontnote rx.**
-dontnote com.github.mikephil.charting.**
-dontnote kotlin.coroutines.**
-dontnote com.umeng.**
-dontnote org.jetbrains.anko.**
-dontnote uk.co.senab.photoview.**
-dontnote org.greenrobot.eventbus.**
-dontnote javax.**
-dontnote android.webkit.**
-dontnote com.amap.api.**
-dontnote com.bumptech.glide.**
-dontnote com.contrarywind.view.**
-dontnote com.google.android.material.**
-dontnote com.loc.**
-dontnote im.yixin.sdk.api.**
-dontnote com.tencent.**
-dontnote sun.**
-dontnote com.android.**
-dontnote android.**
-dontnote kotlin.internal.**
-dontnote dalvik.system.**
-dontnote org.conscrypt.**
-dontnote com.lzy.ninegrid.**
-dontnote com.sc.clgg.widget.**
-dontnote com.sc.clgg.widget.**

-dontwarn kotlinx.coroutines.experimental.**
-dontwarn sun.misc.**
-dontwarn com.amap.api.maps2d.**
-dontwarn android.telephony.**
-dontwarn com.bumptech.glide.**
-dontwarn android.os.**
-dontwarn kotlin.internal.**
-dontwarn kotlin.reflect.**
-dontwarn com.android.**

# ServiceLoader support
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Most of volatile fields are updated with AFU and should not be mangled
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

#--takephoto---
-keep class org.devio.takephoto.** { *; }
-dontwarn org.devio.takephoto.**

-keep class com.darsh.multipleimageselect.** { *; }
-dontwarn com.darsh.multipleimageselect.**

-keep class com.soundcloud.android.crop.** { *; }
-dontwarn com.soundcloud.android.crop.**
#--takephoto---

#Apache不混淆
-dontwarn org.apache.**
-dontnote org.apache.**
-keep class org.apache.http.**{*;}

-dontwarn com.sc.clgg.bean.**
-dontnote com.sc.clgg.bean.**
-keep class com.sc.clgg.bean.**{*;}

-dontwarn com.clgg.api.**
-keep class com.clgg.api.**{*;}

#----------高德地图----------
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.**{*;}
-keep class com.amap.api.trace.**{*;}

-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

-keep   class com.amap.api.services.**{*;}

-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}

-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}
#----------高德地图----------

#----------glide----------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#----------glide----------


#----------banner----------
# banner 的混淆代码
-keep class com.youth.banner.** {*;}
#----------banner----------


#----------Bugly----------
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
#----------Bugly----------


#----------eventbus----------
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
#----------eventbus----------

#----------retrofit----------
-dontwarn okio.**
-dontwarn javax.annotation.**

-dontwarn org.codehaus.mojo.animal_sniffer.**
-keep class org.codehaus.mojo.animal_sniffer.** {*;}

# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain service method parameters.
-keepclassmembernames,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}
# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
#----------retrofit----------



#----------kotlinx----------
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
#----------kotlinx----------

#----------沉浸式状态栏----------
-keep class com.gyf.barlibrary.* {*;}

-keep public class android.support.design.widget.TabLayout {*;}


-dontwarn com.yanzhenjie.permission.**


#----------umeng----------
-dontshrink
-dontoptimize
-dontwarn com.google.android.maps.**
-dontwarn android.webkit.WebView
-dontwarn com.umeng.**
-dontwarn com.tencent.weibo.sdk.**
-dontwarn com.facebook.**
-keep public class javax.**
-keep public class android.webkit.**
-dontwarn android.support.v4.**
-keep enum com.facebook.**
-keepattributes Exceptions,InnerClasses,Signature
-keepattributes *Annotation*
-keep @**annotation** class * {*;}
-keepattributes SourceFile,LineNumberTable

-keep public interface com.facebook.**
-keep public interface com.tencent.**
-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**

-keep public class com.umeng.socialize.* {*;}


-keep class com.facebook.**
-keep class com.facebook.** { *; }
-keep class com.umeng.scrshot.**
-keep public class com.tencent.** {*;}
-keep class com.umeng.socialize.sensor.**
-keep class com.umeng.socialize.handler.**
-keep class com.umeng.socialize.handler.*
-keep class com.umeng.weixin.handler.**
-keep class com.umeng.weixin.handler.*
-keep class com.umeng.qq.handler.**
-keep class com.umeng.qq.handler.*
-keep class UMMoreHandler{*;}
-keep class com.tencent.mm.sdk.modelmsg.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.modelmsg.** implements com.tencent.mm.sdk.modelmsg.WXMediaMessage$IMediaObject {*;}
-keep class im.yixin.sdk.api.YXMessage {*;}
-keep class im.yixin.sdk.api.** implements im.yixin.sdk.api.YXMessage$YXMessageData{*;}
-keep class com.tencent.mm.sdk.** {
   *;
}
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}
-dontwarn twitter4j.**
-keep class twitter4j.** { *; }

-keep class com.tencent.** {*;}
-dontwarn com.tencent.**
-keep class com.kakao.** {*;}
-dontwarn com.kakao.**
-keep public class com.umeng.com.umeng.soexample.R$*{
    public static final int *;
}
-keep public class com.linkedin.android.mobilesdk.R$*{
    public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}
-keep class com.umeng.socialize.impl.ImageImpl {*;}
-keep class com.sina.** {*;}
-dontwarn com.sina.**
-keep class  com.alipay.share.sdk.** {
   *;
}

-keepnames class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keep class com.linkedin.** { *; }
-keep class com.android.dingtalk.share.ddsharemodule.** { *; }
-keepattributes Signature
#----------umeng----------

#----------etc蓝牙sdk----------
-keep class etc.obu.** {*;}
-keep class android.cert.** {*;}
-keep class android.recharge.** {*;}
-keep class genvict.bluetooth.manage.** {*;}
-keep class google.protobuf.** {*;}
-keep class protobufBle.protobuf.** {*;}
#----------etc蓝牙sdk----------