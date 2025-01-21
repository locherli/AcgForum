package com.example.acgforum.tables;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Forum {
    private Integer id;
    private String userName;
    private String email;
    private Integer phoneNum;
    private Long hc_password;
    private char[] gender = new char[4];
    private Integer age;

}
