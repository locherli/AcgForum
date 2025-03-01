package com.example.acgforum.tables;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
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
    private Integer forum;

}
