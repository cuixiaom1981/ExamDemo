package com.example.lenovo.examdemo.Api;

import com.example.lenovo.examdemo.Bean.ResResult;
import com.example.lenovo.examdemo.Bean.ResponseBean;
import com.example.lenovo.examdemo.Bean.ResponseGradeBean;
import com.example.lenovo.examdemo.Bean.ResponsePaperBean;
import com.example.lenovo.examdemo.Bean.ResponseQuestionBean;
import com.example.lenovo.examdemo.Bean.TimeBean;
import com.example.lenovo.examdemo.Bean.TokenBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RequestApi {

    @POST("user/sendsms")
    @FormUrlEncoded
    Observable<ResponseBean<TokenBean>> sendsms(@Field("phone") String phone);

    @POST("user/login")
    @FormUrlEncoded
    Observable<ResponseBean<TokenBean>> login(@Field("param") String param);

    @POST("user/registe")
    @FormUrlEncoded
    Observable<ResponseBean<TokenBean>> registe(@Field("param") String param);

    @POST("upload")
    @FormUrlEncoded
    Observable<ResponseBean> upload(@Field("param") String param);

    @GET ("getTime/{examName}/{stuId}")
    Observable<ResponseBean<TimeBean>> getTime(@Path("examName") String examName, @Path("stuId") String stuId);

    @GET ("getGradeByStuId/{stuId}")
    Observable<ResponseBean<List<ResponseGradeBean>>> getGrade(@Path("stuId") String stuId);

    @GET ("getPaperByExam/{examName}")
    Observable<ResponseBean<ResponsePaperBean>> getPaper(@Path("examName") String examName);
}
