package com.example.blogserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2024-01-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("iconed")
public class Iconed implements Serializable {

    private static final long serialVersionUID = 1L;




    private String biliUrl;

    private String qqUrl;

    private String gitUrl;

    private String twitterUrl;
    @TableId(value = "user_id", type = IdType.NONE)
    private Long userId;


}
