package com.example.blogserver.Vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteVo {
    private Long blogId;
    /**
     * 标题
     */
    private String title;

    private String description;
    private String typeName;

    private LocalDateTime createTime;
}
