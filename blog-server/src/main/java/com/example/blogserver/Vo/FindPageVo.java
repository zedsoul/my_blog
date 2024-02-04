package com.example.blogserver.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FindPageVo {
    private Long blogId;
    private String typeName;
    private Boolean recommend;
    private Integer published;
    private LocalDateTime updateTime;
    private String title;
    private String preserve;
    private Long TimeStamp;
}
