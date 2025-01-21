package com.example.acgforum.ReturnFormat;

import java.time.LocalDateTime;

public class ReturnSimplePost {
    private Integer id;
    private String title;
    private LocalDateTime date;
    private Integer like;
    private Integer commentsNum;

    public ReturnSimplePost() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public Integer getCommentsNum() {
        return commentsNum;
    }

    public void setCommentsNum(Integer commentsNum) {
        this.commentsNum = commentsNum;
    }

    public ReturnSimplePost(Integer id, String title, LocalDateTime date, Integer like, Integer commentsNum) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.like = like;
        this.commentsNum = commentsNum;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
