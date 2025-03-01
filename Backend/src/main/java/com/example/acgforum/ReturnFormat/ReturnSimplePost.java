package com.example.acgforum.ReturnFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnSimplePost {
    private Integer id;
    private String title;
    private LocalDateTime date;
    private Integer like;
    private Integer commentsNum;

}
