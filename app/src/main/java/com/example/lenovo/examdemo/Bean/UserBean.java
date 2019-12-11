package com.example.lenovo.examdemo.Bean;

import java.io.Serializable;

/**
 * Created by Sky Lee on 2019/8/4.
 */

public class UserBean implements Serializable {
    private static final long serialVersionUID = -3639664550080391492L;
    private long id;
    private String phone;
    private String password;
    private String name;
    private int age;
    private String sex;
    private String province;
    private String city;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
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
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getSex() {
        return sex;
    }
    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    public UserBean(long id, String phone, String password, String name, int age, String sex, String province,
                String city) {
        super();
        this.id = id;
        this.phone = phone;
        this.password = password;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.province = province;
        this.city = city;
    }
    public static UserBean resetPasswordBean(String phone, String password) {
        return new UserBean(0,phone, password, "",0, "","","");
    }

}
