package com.sc.clgg.http;

import android.app.Application;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.sc.clgg.tool.helper.LogHelper;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;


/**
 * 作者：lvke
 * 创建时间：2017/6/27
 */

public class HttpManager {

    public static void init(Application application) {

        //1. 构建OkHttpClient.Builder
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //2. 配置log
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);//log打印级别，决定了log显示的详细程度
        loggingInterceptor.setColorLevel(Level.INFO);//log颜色级别，决定了log在控制台显示的颜色
        builder.addInterceptor(loggingInterceptor);
        //builder.addInterceptor(new ChuckInterceptor(this));//第三方的开源库，使用通知显示当前请求的log

        //3. 配置超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);//全局的读取超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);//全局的写入超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);//全局的连接超时时间

        //4. 配置Cookie，以下几种任选其一就行
        //builder.cookieJar(new CookieJarImpl(new SPCookieStore(application)));//使用sp保持cookie，如果cookie不过期，则一直有效
        //builder.cookieJar(new CookieJarImpl(new DBCookieStore(application)));//使用数据库保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));//使用内存保持cookie，app退出后，cookie消失

        //5. Https配置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
        //HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
        //HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
        //builder.hostnameVerifier(new SafeHostnameVerifier());


        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        HttpHeaders headers = new HttpHeaders();
        //headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        //headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
        //params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        //params.put("commonParamsKey2", "这里支持中文参数");
//-------------------------------------------------------------------------------------//

        OkGo.getInstance().init(application)                    //必须调用初始化
                .setOkHttpClient(builder.build())               //必须设置OkHttpClient
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(0)                               //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
                .addCommonHeaders(headers)                      //全局公共头
                .addCommonParams(params);                       //全局公共参数
    }

    public void post(final String url, final Map<String, String> params, final HttpCallBack callback) {

        OkGo.<String>post(url)     // 请求方式和请求url
                .tag("okgo")                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey("cacheKey")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.NO_CACHE)    // 缓存模式，详细请看缓存介绍
                .upJson(new Gson().toJson(params))
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);

                        LogHelper.v("http...onStart()" + "\t\t" + url + "\t\t" + new Gson().toJson(params));

                        if (callback != null) {
                            callback.onStart();
                        }
                    }

                    @Override
                    public void onSuccess(Response<String> response) {

                        LogHelper.v("http...onSuccess()" + "\t\t" + url + "\t\t" + new Gson().toJson(params));
                        LogHelper.e("onSuccess()\t\t" + "url = " + url + "\t\tparams = " + new Gson().toJson(params) + "\nbody = " + response.body());

                        try {
                            if (callback != null) {
                                callback.onSuccess(response.body());
                            }
                        } catch (Exception e) {
                            LogHelper.e(e);
                        }
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {
                        super.onCacheSuccess(response);

                        LogHelper.v("http...onCacheSuccess()" + "\t\t" + url + "\t\t" + new Gson().toJson(params));
                        LogHelper.e("onCacheSuccess()\t\t" + "url = " + url + "\t\tparams = " + new Gson().toJson(params) + "\nbody = " + response.body());

                        if (callback != null) {
                            callback.onCacheSuccess(response.body());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        try {
                            LogHelper.v("http...onError()" + "\t\t" + url + "\t\t" + new Gson().toJson(params));
                            LogHelper.e("onError()\t\t" + "url = " + url + "\t\tparams = " + new Gson().toJson(params) + "\nbody = " + response.body());

                            if (callback != null) {
                                callback.onError(response.body());
                            }
                        } catch (Exception e) {
                            LogHelper.e(e);
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();

                        LogHelper.v("http...onFinish()" + "\t\t" + url + "\t\t" + new Gson().toJson(params));

                        if (callback != null) {
                            callback.onFinish();
                        }
                    }

                });


    }

    public void get(final String url, final Map<String, String> params, final HttpCallBack callback) {

        OkGo.<String>get(url)     // 请求方式和请求url
                .tag(this)                       // 请求的 tag, 主要用于取消对应的请求
                .cacheKey("cacheKey")            // 设置当前请求的缓存key,建议每个不同功能的请求设置一个
                .cacheMode(CacheMode.NO_CACHE)    // 缓存模式，详细请看缓存介绍
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onStart(Request<String, ? extends Request> request) {
                        super.onStart(request);
                        LogHelper.v("http...onStart()" + "\t\t" + url + "\t\t" + new Gson().toJson(params));
                        if (callback != null) {
                            callback.onStart();
                        }
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            LogHelper.v("http...onSuccess()" + "\t\t" + url + "\t\t" + new Gson().toJson(params));
                            LogHelper.e("onSuccess()\t\t" + "url = " + url + "\t\tparams = " + new Gson().toJson(params) + "\nbody = " + response.body());

                            if (callback != null) {
                                callback.onSuccess(response.body());
                            }
                        } catch (Exception e) {
                            LogHelper.e(e);
                        }
                    }

                    @Override
                    public void onCacheSuccess(Response<String> response) {
                        super.onCacheSuccess(response);

                        LogHelper.v("http...onCacheSuccess()" + "\t\t" + url + "\t\t" + new Gson().toJson(params));
                        LogHelper.e("onCacheSuccess()\t\t" + "url = " + url + "\t\tparams = " + new Gson().toJson(params) + "\nbody = " + response.body());

                        if (callback != null) {
                            callback.onCacheSuccess(response.body());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        try {
                            LogHelper.v("http...onError()" + "\t\t" + url + "\t\t" + new Gson().toJson(params));
                            LogHelper.e("onError()\t\t" + "url = " + url + "\t\tparams = " + new Gson().toJson(params) + "\nbody = " + response.body());

                            if (callback != null) {
                                callback.onError(response.body());
                            }
                        } catch (Exception e) {
                            LogHelper.e(e);
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();

                        LogHelper.v("http...onFinish()" + "\t\t" + url + "\t\t" + new Gson().toJson(params));

                        if (callback != null) {
                            callback.onFinish();
                        }
                    }

                });

    }
}
