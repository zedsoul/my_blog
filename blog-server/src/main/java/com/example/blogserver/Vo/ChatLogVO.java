package com.example.blogserver.Vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 设计博客模块查询的附加条件
 */
@Data
public class                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       ChatLogVO implements Serializable {

    private Long sender;
    private Long receiver;
    private Integer currentPage;


}
