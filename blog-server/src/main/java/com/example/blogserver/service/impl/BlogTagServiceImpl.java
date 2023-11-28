package com.example.blogserver.service.impl;

import com.example.blogserver.entity.BlogTag;
import com.example.blogserver.mapper.BlogTagMapper;
import com.example.blogserver.service.IBlogTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zlc
 * @since 2023-11-21
 */
@Service
public class BlogTagServiceImpl extends ServiceImpl<BlogTagMapper, BlogTag> implements IBlogTagService {



    /**
     * @param blogId
     * @param value
     * @return boolean
     * 根据博客id，增加对应的标签数量
     */

    @Override
    public boolean addOneBlogTag(Long blogId, List<String> value) {
        BlogTag blogTag = new BlogTag();
        blogTag.setBlogId(blogId);
        for (String tag : value) {
            blogTag.setTagId(Integer.valueOf(tag));
            save(blogTag);
        }
        return true;

    }
}
