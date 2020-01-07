package com.example.lenovo.examdemo.Bean;

public interface ResponseCallBack<T> {
    void onSuccess(T t);
    void onFault(String errorMsg);
    void onLogin();

}