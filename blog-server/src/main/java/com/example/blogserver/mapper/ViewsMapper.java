package com.example.blogserver.mapper;

import com.example.blogserver.entity.Views;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlc.blogcommon.dto.ViewsDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 博客浏览量记录表 Mapper 接口
 * </p>
 *
 * @author zlc
 * @since 2023-11-22
 */
public interface ViewsMapper extends BaseMapper<Views> {

    @Select("  SELECT DATE_FORMAT( create_time, '%Y-%m-%d\' ) as `day`, SUM(count) viewsCount\n" +
            "        FROM `views`\n" +
            "        WHERE\n" +
            "        create_time > #{startTime}\n" +
            "        AND create_time <=  #{endTime}\n" +
            "        GROUP BY `day`")
    List<ViewsDTO> getViewsData(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
