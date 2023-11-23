package com.example.blogserver.Vo;

import com.example.blogserver.entity.Tag;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class TagVO extends Tag implements Serializable {
    private Integer tagCount;
}
