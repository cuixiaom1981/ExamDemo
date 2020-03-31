package com.example.lenovo.examdemo.Utils;

import com.example.lenovo.examdemo.Api.RequestApi;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitManager {
    public static String BASE_URL = "http://172.81.253.56:8090/hmu/";
    private static RetrofitManager retrofitManager;
    private Retrofit retrofit;
    //token换成获取本地token的方法
//    private String token="eyJhbGciOiJIUzUxMiJ9.eyJtb2RlIjoicGFzc3dvcmQiLCJzdWIiOiIxODk0NjEwNDAzMiIsImlzcyI6ImN1aSIsImV4cCI6MTU3ODczNTQxMiwiaWF0IjoxNTc4MTMwNjEyLCJyb2wiOiJzdHUifQ.72YEZHT0seJAketMd6fuAYXkNJ5D27veMASGc9UljgEg58Y9hfUfHaz4HfYRH9YBMuUAOg56Iv_9dSUlr_o4WA";
    public static synchronized RetrofitManager getInstance(){//synchronized线程锁
//        //单例
        if (retrofitManager == null){
            retrofitManager = new RetrofitManager();
        }
        return retrofitManager;
    }
    private RetrofitManager(){
        initRetrofit();
    }

    public RequestApi getRetrofit() {

        return retrofit.create(RequestApi.class);
    }


    public Retrofit initRetrofit(){
        //http设置，可添加拦截器是实现http参数统一配置
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
         .connectTimeout(3000, TimeUnit.SECONDS)//连接 超时时间
         .writeTimeout(3000,TimeUnit.SECONDS)//写操作 超时时间
         .readTimeout(3000,TimeUnit.SECONDS)//读操作 超时时间
         .retryOnConnectionFailure(false)//错误不重连
        .addInterceptor(tokenInterceptor);//添加token拦截器
         retrofit = new Retrofit.Builder()
                .client(builder.build())
                 .baseUrl(BASE_URL)
                //添加rxjava支持
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
         return retrofit;
    }
    Interceptor tokenInterceptor = new Interceptor() {  //全局拦截器，
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();//获取原始请求
            Request.Builder requestBuilder = originalRequest.newBuilder() //建立新的请求
                    //token换成获取本地token的方法
                    .addHeader("Authorization", ConstantData.token)
                    .method(originalRequest.method(), originalRequest.body());
            return chain.proceed(requestBuilder.build()); //重新请求
        }};
    public <T> T createReq(Class<T> reqServer){
        return retrofit.create(reqServer);
    }
}
