package com.example.lenovo.examdemo.Bean;

import java.util.List;

/**
 * Created by lenovo on 2019/10/16.
 */

public class TimeBean {

    private long during;        //距离考试时间
    private List<String> department;    //科室
    private Boolean isSubmit; //是否已经提交试卷

    public TimeBean(long duration,List department) {
        this.during = duration;
        this.department=department;
    }

    public long getDuring() {
        return during;
    }

    public void setDuring(long during) {
        this.during = during;
    }

    public List<String> getDepartment() {
        return department;
    }

    public void setDepartment(List<String> department) {
        this.department = department;
    }

    public Boolean getSubmit() {
        return isSubmit;
    }

    public void setSubmit(Boolean submit) {
        isSubmit = submit;
    }
}
