package com.example.blogserver.mapper;

import com.example.blogserver.Vo.GroupChatVO;
import com.example.blogserver.entity.GroupChat;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zlc
 * @since 2024-01-25
 */
public interface GroupChatMapper extends BaseMapper<GroupChat> {
    /**
     * 获取群聊记录
     * @param rows 条数
     * @return list
     */
    List<GroupChatVO> getMessage(Integer rows);
}
