package com.example.acgforum.ReturnFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnComment {
    private Integer id;
    private Integer author;
    private String content;
    private LocalDateTime date;
    private Integer like;
    private Integer commentsNum;

    
}
