package com.example.blogserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogserver.entity.QueryPageBean;

import java.util.List;

/**
 * <p>
 * 留言板内容 服务类
 * </p>
 *
 * @author zlc
 * @since 2023-11-22
 */
public interface IMessageService extends IService<Message> {

    /**
     * 获取留言信息列表
     * @return list
     */
    List<Message> getMessageList();

    /**
     * 添加留言
     * @param message 留言
     * @param host ip
     * @return boolean
     */
    boolean addMessage(Message message, String host);

    /**
     * 获取留言的分页数据
     * @param queryPageBean 分页实体
     * @return page
     */
    Page<Message> getMessagePage(QueryPageBean queryPageBean);

    /**
     * 删除留言
     * @param messageIdList 留言id列表
     */
    void deleteMessage(List<Long> messageIdList);
}
