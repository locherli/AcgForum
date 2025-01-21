package com.example.acgforum.ReturnFormat;

import lombok.Data;

@Data
public class ReturnUser {
    private Boolean token;
    private Integer id;
    private String name;
    private String avatarUrl;
    private String selfIntro;
    private Integer fanNum;
    private Integer subscribeNum;

    public ReturnUser() {
    }

    public ReturnUser(Boolean token) {
        this.token = token;
    }

    public ReturnUser(Boolean token, Integer id, String name, String avatarUrl, String selfIntro, Integer fanNum, Integer subscribeNum) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.selfIntro = selfIntro;
        this.fanNum = fanNum;
        this.subscribeNum = subscribeNum;
    }

    public ReturnUser(Integer id, String name, String avatarUrl, String selfIntro, Integer fanNum, Integer subscribeNum) {
        this.id = id;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.selfIntro = selfIntro;
        this.fanNum = fanNum;
        this.subscribeNum = subscribeNum;
    }

    @Override
    public String toString() {
        return "ReturnUser{" +
                "token=" + token +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", selfIntro='" + selfIntro + '\'' +
                ", fanNum=" + fanNum +
                ", subscribeNum=" + subscribeNum +
                '}';
    }

    public Boolean getToken() {
        return token;
    }

    public void setToken(Boolean token) {
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getSelfIntro() {
        return selfIntro;
    }

    public void setSelfIntro(String selfIntro) {
        this.selfIntro = selfIntro;
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
