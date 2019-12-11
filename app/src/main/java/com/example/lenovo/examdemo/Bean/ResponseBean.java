package com.example.lenovo.examdemo.Bean;

public class ResponseBean<T> {

    /**
     * result : 0
     * errMsg : OK
     * data : {"token":"eyJhbGciOiJIUzUxMiJ9.eyJtb2RlIjoicGFzc3dvcmQiLCJzdWIiOiIxODk0NjEwNDAzMiIsImlzcyI6ImN1aSIsImV4cCI6MTU2NDkyNzk2NSwiaWF0IjoxNTY0MzIzMTY1LCJyb2wiOiJQQSJ9.yqTdkKjGzca18Sid3pcIjy1yavEgyiqA6PfaG8Hhu-23wp1UKzIVyA8lVDmSxixVniOIWa_VIIyiZvfkfLIQLg"}
     */

    private int result;
    private String errMsg;
    private T data;

    public String toString() {
        return "ResponseBean [result=" + result + ", errMsg=" + errMsg + ", data=" + data + "]";
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


}
