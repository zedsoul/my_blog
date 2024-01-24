package com.example.blogserver.mapper;

import com.example.blogserver.Vo.CommentVO;
import com.example.blogserver.Vo.adminCommentVo;
import com.example.blogserver.Vo.selectAllCommentsVo;
import com.example.blogserver.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogserver.entity.QueryPageBean;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zlc
 * @since 2023-11-21
 */
@Repository
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 将根节点评论封装到list
     * @return
     */
    @Select("SELECT u.nickname, u.avatar, c.comment_id, c.uid, c.content, c.create_time, c.blog_id, c.parent_comment_id " +
            "FROM comment c, user u " +
            "WHERE c.uid = u.uid AND c.blog_id = #{blogId} AND c.parent_comment_id = -1 ")
    List<CommentVO> selectRootList(Long blogId);

    /**
     * 将不是根节点评论封装到list
     * @return
     */
    @Select("SELECT u.nickname, u.avatar, c.comment_id, c.uid, c.content, c.create_time, c.blog_id, c.parent_comment_id, c.reply_uid, uu.nickname as reply_nickname " +
            "FROM comment c, user u, user uu " +
            "WHERE c.uid = u.uid AND c.blog_id = #{blogId} AND c.parent_comment_id != -1 AND c.reply_uid = uu.uid")
    List<CommentVO> selectChildList(Long blogId);

    /**
     * 获取分页数据
     * @param queryPageBean 实体
     * @return list
     */
    @Select("select u.nickname,b.title,c.content,c.create_time,c.comment_id,c.blog_id ,c.uid from user u left join comment c on  c.uid = u.uid left join  blog b  on   c.blog_id=b.blog_id where  u.uid=#{queryString} limit    #{pageSize} offset #{offset}")
    List<CommentVO> adminComments(Integer offset, Integer pageSize, String queryString);


    @Select("SELECT b.blog_id, b.title, u.nickname, b.create_time AS createTime, COUNT(c.comment_id) AS commentCounts\n" +
            "FROM blog b\n" +
            "LEFT JOIN user u ON u.uid = b.uid\n" +
            "LEFT JOIN comment c ON b.blog_id = c.blog_id\n" +
            "GROUP BY b.blog_id, b.title, u.nickname, b.create_time\n" +
            "ORDER BY commentCounts DESC\n" +
            "LIMIT #{pageSize} OFFSET #{offset}; ")
    List<selectAllCommentsVo> selectAllComments(Integer offset, Integer pageSize, String queryString);


    @Select("select c.blog_id,c.comment_id,c.uid,u.nickname ,c.content ,c.create_time from  comment c left join user u on  c.uid=u.uid where c.blog_id=#{bid}")
    List<adminCommentVo> selectCommentsById(Long bid);
}
