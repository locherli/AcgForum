package com.example.acgforum.ReturnFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnUser {
    private Boolean token;
    private Integer id;
    private String name;
    private String avatarUrl;
    private String selfIntro;
    private Integer fanNum;
    private Integer subscribeNum;

    public ReturnUser(Boolean token) {
        this.token = token;
    }
}
