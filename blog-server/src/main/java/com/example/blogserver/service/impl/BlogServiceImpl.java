package com.example.blogserver.service.impl;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.Utils.MarkdownUtils;
import com.example.blogserver.Vo.BlogVo;
import com.example.blogserver.Vo.displayBlogVo;
import com.example.blogserver.dto.BlogBackInfoDTO;
import com.example.blogserver.entity.*;
import com.example.blogserver.exception.BizException;
import com.example.blogserver.filter.SensitiveFilter;
import com.example.blogserver.mapper.BlogMapper;
import com.example.blogserver.mapper.TagMapper;
import com.example.blogserver.mapper.TypeMapper;
import com.example.blogserver.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zlc.blogcommon.dto.*;
import com.zlc.blogcommon.po.Blog;
import com.zlc.blogcommon.po.User;
import com.zlc.blogcommon.vo.TypeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
    @Autowired
    ITagService tagService;


    private Integer currentPage;
    private Integer pageSize;
    private Integer start;
    /**
     * @param blogVo
     *
     * @return {@link Long}
     * 增加或编辑博客
     */
    @Override
    public Long addOrUpdateBlog(BlogVo blogVo) {

        Blog blog = BeanUtil.toBeanIgnoreCase(blogVo, Blog.class, true);
//        blog.setFirstPicture(isImagesTrue(blog.getFirstPicture()));
        blog.setContent(SensitiveFilter.filter(blog.getContent()));
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


    /**
     * 获取网站信息
     * @return blogInfo
     */
    @Override
    public Page<BlogBackDTO> adminBlogPage(QueryPageBean queryPageBean) {
        Page<BlogBackDTO> blogBackDTOPage = new Page<>();
        blogBackDTOPage.setRecords(blogMapper.adminBlogPage(queryPageBean));
        blogBackDTOPage.setTotal(blogMapper.adminBlogPageCount(queryPageBean));
        return blogBackDTOPage;
    }



    @Override
    public Page<BlogVo> findFavoritesPage(QueryPageBean queryPageBean, Long uid) {
            Page<BlogVo> blogVOPage = new Page<BlogVo>();
            blogVOPage.setRecords(blogMapper.findFavoritesPage(queryPageBean, uid));
            return blogVOPage;
        }

    @Override
    public BlogInfoDTO blogInfo() {
        BlogInfoDTO blogInfoDTO = new BlogInfoDTO();
        blogInfoDTO.setBlogCount(blogMapper.selectCount(null));
        blogInfoDTO.setTagCount(tagMapper.selectCount(null));
        blogInfoDTO.setTypeCount(typeMapper.selectCount(null));
        return blogInfoDTO;
    }

    /**
     * 管理员编辑文章
     * @param  blogVO
     * @param uid uid
     */

    @Override
    public void adminSaveOrUpdateBlog(BlogVo blogVO, Long uid) {
        // 保存文章分类
        Type type = saveType(blogVO);
        // 修改文章
        Blog blog = BeanUtil.toBean(blogVO, Blog.class);
        if (Objects.nonNull(type)) {
            blog.setTypeId(type.getTypeId());
        }
        blog.setUid(uid);
        blog.setContent(SensitiveFilter.filter(blog.getContent()));
       saveOrUpdate(blog);
        // 保存文章标签
        saveBlogTag(blogVO, blog.getBlogId());
    }

    /**
     * 保存文章分类
     *
     * @param
     * @return {@link Type} 文章分类
     */
    private Type saveType(BlogVo blogVO) {
        // 判断分类是否存在
        Type type = typeMapper.selectOne(new LambdaQueryWrapper<Type>()
                .eq(Type::getTypeName, blogVO.getTypeName()));
        if (Objects.isNull(type)) {
            type = type.builder()
                    .typeName(blogVO.getTypeName())
                    .build();
            typeService.save(type);
        }
        return type;
    }

    /**
     * 保存文章标签
     *
     * @param blogVO 文章信息
     */
    private void saveBlogTag(BlogVo blogVO, Long blogId) {
        // 编辑文章则先删除文章所有标签
        if (Objects.nonNull(blogVO.getBlogId())) {
            blogTagService.remove(new LambdaQueryWrapper<BlogTag>()
                    .eq(BlogTag::getBlogId, blogVO.getBlogId()));
        }
        // 添加文章标签
        List<String> tagNameList = blogVO.getTags();
        if (CollectionUtils.isNotEmpty(tagNameList)) {
            // 查询已存在的标签
            List<Tag> existTagList = tagService.list(new LambdaQueryWrapper<Tag>()
                    .in(Tag::getTagName, tagNameList));
            List<String> existTagNameList = existTagList.stream()
                    .map(Tag::getTagName)
                    .collect(Collectors.toList());
            List<Integer> existTagIdList = existTagList.stream()
                    .map(Tag::getTagId)
                    .collect(Collectors.toList());
            // 对比新增不存在的标签（去掉存在的就是不存在的，即我们要新增的）
            tagNameList.removeAll(existTagNameList);
            if (CollectionUtils.isNotEmpty(tagNameList)) {
                List<Tag> tagList = tagNameList.stream().map(item -> Tag.builder()
                        .tagName(item)
                        .build())
                        .collect(Collectors.toList());
                tagService.saveBatch(tagList);
                List<Integer> tagIdList = tagList.stream()
                        .map(Tag::getTagId)
                        .collect(Collectors.toList());
                existTagIdList.addAll(tagIdList);
            }
            // 提取标签id绑定文章
            List<BlogTag> blogTagList = existTagIdList.stream().map(item -> BlogTag.builder()
                    .blogId(blogId)
                    .tagId(item)
                    .build())
                    .collect(Collectors.toList());
            blogTagService.saveBatch(blogTagList);
        }
    }

    @Override
    public Page<BlogVo> findPage(QueryPageBean queryPageBean, Long uid) {
        currentPage = queryPageBean.getCurrentPage();
        pageSize = queryPageBean.getPageSize();
        start = (currentPage - 1) * pageSize;

        //设置分页条件
        Page<BlogVo> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<Blog> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid);
        //执行全部查询
        page.setRecords(blogMapper.getAllBlogs(uid, start, pageSize));
        //查询总记录数
        page.setTotal(blogMapper.selectCount(wrapper));
        return page;
    }

//    @Override
//    public List<displayBlogVo> displayblog() {
//        List<Blog> blogList = list();
//        List<displayBlogVo> displayBlogVos = BeanUtil.copyToList(blogList, displayBlogVo.class);
//        displayBlogVos.stream().forEach(blogVo-> {
//                    blogVo.setTags(tagMapper.getBlogTagList(blogVo.getBlogId()).stream().map(Tag::getTagName).collect(Collectors.toList()));
//                    Type type = typeMapper.selectById(blogVo.getBlogId());
//                    if (type != null) {
//                        blogVo.setTypeName(type.getTypeName());
//                    }
//                    User user = userService.getById(blogVo.getUid());
//                    if (user != null) {
//                        blogVo.setNickname(user.getNickname());
//                        blogVo.setAvatar(user.getAvatar());
//                    }
//                }
//
//        );
//        return displayBlogVos;
//    }

    public IPage<displayBlogVo> displayblog(QueryPageBean queryPageBean) {
        currentPage = queryPageBean.getCurrentPage();
        pageSize = queryPageBean.getPageSize();
        start = (currentPage - 1) * pageSize;

        //设置分页条件
        Page<Blog> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        page.addOrder(OrderItem.desc("thumbs"));
        // 使用 MyBatis-Plus 提供的分页查询方法进行查询
        IPage<Blog> blogPage = blogMapper.selectPage(page, null) ;//假设你的 BlogMapper 注入为 blogMapper

        // 将查询结果转换为 DisplayBlogVo 对象列表
        List<displayBlogVo> displayBlogVos = blogPage.getRecords().stream().map(blog -> {
            displayBlogVo blogVo = BeanUtil.copyProperties(blog, displayBlogVo.class);
            // 设置其他属性
            blogVo.setTags(tagMapper.getBlogTagList(blog.getBlogId()).stream().map(Tag::getTagName).collect(Collectors.toList()));
            Type type = typeMapper.selectById(blog.getTypeId());
            if (type != null) {
                blogVo.setTypeName(type.getTypeName());
            }
            User user = userService.getById(blog.getUid());
            if (user != null) {
                blogVo.setNickname(user.getNickname());
                blogVo.setAvatar(user.getAvatar());
            }
            return blogVo;
        }).collect(Collectors.toList());

        // 创建一个新的 IPage，将 DisplayBlogVo 列表设置到其中
        IPage<displayBlogVo> resultPage = new Page<>(blogPage.getCurrent(), blogPage.getSize(), blogPage.getTotal());
        resultPage.setRecords(displayBlogVos);

        return resultPage;
    }


}
