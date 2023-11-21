package com.example.blogserver.service;

import com.example.blogserver.entity.BlogTag;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zlc
 * @since 2023-11-21
 */
public interface IBlogTagService extends IService<BlogTag> {


    /**
     * @param blogId
     * @param value
     * @return boolean
     * 根据博客id，增加对应的标签数量
     */
    boolean addOneBlogTag(Long blogId, Integer[] value);
}
