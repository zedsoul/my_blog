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
    private Boolean published;
    private LocalDateTime updateTime;
    private String title;
}
