package com.example.acgforum.tables;

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
    private Integer phoneNum;
    private Integer hc_password;
    private Boolean gender;
    private Integer age;
    private Integer fanNum;
    private Integer subscribeNum;

}
