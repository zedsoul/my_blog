package com.example.blogserver.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.Utils.MarkdownUtils;
import com.example.blogserver.Vo.BlogVo;
import com.example.blogserver.dto.BlogBackInfoDTO;
import com.example.blogserver.entity.*;
import com.example.blogserver.exception.BizException;
import com.example.blogserver.mapper.BlogMapper;
import com.example.blogserver.mapper.BlogTagMapper;
import com.example.blogserver.mapper.TagMapper;
import com.example.blogserver.mapper.TypeMapper;
import com.example.blogserver.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zlc.blogcommon.dto.BlogBackDTO;
import com.zlc.blogcommon.dto.BlogRankDTO;
import com.zlc.blogcommon.dto.BlogStatisticsDTO;
import com.zlc.blogcommon.dto.ViewsDTO;
import com.zlc.blogcommon.po.Blog;
import com.zlc.blogcommon.po.User;
import com.zlc.blogcommon.vo.TypeVO;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    UserServiceImpl userService;
    @Autowired
    TagMapper tagMapper;
    @Autowired
    IViewsService viewsService;
    @Autowired
    IMessageService messageService;
    @Autowired
    ITypeService typeService;
    @Autowired
    BlogMapper blogMapper;
    @Autowired
    TypeMapper typeMapper;

    /**
     * @param blogVo
     *
     * @return {@link Long}
     * 增加或编辑博客
     */
    @Override
    public Long addOrUpdateBlog(BlogVo blogVo) {
        Blog blog = BeanUtil.toBeanIgnoreCase(blogVo, Blog.class, true);
        //如果没有blogId就说明是添加blog,反之更新
        if(blogVo.getBlogId()==null) {
            //设置blogId
            Long blogId = IdWorker.getId();
            blog.setBlogId(blogId)
                    .setUid(blogVo.getUid())
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
            if(userService.getById(blogVo.getUid())==null){
                throw  new BizException("注意！博客已添加成功但数据库没有对应的博客发布人,这将会是垃圾数据请及时删除");
            }
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

    /**
     * 更新博客浏览量
     * @param blogId 博客id
     */
    @Override
    public void updateBlogViewsCount(Long blogId) {

    }

    /**
     * @param blogId
     * @return {@link BlogVo}
     * 根据id获取博客的信息
     */
    @Override
    public BlogVo getOneBlog(Long blogId) {
        Blog blog = getOne(new LambdaQueryWrapper<Blog>().eq(Blog::getBlogId, blogId));
        BlogVo blogVo=new BlogVo();
        User user = userService.getById(blog.getUid());
        if(user==null){
            throw new BizException("博客没有对应的发布人，可能为垃圾数据请及时删除！");
        }
        BeanUtils.copyProperties(blog, blogVo);
        String content = blog.getContent();
        blogVo.setNickname(user.getNickname());
        blogVo.setAvatar(user.getAvatar());
        //将查询出来的tag以list方式返回
        blogVo.setTags(tagMapper.getBlogTagList(blogId).stream().map(Tag::getTagName).collect(Collectors.toList()));
        if (content != null) {
            blogVo.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        }   // 将博客对象中的正文内容markdown格式的文本转换成html元素格式
        return blogVo;
    }



    /**
     * 点赞
     * @param blogId 博客id
     * @param uid 用户id
     */
    @Override
    public boolean thumbsUp(Long blogId, Long uid) {
        QueryWrapper<ThumbsUp> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_id", blogId)
                .eq("uid", uid);
        Blog blogDB = getById(blogId);
        if(thumbsUpService.count(wrapper)!=0){// 该用户已点赞过该篇博客
            //取消点赞
            thumbsUpService.remove(wrapper);
            blogDB.setThumbs(blogDB.getThumbs() - 1);
           updateById(blogDB);
            return false;

        }
        //点赞数加一
        ThumbsUp thumbsUp = new ThumbsUp();
        thumbsUp.setBlogId(blogId);
        thumbsUp.setUid(uid);
        thumbsUpService.save(thumbsUp);
        blogDB.setThumbs(blogDB.getThumbs() + 1);
        updateById(blogDB);
        return  true;
    }


    /**
     * 收藏或者取消收藏
     * @param blogId
     * @param uid
     * @return
     */
    @Override
    public boolean favorite(Long blogId, Long uid) {
        QueryWrapper<Favorites> wrapper = new QueryWrapper<>();
        wrapper.eq("blog_id", blogId)
                .eq("uid", uid);
        Integer count = favoritesService.count(wrapper);
        if (count != 0) {  //代表已经收藏了该文章
            favoritesService.remove(wrapper);
            return false;
        }
        Favorites favorites = new Favorites();
        favorites.setBlogId(blogId);
        favorites.setUid(uid);
        favoritesService.save(favorites);
        return true;
    }

    @Override
    public IPage<Blog> search(QueryPageBean queryPageBean) {
        Page<Blog> blogPage = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());

        return page(blogPage, new LambdaQueryWrapper<Blog>()
                .like(queryPageBean.getQueryString() != null, Blog::getTitle, queryPageBean.getQueryString())
                .or().like(queryPageBean.getQueryString() != null, Blog::getContent, queryPageBean.getQueryString())
                .orderByDesc(Blog::getViews));
    }



    /**
     * 获取博客后台可视化管理数据
     * @return 博客数据dto
     */
    @Override
    public BlogBackInfoDTO getBlogBackInfo() {
        // 查询访问量
        Integer viewsCount = viewsService.count();
        // 查询留言量
        Integer messageCount = messageService.count(null);
        // 查询用户量
        Integer userCount = userService.count(null);
        // 查询文章量
        Integer blogCount = count(null);
        // 查询一周访问量
        List<ViewsDTO> viewsDTOList = viewsService.getViewsData();
        // 查询文章统计
        List<BlogStatisticsDTO> blogStatisticsList = blogMapper.listArticleStatistics();
        // 查询分类数据
        List<TypeVO> typeList =typeMapper .getTypeCount();
        // 查询标签数据
        List<Tag> tagList = tagMapper.selectList(null);
        // 查询博客浏览量前五
        List<BlogRankDTO> blogRankDTOList = list(new LambdaQueryWrapper<Blog>()
                .select(Blog::getBlogId, Blog::getTitle, Blog::getViews)
                .last("limit 5").orderByDesc(Blog::getViews))
                .stream().map(blog -> BlogRankDTO.builder()
                        .title(blog.getTitle())
                        .views(blog.getViews())
                        .build())
                .sorted(Comparator.comparingInt(BlogRankDTO::getViews).reversed())
                .collect(Collectors.toList());
        // 查询redis访问量前五的文章
//        Map<Object, Double> blogMap = redisService.zReverseRangeWithScore(blog_VIEWS_COUNT, 0, 4);
        BlogBackInfoDTO blogBackInfoDTO = BlogBackInfoDTO.builder()
                .articleStatisticsList(blogStatisticsList)
                .tagList(tagList)
                .viewsCount(viewsCount)
                .messageCount(messageCount)
                .userCount(userCount)
                .articleCount(blogCount)
                .blogRankDTOList(blogRankDTOList)
                .typeList(typeList)
                .viewsDTOList(viewsDTOList)
                .build();
        return blogBackInfoDTO;

    }

    @Override
    public Page<BlogBackDTO> adminBlogPage(QueryPageBean queryPageBean) {
        Page<BlogBackDTO> blogBackDTOPage = new Page<>();
        blogBackDTOPage.setRecords(blogMapper.adminBlogPage(queryPageBean));
        blogBackDTOPage.setTotal(blogMapper.adminBlogPageCount(queryPageBean));
        return blogBackDTOPage;
    }
}
