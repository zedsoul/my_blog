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
 * @since 2024-01-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("chat_log")
public class ChatLog implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * '消息id'
     */
    @TableId(value = "msg_id", type = IdType.NONE)
    private Long msgId;

    private Long sender;

    private Long receiver;

    /**
     * '发送消息的时间'
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * '消息内容'
     */
    private String content;

    /**
     * 1：文字消息；2：图片
     */
    private Integer textType;


}
