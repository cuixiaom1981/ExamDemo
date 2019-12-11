package com.example.lenovo.examdemo.Bean;

/**
 * Created by lenovo on 2019/10/21.
 * 获取试题输入实体
 */

public class StuBean {
    private  String stuId;

    public StuBean(String stuId) {
        this.stuId = stuId;
    }

    public String getStuId() {

        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }
}
