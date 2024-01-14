package com.example.blogserver.mapper;

import com.example.blogserver.Vo.HistoryVo;
import com.example.blogserver.entity.History;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zlc
 * @since 2024-01-02
 */
public interface HistoryMapper extends BaseMapper<History> {

    @Select("select h.id,u.nickname, h.operation, b.title,h.blog_id ,h.create_time from history h left join user u on h.uid=u.uid  left join blog b on h.blog_id=b.blog_id where h.uid=#{uid} ORDER BY h.create_time DESC   LIMIT  #{pageSize} offset #{offset}")
    List<HistoryVo> Records(Integer offset ,Integer pageSize,String uid);
}
