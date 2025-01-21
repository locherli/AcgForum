package com.example.acgforum.tables;

public class User {
    private Integer id;
    private String userName;
    private String email;
    private String selfIntro;
    private String avatarUrl;
    private Integer phoneNum;
    private Integer hc_password;
    private Boolean gender;
    private Integer age;
    private Integer fanNum;
    private Integer subscribeNum;



    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", selfIntro='" + selfIntro + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", phoneNum=" + phoneNum +
                ", hc_password=" + hc_password +
                ", gender=" + gender +
                ", age=" + age +
                ", fanNum=" + fanNum +
                ", subscribeNum=" + subscribeNum +
                '}';
    }

    public User(Integer id, String userName, String email, String selfIntro, Integer phoneNum, Integer hc_password, Boolean gender, Integer age, Integer fanNum, Integer subscribeNum) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.selfIntro = selfIntro;
        this.phoneNum = phoneNum;
        this.hc_password = hc_password;
        this.gender = gender;
        this.age = age;
        this.fanNum = fanNum;
        this.subscribeNum = subscribeNum;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSelfIntro() {
        return selfIntro;
    }

    public void setSelfIntro(String selfIntro) {
        this.selfIntro = selfIntro;
    }

    public Integer getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(Integer phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Integer getHc_password() {
        return hc_password;
    }

    public void setHc_password(Integer hc_password) {
        this.hc_password = hc_password;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getFanNum() {
        return fanNum;
    }

    public void setFanNum(Integer fanNum) {
        this.fanNum = fanNum;
    }

    public Integer getSubscribeNum() {
        return subscribeNum;
    }

    public void setSubscribeNum(Integer subscribeNum) {
        this.subscribeNum = subscribeNum;
    }
}
