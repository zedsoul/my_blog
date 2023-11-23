package com.zlc.blogcommon.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.*;

import java.io.Serializable;

/**
 * <p>
 * 类型
 * </p>
 *
 * @author zlc
 * @since 2023-11-22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "分类实体", description = "分类实体")
@EqualsAndHashCode(callSuper = false)
public class Type extends Model<Type> implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "type_id", type = IdType.AUTO)
    private Integer typeId;

    private String typeName;

    @Override
    protected Serializable pkVal() {
        return this.typeId;
    }

}
