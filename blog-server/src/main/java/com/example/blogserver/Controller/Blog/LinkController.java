package com.example.blogserver.Controller.Blog;


import com.example.blogserver.Utils.JWTUtils;
import com.example.blogserver.Utils.WebUtil;
import com.example.blogserver.annotation.OptLog;
import com.example.blogserver.entity.Link;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.service.ILinkService;
import com.zlc.blogcommon.po.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.zlc.blogcommon.constant.OptTypeConst.*;


/**
 * <p>
 * 友链控制器
 * </p>
 *
 * @author zlc
 * @since 2023-12-7
 */
@RestController
@Api(value = "友链模块", description = "友链模块的接口信息")
@RequestMapping("/link")
public class LinkController {
    @Resource
    private ILinkService linkService;

    @ApiOperation(value = "获取友链列表", notes = "获取友链列表")
    @GetMapping("/getLink")
    public R getLinkList() {
        String uid= JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
        return R.data(linkService.getLinkList( Long.valueOf(uid)),"获取友链列表信息成功" );
    }

    @ApiOperation(value = "添加友链", notes = "添加友链")
    @PostMapping("/addLink")
    public R addLink(@RequestBody Link link) {
        link.setCreateTime(LocalDateTime.now());
        if (linkService.addLink(link))
            return R.data("添加友链成功，请等待管理员审核");
        return R.fail("添加友链失败！");
    }

    /**
     * 保存或修改友链
     *
     * @param link 友链信息
     * @return {@link}
     */
    @OptLog(optType = SAVE_OR_UPDATE)
    @ApiOperation(value = "保存或修改友链")
    @PostMapping("/admin/saveOrUpdateFriendLink")
    public R saveOrUpdateFriendLink(@RequestBody Link link) {
        link.setCreateTime(LocalDateTime.now());
        linkService.saveOrUpdateFriendLink(link);
        return R.data("添加或编辑友链成功");
    }

    /**
     * 删除友链
     *
     * @param linkIdList 友链id列表
     * @return {@link}
     */
    @OptLog(optType = REMOVE)
    @ApiOperation(value = "删除友链")
    @DeleteMapping("/admin/delete")
    public R deleteFriendLink(@RequestBody List<Long> linkIdList) {
        linkService.removeByIds(linkIdList);
        return R.data("删除友链成功");
    }

    @ApiOperation(value = "获取友链分页数据", notes = "获取友链分页数据")
    @GetMapping("/listLink")
    public R listLink(QueryPageBean queryPageBean) {
        return R.data(linkService.listLink(queryPageBean),"获取分页数据成功");
    }

    /**
     * 修改友链展示状态
     *
     * @param link 友链
     * @return Result
     */
    @OptLog(optType = UPDATE)
    @ApiOperation(value = "修改友链禁用状态")
    @PutMapping("/admin/disable")
    public R updateLinkDisable(@RequestBody Link link) {
        linkService.updateLinkDisable(link);
        return R.data("获取分页数据成功");
    }

}

