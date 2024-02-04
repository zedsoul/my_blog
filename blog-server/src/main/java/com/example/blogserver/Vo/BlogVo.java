package com.example.blogserver.Vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogVo {
    /**
     * 博客id
     */
    @TableId(value = "blog_id")
    private Long blogId;

    /**
     * 标题
     */
    private String title;

    private Long uid;

    /**
     * 内容
     */
    private String content;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 首图
     */
    private String firstPicture;



    /**
     * 发布状态
     */
    private Integer published;

    /**
     * 推荐状态
     */
    private Boolean recommend;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;



    private Integer typeId;

    private String description;

    /**
     * 赞赏状态
     */
    private Boolean appreciation;

    /**
     * 评论状态
     */
    private Boolean commentAble;

    /**
     * 版权状态
     */
    private String copyright;


    private String typeName;    // 分类名称
    private String nickname;    //用户昵称
    private String avatar;      //用户头像

    //标签
    private List<String> tags;

    private String preserve;

    //浏览数
    private  Integer views;
    //点赞数
    private  Integer thumbs;
    //收藏数
    private  Integer favorite;
    //评论数
    private  Integer comments;
    //是否点赞
    private  Boolean isThumbs;
    //是否收藏
    private Boolean isFavorite;


}
