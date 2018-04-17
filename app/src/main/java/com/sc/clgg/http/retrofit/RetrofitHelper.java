package com.sc.clgg.http.retrofit;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author：lvke
 * @date：2018/1/10 09:43
 */

public class RetrofitHelper {

    public <T> T init(String baseUrl, final Class<T> service) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(service);
    }
    //    public class HttpInterceptor implements Interceptor {
//        @Override
//        public okhttp3.Response intercept(Chain chain) throws IOException {
//            Request.Builder builder = chain.request().newBuilder();
//            Request requst = builder.addHeader("Content-type", "application/json").build();
//            return chain.proceed(requst);
//        }
//    }

    public HttpService init(String baseUrl) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit.create(HttpService.class);
    }

}
