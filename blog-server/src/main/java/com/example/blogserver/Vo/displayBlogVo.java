package com.example.blogserver.Vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class displayBlogVo {
    private Long blogId;
    /**
     * 标题
     */
    private String title;

    private Long uid;

    private LocalDateTime createTime;

    private  String description;

    /**
     * 首图
     */
    private String firstPicture;

    //标签
    private List<String> tags;

    private Integer thumbs;

    private String typeName;    // 分类名称
    private String nickname;    //用户昵称
    private String avatar;      //用户头像

    private  Integer thumbsCounts;
}
