package com.example.blogserver.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlc.blogcommon.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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

}
