package com.example.blogserver.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.blogserver.Vo.BlogVo;
import com.example.blogserver.Vo.FavoriteVo;
import com.example.blogserver.Vo.FindPageVo;
import com.example.blogserver.Vo.examBlogVo;
import com.example.blogserver.entity.QueryPageBean;
import com.zlc.blogcommon.dto.BlogBackDTO;
import com.zlc.blogcommon.dto.BlogStatisticsDTO;
import com.zlc.blogcommon.po.Blog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zlc
 * @since 2023-11-21
 */
@Repository
public interface BlogMapper extends BaseMapper<Blog> {


    @Select("        SELECT DATE_FORMAT( create_time, '%Y-%m-%d\' ) AS date, COUNT( 1 ) AS count\n" +
            "        FROM blog " +
            "        GROUP BY date\n" +
            "        ORDER BY date DESC")
    /**
     * 文章统计
     *
     * @return {@link List < BlogStatisticsDTO >} 文章统计结果
     */
    List<BlogStatisticsDTO> listArticleStatistics();


    /**
     * 获取对应用户的博客列表(多表查询)
     *
     * @param
     * @return List
     */
    @Select("SELECT b.blog_id, t.type_name, b.recommend, b.published, b.update_time, b.title " +
            "FROM blog b,type t " +
            "WHERE b.type_id = t.type_id AND b.uid = #{uid} LIMIT #{start},#{pageSize} ")
    List<FindPageVo> getAllBlogs(Long uid, Integer start, Integer pageSize);

    @Select("SELECT b.blog_id, t.type_name, b.recommend, b.published, b.update_time, b.title " +
            "FROM blog b,type t " +
            "WHERE b.type_id = t.type_id AND b.uid = #{uid}  and b.title=#{title} LIMIT #{start},#{pageSize} ")
    List<FindPageVo> getBlogByTitle(Long uid, Integer start, Integer pageSize,String title);

    @Select("SELECT b.blog_id, t.type_name, b.recommend, b.published, b.update_time, b.title " +
            "FROM blog b,type t " +
            "WHERE b.type_id = t.type_id AND b.uid = #{uid}  and b.type_id=#{typeId} LIMIT #{start},#{pageSize} ")
    List<FindPageVo> getBlogByType(Long uid, Integer start, Integer pageSize,Integer typeId);


    @Select("SELECT b.blog_id, t.type_name, b.recommend, b.published, b.update_time, b.title " +
            "FROM blog b,type t " +
            "WHERE b.type_id = t.type_id AND b.uid = #{uid}  and b.type_id=#{typeId} and b.title=#{title} LIMIT #{start},#{pageSize} ")
    List<FindPageVo> getBlogByTitleAndType(Long uid, Integer start, Integer pageSize,String title,Integer typeId);


    /**
     * 获取博客列表(多表查询)
     *
     * @param
     * @return List
     */
    List<BlogVo> findHomePage(@Param("queryPageBean") QueryPageBean queryPageBean);



    /**
     * 获取后台博客列表
     * @param queryPageBean 分页实体
     * @return list
     */
    List<BlogBackDTO> adminBlogPage(@Param("queryPageBean")QueryPageBean queryPageBean);

    /**
     * 获取收藏夹的分页数据
     * @param queryPageBean 分页实体
     * @param uid 用户id
     * @return
     */
    List<FavoriteVo> findFavoritesPage(@Param("queryPageBean") QueryPageBean queryPageBean, @Param("uid") Long uid);

    /**
     * 获取管理后台对应博文数量
     * @param queryPageBean 分页实体
     * @return 用户id
     */
    Integer adminBlogPageCount(@Param("queryPageBean")QueryPageBean queryPageBean);


    /**
     * 管理员端获取对应用户的博客列表(多表查询)
     *
     * @param
     * @return List
     */
    @Select("SELECT b.blog_id, t.type_name, b.recommend, b.published, b.update_time, b.title " +
            "FROM blog b,type t " +
            "WHERE b.type_id = t.type_id LIMIT #{start},#{pageSize} ")
    List<FindPageVo> adminGetAllBlogs( Integer start, Integer pageSize);

    @Select("SELECT b.blog_id, t.type_name, b.recommend, b.published, b.update_time, b.title " +
            "FROM blog b,type t " +
            "WHERE b.type_id = t.type_id  and b.title=#{title} LIMIT #{start},#{pageSize} ")
    List<FindPageVo> adminGetBlogByTitle( Integer start, Integer pageSize,String title);

    @Select("SELECT b.blog_id, t.type_name, b.recommend, b.published, b.update_time, b.title " +
            "FROM blog b,type t " +
            "WHERE b.type_id = t.type_id   and b.type_id=#{typeId} LIMIT #{start},#{pageSize} ")
    List<FindPageVo> adminGetBlogByType( Integer start, Integer pageSize,Integer typeId);


    @Select("SELECT b.blog_id, t.type_name, b.recommend, b.published, b.update_time, b.title " +
            "FROM blog b,type t " +
            "WHERE b.type_id = t.type_id   and b.type_id=#{typeId} and b.title=#{title} LIMIT #{start},#{pageSize} ")
    List<FindPageVo> adminGetBlogByTitleAndType( Integer start, Integer pageSize,String title,Integer typeId);


    @Select("select b.blog_id,b.title,u.nickname,b.content ,t.type_name,b.copyright,b.create_time,b.first_picture,b.description from blog b left join user u on b.uid=u.uid  left join type t on t.type_id=b.type_id where b.published=0 ORDER BY b.create_time DESC LIMIT #{start},#{pageSize}")
    List<examBlogVo> examBlogPages(Integer start, Integer pageSize);
}
