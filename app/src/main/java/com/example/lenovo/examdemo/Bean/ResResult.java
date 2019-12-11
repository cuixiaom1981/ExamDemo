package com.example.lenovo.examdemo.Bean;

import org.json.JSONObject;

public class ResResult {

    private int result;
    private String errMsg;
    private JSONObject data;
    public ResResult(int result, String errMsg, JSONObject data) {
        super();
        this.result = result;
        this.errMsg = errMsg;
        this.data = data;
    }
    public ResResult() {

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
    public JSONObject getData() {
        return data;
    }
    public void setData(JSONObject data) {
        this.data = data;
    }
    @Override
    public String toString() {
        return "ResResult [result=" + result + ", errMsg=" + errMsg + ", data=" + data + "]";
    }
    public ResResult resultOK() {
        return new ResResult(0,"OK",null);
    }
    public ResResult resultData(JSONObject data) {
        return new ResResult(0,"OK",data);
    }
    public ResResult resultErr(int code,String  errMsg) {
        return new ResResult(code,errMsg,null);
    }


}
