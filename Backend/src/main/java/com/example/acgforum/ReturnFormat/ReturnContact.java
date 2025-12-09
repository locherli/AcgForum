package com.example.acgforum.ReturnFormat;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReturnContact {
    private Integer id;
    private String name;
    private String avatar;
    private Integer messageCount;
    private LocalDateTime latestMessage;
}
