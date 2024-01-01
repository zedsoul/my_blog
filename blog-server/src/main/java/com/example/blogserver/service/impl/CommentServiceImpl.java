package com.example.blogserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.Vo.CommentVO;
import com.example.blogserver.entity.Comment;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.filter.SensitiveFilter;
import com.example.blogserver.mapper.CommentMapper;
import com.example.blogserver.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zlc
 * @since 2023-11-21
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {
    @Resource
    private CommentMapper commentDao;


    public List<CommentVO> getCommentList(Long blogId) {
        // 获取一级评论的list
        List<CommentVO> comments1 = commentDao.selectRootList(blogId);
        // 获取二级评论的list
        List<CommentVO> comments2 = commentDao.selectChildList(blogId);
        return combineChildren(comments1, comments2);
    }


    public void replyComment(Comment comment, Long uid) {
        comment.setUid(uid);
        comment.setCreateTime(LocalDateTime.now());
        if (comment.getParentCommentId() == null) {
            comment.setParentCommentId(-1L);
        }
        comment.setContent(SensitiveFilter.filter(comment.getContent()));
        commentDao.insert(comment);
    }


    public boolean delComment(Long blogId, Long commentId, Long uid) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid)
                .eq("blog_id", blogId)
                .eq("comment_id", commentId);
        if (commentDao.selectOne(wrapper) == null) {
            return false;
        }
        // 获取被删的根评论
        Comment comment = commentDao.selectOne(wrapper);
        // 递归删除该评论以及其子评论
        List<Long> idList = new ArrayList<>();
        getDelIdList(commentId, idList);
        idList.add(commentId);
        commentDao.deleteBatchIds(idList);
        return true;
    }

    @Override
    public Page<CommentVO> adminComments(QueryPageBean queryPageBean) {
        Page<CommentVO> commentVOPage = new Page<>();
        commentVOPage.setRecords(commentDao.adminComments(queryPageBean));
        commentVOPage.setTotal(commentDao.selectCount(null));
        return commentVOPage;
    }

    /**
     * 获取递归删除的id的list
     */
    public void getDelIdList(Long commentId, List<Long> idList) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_comment_id",commentId)
                .select("comment_id");
        List<Comment> childIdList = commentDao.selectList(wrapper);
        //把childIdList里面菜单id值获取出来，封装idList里面，做递归查询
        childIdList.stream().forEach(item -> {
            //封装idList里面
            idList.add(item.getCommentId());
            //递归查询
            this.getDelIdList(item.getCommentId(),idList);
        });
    }

    /**
     * 把子评论加入到父评论的children
     *
     * @param rootList 根评论
     * @param childList 子评论
     * @return list
     */
    public List<CommentVO> combineChildren(List<CommentVO> rootList, List<CommentVO> childList) {
        Map<Long, List<CommentVO>> commentMap = new HashMap<>();
        for (CommentVO child : childList) {
            Long parentId = child.getParentCommentId();
            commentMap.computeIfAbsent(parentId, k -> new ArrayList<>()).add(child);
        }
        for (CommentVO root : rootList) {
            List<CommentVO> children = commentMap.getOrDefault(root.getCommentId(), new ArrayList<>());
            root.setChildren(children);
        }
        return rootList;
}
 }
