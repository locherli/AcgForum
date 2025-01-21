package com.example.acgforum.ReturnFormat;

import java.time.LocalDateTime;

public class ReturnComment {
    private Integer id;
    private Integer author;
    private String content;
    private LocalDateTime date;
    private Integer like;
    private Integer commentsNum;

    public ReturnComment(){
    }
    
    public ReturnComment(Integer id, Integer author, String content, LocalDateTime date, Integer like,
            Integer commentsNum) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.date = date;
        this.like = like;
        this.commentsNum = commentsNum;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getAuthor() {
        return author;
    }
    public void setAuthor(Integer author) {
        this.author = author;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
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

    
}
