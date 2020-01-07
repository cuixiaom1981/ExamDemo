package com.example.lenovo.examdemo.Bean;

import java.util.List;

/**
 * Created by lenovo on 2019/10/16.
 */

public class TimeBean {

    private long during;        //距离考试时间

    public TimeBean(long during, Boolean isSubmit) {
        this.during = during;
        this.isSubmit = isSubmit;
    }

    public long getDuring() {

        return during;
    }

    public void setDuring(long during) {
        this.during = during;
    }

    public Boolean getSubmit() {
        return isSubmit;
    }

    public void setSubmit(Boolean submit) {
        isSubmit = submit;
    }

    private Boolean isSubmit; //是否已经提交试卷


}
