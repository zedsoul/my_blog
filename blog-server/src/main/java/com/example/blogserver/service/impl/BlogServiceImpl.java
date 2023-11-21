package com.example.blogserver.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.example.blogserver.Vo.BlogVo;
import com.example.blogserver.entity.BlogTag;
import com.example.blogserver.entity.Comment;
import com.example.blogserver.entity.Favorites;
import com.example.blogserver.entity.ThumbsUp;
import com.example.blogserver.exception.BizException;
import com.example.blogserver.mapper.BlogMapper;
import com.example.blogserver.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.blogcommon.po.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
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
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements IBlogService {

    @Autowired
    IBlogTagService blogTagService;
    @Autowired
    IThumbsUpService thumbsUpService;
    @Autowired
    IFavoritesService favoritesService;
    @Autowired
    ICommentService commentService;
    /**
     * @param blogVo
     * @param uid
     * @return {@link Long}
     * 增加或编辑博客
     */
    @Override
    public Long addOrUpdateBlog(BlogVo blogVo, Long uid) {
        Blog blog = BeanUtil.toBeanIgnoreCase(blogVo, Blog.class, true);
        if(blogVo.getBlogId()==null) {
            //设置blogId
            Long blogId = IdWorker.getId();
            blog.setBlogId(blogId)
                    .setUid(uid)
                    .setCreateTime(LocalDateTime.now())
                    .setPublished(false)
                    .setRecommend(false)
                    .setViews(0)
                    .setAppreciation(false)
                    .setCommentAble(false)
                    .setCopyright(false);
            save(blog);
            //保存博客对应的标签
            blogTagService.addOneBlogTag(blog.getBlogId(), blogVo.getTags());
        }else{
            if(getOne(new LambdaQueryWrapper<Blog>().eq(Blog::getBlogId,blogVo.getBlogId()))==null){
                throw new BizException("数据库没有对应博客id！");
            }
            if(blogVo.getTags()!=null){
                blogTagService.remove(new LambdaQueryWrapper<BlogTag>().eq(BlogTag::getBlogId, blog.getBlogId()));
            }
            blogTagService.addOneBlogTag(blog.getBlogId(), blogVo.getTags());
            blog.setUpdateTime(LocalDateTime.now());
            updateById(blog);
        }
        return  blog.getBlogId();
    }


    /**
     * 删除博客
     * @param blogIdList 博客id列表
     */
    @Override
    public void deleteBlogs(  List<Long> blogIdList) {
        System.out.println(blogIdList.toString());
            blogTagService.remove(new LambdaQueryWrapper<BlogTag>() //删除博客标签的中间表数据
                    .in(BlogTag::getBlogId, blogIdList));
        // 删除博客的点赞和收藏信息
        thumbsUpService.remove(new LambdaQueryWrapper<ThumbsUp>().in(ThumbsUp::getBlogId, blogIdList));
        favoritesService.remove(new LambdaQueryWrapper<Favorites>().in(Favorites::getBlogId, blogIdList));
        commentService.remove(new LambdaQueryWrapper<Comment>().in(Comment::getBlogId, blogIdList));// 删除博客下的所有评论数据
        removeByIds(blogIdList); //删除博客
    }
}
