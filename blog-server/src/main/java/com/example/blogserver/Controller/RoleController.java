//package com.example.blogserver.Controller;
//
//import com.example.blogserver.annotation.OptLog;
//import com.example.blogserver.entity.QueryPageBean;
//
//import com.zlc.blogcommon.po.result.R;
//import com.zlc.blogcommon.po.result.Result;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.web.bind.annotation.*;
//
//
//import javax.annotation.Resource;
//import javax.validation.Valid;
//
//import java.util.List;
//
//import static com.zlc.blogcommon.constant.OptTypeConst.*;
//
//
///**
// * 资源模块控制器
// *
// * @author fangjiale 2022年01月14日
// */
//
//@Api(value = "角色模块", description = "角色模块的接口信息")
//@RequestMapping("/role")
//@RestController
//@CrossOrigin
//public class RoleController {
//    @Resource
//    private ITbRoleMenuService roleService;
//
//    @ApiOperation(value = "获取后台角色数据")
//    @GetMapping("/admin/listRoles")
//    public R listRole(QueryPageBean queryPageBean) {
//        return R.data( roleService.listRoles(queryPageBean),"获取后台角色数据");
//    }
//
//    @ApiOperation(value = "获取后台全部角色数据")
//    @GetMapping("/admin/listAllRoles")
//    public R listAllRoles() {
//        return R.data( roleService.listAllRoles(),"获取后台角色数据");
//    }
//
//    /**
//     * 保存或更新角色
//     *
//     * @param roleVO 角色信息
//     * @return {@link Result}
//     */
//
//    @OptLog(optType = SAVE_OR_UPDATE)
//    @ApiOperation(value = "保存或更新角色信息")
//    @PostMapping("/admin/saveOrUpdateRole")
//    public R saveOrUpdateRole(@RequestBody @Valid RoleVO roleVO) {
//        roleService.saveOrUpdateRole(roleVO);
//        return R.data("编辑角色信息成功");
//    }
//
//    /**
//     * 删除角色
//     *
//     * @param roleIdList 角色id列表
//     * @return {@link Result}
//     */
//    @OptLog(optType = REMOVE)
//    @ApiOperation(value = "删除角色")
//    @DeleteMapping("/admin")
//    public R deleteRoles(@RequestBody List<Integer> roleIdList) {
//        roleService.deleteRoles(roleIdList);
//        return R.data("删除角色成功");
//    }
//
//    /**
//     * 修改用户角色
//     *
//     * @param userRoleVO 用户角色信息
//     * @return {@link Result}
//     */
//    @ApiOperation(value = "修改用户角色")
//    @PutMapping("/admin/user")
//    public R updateUserRole(@Valid @RequestBody UserRoleVO userRoleVO) {
//        roleService.updateUserRole(userRoleVO);
//        return R.data("修改用户角色成功");
//    }
//
//}
