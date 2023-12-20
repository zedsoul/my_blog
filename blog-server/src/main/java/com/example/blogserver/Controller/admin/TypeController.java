package com.example.blogserver.Controller.admin;


import com.example.blogserver.annotation.LoginRequired;
import com.example.blogserver.annotation.OptLog;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.entity.Type;
import com.example.blogserver.service.ITypeService;
import com.zlc.blogcommon.constant.OptTypeConst;
import com.zlc.blogcommon.po.result.R;
import com.zlc.blogcommon.po.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 分类管理模块
 * </p>
 *
 * @author zlc
 * @since 2023-11-22
 */
@Api(value = "后台分类管理模块", description = "分类管理模块的接口信息")
@RequestMapping("/type")
@RestController

public class TypeController {


    @Resource
    private ITypeService typeService;

    @OptLog(optType = OptTypeConst.Get)
    @LoginRequired
    @ApiOperation(value = "后台分类分页查询", notes = "返回分页数据")
    @PostMapping("/admin/findPage")
    public R findPage( QueryPageBean queryPageBean) {
        return R.data( typeService.findPage(queryPageBean),"获取分页数据成功");
    }

    @OptLog(optType = OptTypeConst.Get)
    @ApiOperation(value = "管理员后台分类数据", notes = "返回分页数据")
    @PostMapping("/admin/typeList")
    public R adminType( QueryPageBean queryPageBean) {
        return R.data( typeService.adminType(queryPageBean),"获取后台分类数据成功");
    }

    @OptLog(optType = OptTypeConst.SAVE)
    @PostMapping("/admin/add")
    @ApiOperation(value = "后台分类添加", notes = "添加")
    public R addType( Type type) {
        boolean flag = typeService.addType(type);
        if (flag)
            return R.data(type.getTypeName(),"添加成功");
        return R.fail("添加失败，要添加的分类已存在");
    }


    @GetMapping("/getTypeList")
    public R getTypeList() {
        return R.data( typeService.getTypeList(),"获取分类信息成功");
    }


    @OptLog(optType = OptTypeConst.SAVE)
    @ApiOperation(value = "分类搜索", notes = "返回分页数据")
    @GetMapping("/admin/search")
    public R searchTypes(QueryPageBean queryPageBean){
        return R.data( typeService. searchTypes(queryPageBean),"搜索成功");
    }

    @OptLog(optType = OptTypeConst.SAVE_OR_UPDATE)
    @PostMapping("/admin/saveOrUpdate")
    @ApiOperation(value = "后台分类添加或更改", notes = "添加或更改")
    public R saveOrUpdateType( Type type) {
        boolean flag = typeService.saveOrUpdateType(type);
        if (flag)
            return R.success("添加或更改分类成功");
        return R.fail("添加失败，要添加的分类已存在");
    }

    @OptLog(optType = OptTypeConst.REMOVE)
    @DeleteMapping("/admin/delete")
    @ApiOperation(value = "删除后台分类", notes = "删除")
    public Result delete( List<Integer> typeIdList) {
        typeService.delete(typeIdList);
        return Result.ok("删除分类成功");
    }
}
