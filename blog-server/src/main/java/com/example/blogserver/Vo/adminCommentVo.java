package com.example.blogserver.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class adminCommentVo {

    private Long blogId;
    private Long commentId;
    private Long uid;
    private String nickname;
    private String content;
    private LocalDateTime createTime;
}
