package com.example.lenovo.examdemo.Api;

import com.example.lenovo.examdemo.Bean.QuestionBean;
import com.example.lenovo.examdemo.Bean.RequestAnswerBean;
import com.example.lenovo.examdemo.Bean.ResResult;
import com.example.lenovo.examdemo.Bean.ResponseBean;
import com.example.lenovo.examdemo.Bean.ResponseQuestionBean;
import com.example.lenovo.examdemo.Bean.TimeBean;
import com.example.lenovo.examdemo.Bean.TokenBean;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RequestApi {

    @POST("user/sendsms")
    @FormUrlEncoded
    Call<ResResult> sendsms(@Field("phone") String phone);

    @POST("user/validatetoken")
    @FormUrlEncoded
    Call<ResResult> token(@Field("code") String code);

    @POST("user/login")
    @FormUrlEncoded
    Observable<ResponseBean<TokenBean>> login(@Field("param") String param);

    @POST("user/registe")
    @FormUrlEncoded
    Observable<ResponseBean<TokenBean>> registe(@Field("param") String param);

    @POST("user/getTime")
    @FormUrlEncoded
    Observable<ResponseBean<TimeBean>> getTime(@Field("param") String param);

    @POST("getQuestion")
    @FormUrlEncoded
    Observable<ResponseBean<ResponseQuestionBean>> getQuestion( @Field("param") String param,@Field("token") String token);

    @POST("upload")
    @FormUrlEncoded
    Observable<ResponseBean<TokenBean>> upload(@Field("token") String token, @Field("param") String param);
}
