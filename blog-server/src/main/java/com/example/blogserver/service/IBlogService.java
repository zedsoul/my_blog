package com.example.blogserver.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogserver.Vo.BlogVo;
import com.zlc.blogcommon.po.Blog;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zlc
 * @since 2023-11-21
 */
public interface IBlogService extends IService<Blog> {


    /**
     * 添加博客
     * @param addBlogVO 返回博客实体
     * @return boolean
     */
    Long addOrUpdateBlog(BlogVo addBlogVO, Long uid);


    /**
     * 删除博客
     * @param blogIdList 博客id列表
     */
    void deleteBlogs(List<Long> blogIdList);
}
