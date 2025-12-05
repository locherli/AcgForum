package com.example.acgforum.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private Integer id;
    private Integer sender;
    private Integer receiver;
    private String content;
    private LocalDateTime date;
    private Boolean isRead;

    //非数据库字段
    private String senderName;
    private String senderAvatar;
}