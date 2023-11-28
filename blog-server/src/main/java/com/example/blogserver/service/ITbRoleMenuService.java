//package com.example.blogserver.service;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.example.blogserver.entity.QueryPageBean;
//import com.example.blogserver.entity.TbRoleMenu;
//import com.baomidou.mybatisplus.extension.service.IService;
//
//import java.util.List;
//
///**
// * <p>
// * 用户权限菜单中间表 服务类
// * </p>
// *
// * @author zlc
// * @since 2023-11-24
// */
//public interface ITbRoleMenuService extends IService<TbRoleMenu> {
//
//    /**
//     * 获取后台角色数据
//     * @param queryPageBean 分页实体
//     * @return list
//     */
//    Page<RoleDTO> listRoles(QueryPageBean queryPageBean);
//
//    /**
//     * 获取后台全部角色数据
//     * @return list
//     */
//    List<Role> listAllRoles();
//
//    /**
//     * 后台修改用户角色
//     * @param userRoleVO 用户更新信息
//     */
//    void updateUserRole(UserRoleVO userRoleVO);
//
//    /**
//     * 编辑角色信息
//     * @param roleVO 角色信息
//     */
//    void saveOrUpdateRole(RoleVO roleVO);
//
//    /**
//     * 删除角色
//     * @param roleIdList 角色id列表
//     */
//    void deleteRoles(List<Integer> roleIdList);
//}
