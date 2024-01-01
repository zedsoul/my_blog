package com.example.blogserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.Vo.CommentVO;
import com.example.blogserver.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogserver.entity.QueryPageBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zlc
 * @since 2023-11-21
 */
public interface ICommentService extends IService<Comment> {
    /**
     * 获取评论信息的列表
     *
     * @param blogId
     * @return
     */
    List<CommentVO> getCommentList(Long blogId);

    /**
     * 回复评论
     *
     * @param comment
     * @param uid
     */
    void replyComment(Comment comment, Long uid);

    /**
     * 删除评论
     * @param blogId
     * @param commentId
     * @param uid
     */
    boolean delComment(Long blogId, Long commentId, Long uid);

    /**
     * 获取后台评论的分页数据
     * @param queryPageBean 分页实体
     * @return page
     */
    Page<CommentVO> adminComments(QueryPageBean queryPageBean);
}
