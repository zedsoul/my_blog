package com.example.blogserver.mapper;

import com.example.blogserver.entity.TbUserRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 * 用户权限列表 Mapper 接口
 * </p>
 *
 * @author zlc
 * @since 2024-01-07
 */
public interface TbUserRoleMapper extends BaseMapper<TbUserRole> {

    @Select("select r.rid from tb_user_role r left join user u on r.uid=u.uid where r.uid=#{uid}")
    Long rid(Long uid);
}
