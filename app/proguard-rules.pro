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

#okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

#okio
-dontwarn okio.**
-keep class okio.**{*;}

#okgo
-dontwarn com.lzy.okgo.**
-keep class com.lzy.okgo.**{*;}

#okrx
-dontwarn com.lzy.okrx.**
-keep class com.lzy.okrx.**{*;}

#okrx2
-dontwarn com.lzy.okrx2.**
-keep class com.lzy.okrx2.**{*;}

#okserver
-dontwarn com.lzy.okserver.**
-keep class com.lzy.okserver.**{*;}

#Apache不混淆
-dontwarn org.apache.**
-keep class org.apache.http.**{*;}

-dontwarn com.sc.clgg.bean.**
-keep class com.sc.clgg.bean.**{*;}

-dontwarn com.lvke.tools.widget.**
-keep class com.lvke.tools.widget.**{*;}

-dontwarn com.clgg.**
-keep class com.clgg.**{*;}

#----------高德地图----------
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}

#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#-keep class com.autonavi.custom.CustomRendererHelper.**{*;}
#-keep class com.autonavi.custom.CustomRendererHelper.**{*;}

#搜索
-keep   class com.amap.api.services.**{*;}

#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}

#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}
#----------高德地图----------


#----------butterknife----------
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
#----------butterknife----------


#----------glide----------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
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
#----------retrofit----------

#----------kotlinx----------
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
#----------kotlinx----------


