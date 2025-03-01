package com.example.acgforum.ReturnFormat;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnPost {
    private Integer id;
    private String message;
    private Integer authorId;
    private String authorName;
    private String title;
    private LocalDateTime date;
    private String content;
    private Integer like;
    private Integer commentsNum;
    private ArrayList<Integer> CommentsId;

}
