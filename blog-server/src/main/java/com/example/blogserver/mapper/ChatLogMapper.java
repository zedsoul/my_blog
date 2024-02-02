package com.example.blogserver.mapper;

import com.example.blogserver.Vo.ChatLogVO;
import com.example.blogserver.entity.ChatLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zlc
 * @since 2024-01-25
 */
public interface ChatLogMapper extends BaseMapper<ChatLog> {
    /**
     * 获取某用户的聊天记录
     * @return list
     */
    List<ChatLog> getMessage(@Param("chatLogVO") ChatLogVO chatLogVO);
}
