package com.example.lenovo.examdemo.Bean;

/**
 * Created by Sky Lee on 2019/10/20.
 * 获取试题输入实体
 */

public class GoQuestionBean {
    private String token;   //验证token

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public GoQuestionBean(String token)
    {
        super();
        this.token = token;
    }
    public static GoQuestionBean getQuestionBean(String token) {
        return new GoQuestionBean(token);
    }

}
