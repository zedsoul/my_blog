package com.example.blogserver.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TipVO {
    private  Long id;
    private  String  nickname;
    private  Long blogId;
    private String title;
    private String message;
    private LocalDateTime createTime;
}
