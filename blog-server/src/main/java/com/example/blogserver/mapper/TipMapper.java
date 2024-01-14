package com.example.blogserver.mapper;

import com.example.blogserver.Vo.HistoryVo;
import com.example.blogserver.Vo.TipVO;
import com.example.blogserver.entity.Tip;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zlc
 * @since 2024-01-06
 */
public interface TipMapper extends BaseMapper<Tip> {

    @Select("select t.id,u.nickname, t.message, b.title,t.blog_id ,t.create_time from tip t left join user u on t.myself_id=u.uid  left join blog b on t.blog_id=b.blog_id where t.myself_id=#{uid} ORDER BY t.create_time DESC   LIMIT  #{pageSize} offset #{offset}")
    List<TipVO> Records(Integer offset , Integer pageSize, String uid);

    @Select( "select u.uid  from tip t left join  blog b on t.blog_id=b.blog_id left join user u on b.uid=u.uid  where t.blog_id=#{blogId}" )
    List<Long> myselfName (Long blogId);
}
