package com.zlc.blogcommon.vo;

import com.zlc.blogcommon.po.Type;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class TypeVO extends Type implements Serializable {
    private Integer typeCount;
}
