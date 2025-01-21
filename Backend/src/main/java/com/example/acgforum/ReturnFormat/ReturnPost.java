package com.example.acgforum.ReturnFormat;


import java.time.LocalDateTime;
import java.util.ArrayList;


public class ReturnPost {
    private String message;
    private Integer id;
    private Integer authorId;
    private String authorName;
    private String title;
    private LocalDateTime date;
    private String content;
    private Integer like;
    private Integer commentsNum;
    private ArrayList<Integer> CommentsId;


    
    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public ArrayList<Integer> getCommentsId() {
        return CommentsId;
    }

    public void setCommentsId(ArrayList<Integer> commentsId) {
        CommentsId = commentsId;
    }

    public ReturnPost() {
    }

    public ReturnPost(Integer id, Integer authorId, String authorName, String title, LocalDateTime date, String content,
            Integer like, Integer commentsNum, ArrayList<Integer> commentsId) {
        this.id = id;
        this.authorId = authorId;
        this.authorName = authorName;
        this.title = title;
        this.date = date;
        this.content = content;
        this.like = like;
        this.commentsNum = commentsNum;
        CommentsId = commentsId;
    }


}
