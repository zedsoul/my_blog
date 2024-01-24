package com.example.blogserver.Controller;


import com.example.blogserver.Utils.JWTUtils;
import com.example.blogserver.Utils.WebUtil;
import com.example.blogserver.annotation.LoginRequired;
import com.example.blogserver.annotation.OptLog;
import com.example.blogserver.entity.Comment;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.service.ICommentService;
import com.zlc.blogcommon.constant.OptTypeConst;
import com.zlc.blogcommon.po.User;
import com.zlc.blogcommon.po.result.R;
import com.zlc.blogcommon.po.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.zlc.blogcommon.constant.OptTypeConst.REMOVE;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author zlc
 * @since 2023-11-21
 */
@RestController
@Api(value = "评论模块", description = "评论模块的接口信息")
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private ICommentService commentService;


    @ApiOperation(value = "根据blogId查找评论")
    @GetMapping("/commentList/{blogId}")
    @OptLog(optType = OptTypeConst.Get)
    public R getCommentList(@PathVariable("blogId") Long blogId) {
        return R.data( commentService.getCommentList(blogId),"获取评论列表信息成功");
    }

    @ApiOperation(value = "回复评论")
    @LoginRequired
    @PostMapping("/admin/replycomment")
    @OptLog(optType = OptTypeConst.Get)
    public R replyComment(@RequestBody Comment comment) {
        String uid= JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
        commentService.replyComment(comment, Long.valueOf(uid));
        return R.data(200,"回复评论信息成功");
    }


    @ApiOperation(value = "用户删除评论")
    @DeleteMapping("/admin/del/{blogId}/{commentId}/{uid}")
    @OptLog(optType = OptTypeConst.Get)
    public R delComment(@PathVariable Long blogId, @PathVariable Long commentId,@PathVariable Long uid ) {

        if (commentService.delComment(blogId, commentId, Long.valueOf(uid)))
            return R.data("删除成功");
        return R.data("您删除的评论不是你发布的，你无权删除！");
    }

    @ApiOperation(value = "获取后台的评论分页数据")
    @PostMapping("/admin/commentPage")
    @OptLog(optType = OptTypeConst.Get)
    public R adminComments(@RequestBody QueryPageBean queryPageBean){
        String uid= JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
        queryPageBean.setQueryString(uid);
        return R.data(commentService.adminComments(queryPageBean),"获取分页数据成功");
    }

    @ApiOperation(value = "删除评论")
    @DeleteMapping("/admin/delete")
    @OptLog(optType = REMOVE)
    public R adminDelComment(@RequestBody List<Long> commentIdList) {
        commentService.removeByIds(commentIdList);
        return R.data("删除评论成功");
    }

    @ApiOperation(value = "后台评论管理-查询博客获取后台的评论分页数据")
    @PostMapping("/admin/managercomments")
    @OptLog(optType = OptTypeConst.Get)
    public R selectAllComments(@RequestBody QueryPageBean queryPageBean){
        String uid= JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
        queryPageBean.setQueryString(uid);
        return R.data(commentService.selectAllComments(queryPageBean),"获取分页数据成功");
    }

    @ApiOperation(value = "后台评论管理-根据博客id查询评论")
    @GetMapping("/admin/selectcommentsbyid")
    @OptLog(optType = OptTypeConst.Get)
    public R selectCommentsById(Long bid){
        return R.data(commentService.selectCommentsById(bid),"获取分页数据成功");
    }

}
