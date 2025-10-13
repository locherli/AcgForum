package com.example.acgforum.ReturnFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnUser {
    private Integer id;
    private String name;
    private String avatarUrl;
    private String selfIntro;
    private Integer fanNum;
    private Integer subscribeNum;
    private String phoneNum;
    private Integer age;
}
