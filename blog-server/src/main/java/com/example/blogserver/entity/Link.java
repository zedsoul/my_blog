package com.example.blogserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author zlc
 * @since 2023-12-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("link")
public class Link implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 友链id
     */
    @TableId(value = "link_id", type = IdType.NONE)
    private Long linkId;

    /**
     * 友链名称
     */
    private String linkName;

    /**
     * 友链头像地址
     */
    private String avatarLink;

    /**
     * 友链地址
     */
    private String blogLink;

    /**
     * 友链博客描述
     */
    private String description;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 友链展示状态
     */
    private Boolean status;

    /**
     * 用户关联的友链
     */
    private Long userId;


}
