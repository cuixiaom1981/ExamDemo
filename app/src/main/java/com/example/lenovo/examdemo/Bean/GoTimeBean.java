package com.example.lenovo.examdemo.Bean;

/**
 * Created by lenovo on 2019/10/16.
 * 获取倒计时输入实体
 */

public class GoTimeBean {
    private String stuId;   //学号

    public String getStuid() {
        return stuId;
    }

    public void setStuid(String stuId) {
        this.stuId = stuId;
    }
    public GoTimeBean(String stuId) {
        super();
        this.stuId = stuId;
    }


    public static GoTimeBean getTimeBean(String stuId) {
        return new GoTimeBean(stuId);
    }
}
