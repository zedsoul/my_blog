package com.example.blogserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.Utils.IpUtils;
import com.example.blogserver.entity.Message;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.filter.SensitiveFilter;
import com.example.blogserver.mapper.MessageMapper;
import com.example.blogserver.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

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
    @Resource
    private MessageMapper messageDao;

    @Cacheable(value = {"MessageMap"})
    public List<Message> getMessageList() {
        return messageDao.selectList(null);
    }


    public boolean addMessage(Message message, String host) {
        message.setIp(host);
        String ipSource = IpUtils.getIpSource(host);
        message.setIpSource(ipSource);
        message.setMessageContent(SensitiveFilter.filter(message.getMessageContent()));
        message.setCreateTime(LocalDateTime.now());
        int i = messageDao.insert(message);
        return i == 1;
    }


    public Page<Message> getMessagePage(QueryPageBean queryPageBean) {
        Page<Message> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        QueryWrapper<Message> wrapper = new QueryWrapper<>();
        wrapper.like(queryPageBean.getQueryString() != null, "nickname", queryPageBean.getQueryString());
        Page<Message> messagePage = messageDao.selectPage(page, wrapper);
        messagePage.setSize(messagePage.getRecords().size());
        return messagePage;
    }

    @Caching(evict = {
            @CacheEvict(value = {"MessagePage"}, allEntries = true),
            @CacheEvict(value = {"MessageMap"}, allEntries = true)
    })
    @Override
    public void deleteMessage(List<Long> messageIdList) {
        messageDao.deleteBatchIds(messageIdList);
    }
}
