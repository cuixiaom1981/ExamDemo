package com.example.lenovo.examdemo.Bean;

/**
 * Created by Sky Lee on 2019/8/3.
 * 登录输入实体
 */

public class GoLoginBean {

    private String phone;     //手机号
    private String code;      //验证码
    private String password;  //登录密码
    private String mode;       //登录模式 sms短信；password 密码

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public GoLoginBean(String phone, String code, String password, String mode) {
        super();
        this.phone = phone;
        this.code = code;
        this.password = password;
        this.mode = mode;
    }

    public static GoLoginBean passLoginBean(String phone, String password) {
        return new GoLoginBean(phone, "", password, "password");
    }

    public static GoLoginBean smsLoginBean(String phone, String code) {
        return new GoLoginBean(phone, code, "", "sms");
    }

}
