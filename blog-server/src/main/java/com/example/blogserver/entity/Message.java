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
 * 留言板内容
 * </p>
 *
 * @author zlc
 * @since 2023-11-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("message")
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 留言id
     */
    @TableId(value = "mid", type = IdType.NONE)
    private Long mid;



    /**
     * 留言内容
     */
    private String messageContent;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String avatar;

    private String nickname;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 'ip来源'
     */
    private String ipSource;


}
