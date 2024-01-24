package com.example.blogserver.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class selectAllCommentsVo {
    private  Long blogId;
    private String title;
    private String nickname;
    private int commentCounts;
    private LocalDateTime createTime;
}
