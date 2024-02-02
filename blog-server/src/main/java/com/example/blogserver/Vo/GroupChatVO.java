package com.example.blogserver.Vo;

import com.example.blogserver.entity.GroupChat;
import lombok.Data;
import lombok.EqualsAndHashCode;


import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
public class GroupChatVO extends GroupChat implements Serializable {
    private String avatar;  // 用户头像
    private String username;
    private String nickname;

    private Integer type;

    /**
     * 转换后的时间
     */
    private String formatTime;
}
