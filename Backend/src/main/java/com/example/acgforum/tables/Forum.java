package com.example.acgforum.tables;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.aop.IntroductionAdvisor;

import javax.swing.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Forum {
    private Integer id;
    private String name;
    private String Introduction;
    private Integer owner;
    private Integer postNum;
    private Integer userNum;
    private String icon;

}
