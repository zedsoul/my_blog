package com.example.blogserver.mapper;

import com.example.blogserver.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

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

}
