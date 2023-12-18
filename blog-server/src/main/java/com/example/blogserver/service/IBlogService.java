package com.example.blogserver.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogserver.Vo.BlogVo;
import com.example.blogserver.Vo.FindPageVo;
import com.example.blogserver.Vo.displayBlogVo;
import com.example.blogserver.dto.BlogBackInfoDTO;
import com.example.blogserver.entity.QueryPageBean;

import com.zlc.blogcommon.dto.BlogBackDTO;
import com.zlc.blogcommon.dto.BlogInfoDTO;
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
    Long addOrUpdateBlog(BlogVo addBlogVO);


    /**
     * 删除博客
     * @param blogIdList 博客id列表
     */
    void deleteBlogs(List<Long> blogIdList);


    /**
     * 更新博客浏览量
     * @param blogId 博客id
     */
    void updateBlogViewsCount(Long blogId);


    /**
     * 根据博客id获取博客
     * @param blog_id
     * @return blog
     */
    BlogVo getOneBlog(Long blog_id);


    /**
     * 点赞
     * @param blogId 博客id
     * @param uid 用户id
     */
    boolean thumbsUp(Long blogId, Long uid);



    /**
     * 收藏或者取消收藏
     * @param blogId
     * @param uid
     * @return
     */
    boolean favorite(Long blogId, Long uid);



    /**
     * 根据标题或内容查询
     * @param queryPageBean
     * @return
     */
    IPage<Blog> search(QueryPageBean queryPageBean);

    /**
     * 获取博客后台可视化管理数据
     * @return 博客数据dto
     */
    BlogBackInfoDTO getBlogBackInfo();


    /**
     * 获取博客后台分页数据
     * @param queryPageBean 分页实体
     * @return page
     */
    Page<BlogBackDTO> adminBlogPage(QueryPageBean queryPageBean);



    /**
     * 收藏夹的分页数据(按时间降序)
     * @param queryPageBean
     * @return Page<BlogVO>
     */

    Page<BlogVo> findFavoritesPage(QueryPageBean queryPageBean, Long uid);


    /**
     * 获取网站信息
     * @return blogInfo
     */
    BlogInfoDTO blogInfo();

    /**
     * 管理员编辑文章
     * @param  blogVO
     * @param uid uid
     */
    void adminSaveOrUpdateBlog(BlogVo blogVO, Long uid);

    /**
     * 博客管理的分页数据
     * @param queryPageBean
     * @return Page<BlogVO>
     */
    Page<FindPageVo> findPage(QueryPageBean queryPageBean, Long uid, String title, Integer typeId);



    IPage<displayBlogVo> displayblog(QueryPageBean queryPageBean);
}
