package com.example.blogserver.service.impl;

import com.example.blogserver.entity.Message;
import com.example.blogserver.mapper.MessageMapper;
import com.example.blogserver.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 留言板内容 服务实现类
 * </p>
 *
 * @author zlc
 * @since 2023-11-22
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

}
