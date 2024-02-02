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
 * @since 2024-02-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("user")
public class User  implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * uuid
     */
    @TableId(value = "uid", type = IdType.NONE)
    private Long uid;

    /**
     * 别名
     */
    private String nickname;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * qq邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 上次登录的ip
     */
    private String lastIp;

    /**
     * 禁用状态
     */
    private Boolean status;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 登录类型
     */
        private Integer loginType;

    /**
     * 上一次登录时间
     */
    private LocalDateTime lastLoginTime;


}
