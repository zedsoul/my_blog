package com.example.blogserver.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryVo {
    private  Long id;
    private  String  nickname;
    private  Long blogId;
    private String title;
    private String operation;
    private LocalDateTime createTime;
}
