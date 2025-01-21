package com.example.acgforum.tables;

import java.time.LocalDateTime;


public class Post {
    private Integer id;
    private Integer authorId;
    private Boolean isRoot;
    private Integer root;
    private String title;
    private LocalDateTime date;
    private Integer likeNum;
    private Integer commentNum;
    private String content;

    public Post() {
    }

    public Post(Integer id, Integer authorId, Boolean isRoot, Integer root, String title, LocalDateTime date, Integer likeNum, Integer commentNum, String content) {
        this.id = id;
        this.authorId = authorId;
        this.isRoot = isRoot;
        this.root = root;
        this.title = title;
        this.date = date;
        this.likeNum = likeNum;
        this.commentNum = commentNum;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", authorId=" + authorId +
                ", isRoot=" + isRoot +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", likeNum=" + likeNum +
                ", commentNum=" + commentNum +
                ", content='" + content + '\'' +
                '}';
    }

    public Boolean getIsRoot(){
        return isRoot;
    }

    public void  setIsRoot(Boolean isRoot){
        this.isRoot=isRoot;
    }

    public Integer getRoot(){
        return root;
    }

    public void setRoot(Integer root){
        this.root=root;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Integer authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRoot(Boolean root) {
        isRoot = root;
    }
}
