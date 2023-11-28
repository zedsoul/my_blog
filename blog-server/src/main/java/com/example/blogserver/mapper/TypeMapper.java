package com.example.blogserver.mapper;

import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.entity.Type;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zlc.blogcommon.vo.TypeVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 类型 Mapper 接口
 * </p>
 *
 * @author zlc
 * @since 2023-11-22
 */
public interface TypeMapper extends BaseMapper<Type> {

    /**
     * 获取每个分类的博客数量
     * @return 分类数据
     */
    @Select("      SELECT DISTINCT t.type_id, t.type_name, COUNT(b.type_id) type_count\n" +
            "            FROM type t LEFT OUTER JOIN blog b\n" +
            "            ON t.type_id = b.type_id\n" +
            "            GROUP BY t.type_id\n" +
            "            ORDER BY COUNT(b.type_id) DESC")
    List<TypeVO> getTypeCount();

    /**
     * 获取后台管理分页数据
     * @param queryPageBean 分页实体
     * @return list
     */
    @Select("")
    List<TypeVO> adminType(@Param("queryPageBean") QueryPageBean queryPageBean);
}
