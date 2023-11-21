package com.example.blogserver.service.impl;

import com.example.blogserver.entity.Comment;
import com.example.blogserver.mapper.CommentMapper;
import com.example.blogserver.service.ICommentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
