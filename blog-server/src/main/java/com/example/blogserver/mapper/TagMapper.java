package com.example.blogserver.mapper;

import com.example.blogserver.entity.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 标签 Mapper 接口
 * </p>
 *
 * @author zlc
 * @since 2023-11-22
 */
@Repository
public interface TagMapper extends BaseMapper<Tag> {
    /**
     * 获取单个博客的tagList
     * @param blogId
     * @return
     */

    List<Tag> getBlogTagList(@Param("blogId") Long blogId);
}
