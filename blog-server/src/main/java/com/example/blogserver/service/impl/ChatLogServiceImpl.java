package com.example.blogserver.service.impl;

import com.example.blogserver.entity.ChatLog;
import com.example.blogserver.mapper.ChatLogMapper;
import com.example.blogserver.service.IChatLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zlc
 * @since 2024-01-25
 */
@Service
public class ChatLogServiceImpl extends ServiceImpl<ChatLogMapper, ChatLog> implements IChatLogService {

}
