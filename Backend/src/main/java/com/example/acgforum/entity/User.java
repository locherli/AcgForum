package com.example.acgforum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Integer id;
    private String userName;
    private String email;
    private String selfIntro;
    private String avatarUrl;
    private String phoneNum;
    private Integer hcPassword;
    private Boolean gender;
    private Integer age;
    private Integer fanNum;
    private Integer subscribeNum;

}
