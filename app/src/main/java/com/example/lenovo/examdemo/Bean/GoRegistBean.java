package com.example.lenovo.examdemo.Bean;

/**
 * Created by lenovo on 2019/10/15.
 * 注册输入实体
 */

public class GoRegistBean {
    private String stuId;      //学号
    private String phone;     //手机号
    private String password;  //登录密码

    public String getStuid() {
        return stuId;
    }

    public void setStuid(String stuId) {
        this.stuId = stuId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public GoRegistBean(String phone, String stuId, String password) {
        super();
        this.phone = phone;
        this.stuId = stuId;
        this.password = password;
    }

    public static  GoRegistBean registBean(String phone,String stuId, String password) {
        return new GoRegistBean(phone, stuId, password);
    }
}
