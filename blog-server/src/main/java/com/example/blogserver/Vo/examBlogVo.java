package com.example.blogserver.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class examBlogVo {
    private Long blogId;

    private String title;

    private String  nickname;

    private String content;

    private String typeName;

    private String copyright;

    private LocalDateTime createTime;

    private  String first_picture;

    private  String  description;

    private  Long timeStamp;
}
