package com.example.blogserver.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogserver.Vo.HistoryVo;
import com.example.blogserver.Vo.UserVo;

import com.example.blogserver.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zlc
 * @since 2023-11-15
 */
@Repository
public interface UserMapper extends BaseMapper<User> {

    @Select("select u.uid,r.rid,u.nickname,u.username,u.email,u.avatar, u.last_login_time from user u  left join tb_user_role r on u.uid=r.uid ORDER BY u.create_time DESC   LIMIT  #{pageSize} offset #{offset}")
    List<UserVo> Records(Integer offset , Integer pageSize );

    @Select("select u.uid,r.rid,u.nickname,u.username,u.email,u.avatar , u.last_login_time from user u  left join tb_user_role r on u.uid=r.uid where u.nickname like  CONCAT('%',#{nickname}, '%')  ORDER BY u.create_time DESC   LIMIT  #{pageSize} offset #{offset}")
    List<UserVo> RecordsBynickname(Integer offset , Integer pageSize ,String nickname);


    @Select("SELECT * FROM user where email=#{email} and username=#{username}")
    com.example.blogserver.entity.User getUser(String email, String username);
}
