package com.example.acgforum.ReturnFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReturnForum {
    private Integer forumId;
    private String forumName;
    private String introduction;
    private Integer userNum;
    private String icon;
}
