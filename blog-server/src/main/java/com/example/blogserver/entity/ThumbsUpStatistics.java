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
 * @since 2023-11-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("thumbs_up_statistics")
public class ThumbsUpStatistics implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "blog_Id", type = IdType.NONE)
    private Long blogId;

    private String title;

    private Long uid;

    private Integer oneDay;

    private Integer twoDay;

    private Integer threeDay;

    private Integer fourDay;

    private Integer fiveDay;

    private Integer sixDay;

    private Integer sevenDay;


}
