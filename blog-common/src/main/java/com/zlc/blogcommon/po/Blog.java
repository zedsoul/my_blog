package com.zlc.blogcommon.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author zlc
 * @since 2023-11-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("blog")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

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
     * 点赞数
     */
    private Integer thumbs;

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

    /**
     * 浏览次数
     */
    private Integer views;

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

    private  String preserve;
}
