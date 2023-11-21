package com.example.blogserver.mapper;

import com.example.blogserver.Vo.BlogVo;
import com.example.blogserver.entity.BlogTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zlc
 * @since 2023-11-21
 */
public interface BlogTagMapper extends BaseMapper<BlogTag> {

    /**
     * 根据标签类型获取博客列表
     * @param start
     * @param pageSize
     * @return
     */
    @Select("SELECT b.blog_id, u.nickname, u.avatar, type.type_name, t.tag_name, b.views, b.description, b.create_time ,b.recommend, b.published, b.update_time, b.title, b.first_picture " +
            "FROM blog b, user u, tag t, blog_tag bts, type " +
            "WHERE b.uid = u.uid AND b.blog_id = bts.blog_id AND bts.tag_id = t.tag_id AND t.tag_id = #{tagId} AND type.type_id = b.type_id " +
            "GROUP BY b.blog_id " +
            "ORDER BY b.views DESC " +
            "LIMIT #{start},#{pageSize}")
    List<BlogVo> getByTagId(Integer start, Integer pageSize, Integer tagId);
}
