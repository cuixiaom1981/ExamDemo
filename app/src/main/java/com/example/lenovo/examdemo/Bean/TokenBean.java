package com.example.lenovo.examdemo.Bean;

public class TokenBean {
    private String token;
    private boolean isNew;
    private String stuId;

    @Override
    public String toString() {
        return "TokenBean{" +
                "token='" + token + '\'' +
                ", isNew=" + isNew +
                ", stuId='" + stuId + '\'' +
                '}';
    }

    public TokenBean(String token, boolean isNew, String stuId) {
        this.token = token;
        this.isNew = isNew;
        this.stuId = stuId;
    }

    public String getToken() {

        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }
}
