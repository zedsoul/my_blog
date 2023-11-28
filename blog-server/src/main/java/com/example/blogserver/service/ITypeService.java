package com.example.blogserver.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.entity.Type;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zlc.blogcommon.vo.TypeVO;

import java.util.List;

/**
 * <p>
 * 类型 服务类
 * </p>
 *
 * @author zlc
 * @since 2023-11-22
 */
public interface ITypeService extends IService<Type> {
    /**
     * 存储type
     *
     * @param type
     * @return type
     */
    boolean addType(Type type);

    /**
     * 获取type
     *
     * @param id
     * @return type
     */
    Type getType(Long id);

    /**
     * 后台管理分类列表
     *
     * @param queryPageBean
     * @return Page
     */
    Page<Type> findPage(QueryPageBean queryPageBean);

    /**
     * 更新分类
     *
     * @param id
     * @param type
     * @return type
     */
    boolean updateType(Long id, Type type);

    /**
     * 删除分类
     *
     * @param id
     * @return boolean
     */
    boolean delete(Long id);

    /**
     * 获取type列表回显
     * @return list
     */
    List<Type> getTypeList();

    /**
     * 获取每个分类的博客数量
     * @return list
     */
    List<TypeVO> getTypeCount();

    /**
     * 获取后台分类分页数据
     * @param queryPageBean 分页实体
     * @return page
     */
    Page<TypeVO> adminType(QueryPageBean queryPageBean);

    /**
     * 查找type列表
     * @param queryPageBean 分页实体
     * @return list
     */
    List<Type> searchTypes(QueryPageBean queryPageBean);

    /**
     * 添加或编辑分类
     * @param type 分类实体
     * @return flag
     */
    boolean saveOrUpdateType(Type type);

    /**
     * 分类
     * @param typeIdList 删除的分类id列表
     */
    void delete(List<Integer> typeIdList);
}
