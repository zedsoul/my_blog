package com.example.blogserver.Controller.admin;

import com.example.blogserver.annotation.LoginRequired;
import com.example.blogserver.annotation.OptLog;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.entity.Tag;
import com.example.blogserver.service.ITagService;
import com.zlc.blogcommon.constant.MessageConstant;
import com.zlc.blogcommon.po.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;

import java.util.List;

import static com.zlc.blogcommon.constant.OptTypeConst.REMOVE;
import static com.zlc.blogcommon.constant.OptTypeConst.SAVE_OR_UPDATE;


/**
 * 标签管理模块
 *
 * zlc
 */

@Api(value = "后台标签管理模块", description = "标签管理模块的接口信息")
@RequestMapping("/tag")
@RestController

public class TagController {
    @Resource
    private ITagService tagService;

    @LoginRequired
    @ApiOperation(value = "个人后台标签分页查询", notes = "返回分页数据")
    @PostMapping("/admin/findPage")
    public R findPage(QueryPageBean queryPageBean) {
        return R.data( tagService.findPage(queryPageBean),"获取分页数据成功");
    }

    @ApiOperation(value = "管理员后台标签数据", notes = "返回分页数据")
    @PostMapping("/admin/tagList")
    public R adminTag( QueryPageBean queryPageBean) {
        return R.data( tagService.adminTag(queryPageBean),"获取后台标签数据成功");
    }

//    @OptLog(optType = SAVE)
//    @PostMapping("/add")
//    public Result addType(@RequestBody Type type){
//        boolean flag = typeService.addType(type);
//        return new Result(flag,"添加成功", MessageConstant.OK);
//    }

    @ApiOperation(value = "获取标签列表", notes = "返回分页数据")
    @GetMapping("/getTagList")
    public R getTagList(){
        return R.data( tagService.getTagList(),"获取成功");
    }

    @ApiOperation(value = "搜索标签", notes = "返回分页数据")
    @GetMapping("/admin/search")
    public R searchTags(QueryPageBean queryPageBean){
        return R.data( tagService.searchTags(queryPageBean),"搜索成功");
    }

    @OptLog(optType = SAVE_OR_UPDATE)
    @PostMapping("/admin/saveOrUpdate")
    @ApiOperation(value = "后台标签添加或更改", notes = "添加或更改")
    public R saveOrUpdateTag( Tag tag) {
        boolean flag = tagService.saveOrUpdateType(tag);
        if (flag)
            return R.data(tag.getTagName(),"添加或更改标签成功");
        return R.fail("添加失败，要添加或更改的标签已存在");
    }

    @OptLog(optType = REMOVE)
    @PostMapping ("/admin/delete")
    @ApiOperation(value = "删除后台标签", notes = "删除标签")
    public R delete(@RequestBody List<Integer> tagIdList) {
        tagService.delete(tagIdList);
        return R.data("删除标签成功", MessageConstant.OK);
    }


}
