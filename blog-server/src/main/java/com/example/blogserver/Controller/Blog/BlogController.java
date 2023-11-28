package com.example.blogserver.Controller.Blog;


import com.example.blogserver.Utils.JWTUtils;
import com.example.blogserver.Utils.WebUtil;
import com.example.blogserver.Vo.BlogVo;
import com.example.blogserver.annotation.LoginRequired;
import com.example.blogserver.annotation.OptLog;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.service.impl.BlogServiceImpl;
import com.zlc.blogcommon.constant.OptTypeConst;
import com.zlc.blogcommon.po.result.R;
import com.zlc.blogcommon.po.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
@Api(value = "博客管理模块", description = "博客管理模块的接口信息")
@RefreshScope
@RestController
@RequestMapping("/blog")
@Slf4j
public class BlogController {
@Autowired
    BlogServiceImpl blogService;


    @OptLog(optType = OptTypeConst.SAVE)
    @LoginRequired
    @ApiOperation(value = "个人后台分页查询", notes = "返回分页数据")
    @PostMapping("/admin/findPage")
    public R findPage( QueryPageBean queryPageBean) {
        String uid=JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
        return R.data( blogService.findPage(queryPageBean, Long.valueOf(uid)),"获取分页数据成功");
    }



    @OptLog(optType = OptTypeConst.Get)
    @LoginRequired
    @ApiOperation(value = "展示博客信息按照浏览数或点赞数排列", notes = "返回博客信息")
    @GetMapping("/displayblog")
    public R displayblog(QueryPageBean queryPageBean) {
        return R.data( blogService.displayblog(queryPageBean),"获取分页数据成功");
    }

    /**
     * @param blogVo
     * @return {@link R}
     * 用户添加或更新博客
     */
    @OptLog(optType = OptTypeConst.SAVE_OR_UPDATE)
    @ApiOperation(value = "用户添加或更新博客")
    @PostMapping("/addorupdate")
    public R addOrupdate(BlogVo blogVo){
        //获取token
       String uid=JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
        blogVo.setUid(Long.valueOf(uid));
        Long  blogId=null;
        try {
            blogId = blogService.addOrUpdateBlog(blogVo);
//            发消息给mq然后同步es
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(100,e.getMessage());
        }
        return  R.data(blogId,"操作成功！");
    }

    @OptLog(optType = OptTypeConst.SAVE_OR_UPDATE)
    @LoginRequired
    @ApiOperation(value = "管理员更新或发布博客")
    @PostMapping("/admin/saveOrUpdate")
    public Result adminSaveOrUpdateBlog( BlogVo blogVO ) {
        String uid=JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
        blogService.adminSaveOrUpdateBlog(blogVO, Long.valueOf(uid));
        return Result.ok("编辑成功");
    }

    /**
     * 删除博客
     * @param blogIdList 博客id列表
     */
    @OptLog(optType = OptTypeConst.REMOVE)
    @ApiOperation(value = "删除博客")
    @PostMapping("/admin/deleteblog")
        public  R deleteBlogs(@RequestBody List<Long> blogIdList){

        try {
            blogService.deleteBlogs(blogIdList);
//            发消息给mq然后同步es
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(100,e.getMessage());
        }
        return  R.data("删除成功！");

    }
    @OptLog(optType = OptTypeConst.Get)
    @ApiOperation(value = "根据id获取博客的信息")
    @GetMapping("/id/{blogId}")
    public R getOneBlog(@PathVariable("blogId") Long blogId) {
        BlogVo blogVo=null;
        try {
        blogService.updateBlogViewsCount(blogId);
      blogVo=  blogService.getOneBlog(blogId);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(100,e.getMessage());
        }
        return R.data(blogVo,"获取博客信息成功");
    }

    @OptLog(optType = OptTypeConst.SAVE)
    @ApiOperation(value = "点赞")
    @GetMapping("/admin/thumbUp/{blogId}/{uid}")
    public R thumbsUp(@PathVariable("blogId") Long blogId, @PathVariable("uid") Long uid) {
        boolean flag = blogService.thumbsUp(blogId, uid);
        if (flag)
            return R.data("点赞成功");
        return R.data("取消点赞成功");
    }

    @OptLog(optType = OptTypeConst.SAVE)
    @ApiOperation(value = "收藏")
    @GetMapping("/admin/favorite/{blogId}/{uid}")
    public Result favorite(@PathVariable("blogId") Long blogId, @PathVariable("uid") Long uid) {
        boolean flag = blogService.favorite(blogId, uid);
        if (flag)
            return Result.ok("收藏成功");
        return Result.ok("取消收藏成功");
    }


    /**
     * 根据标题或内容查询
     * @param queryPageBean
     * @return
     */
    @OptLog(optType = OptTypeConst.Get)
    @ApiOperation(value = "根据标题或内容查询")
    @PostMapping("/search")
    public R search( QueryPageBean queryPageBean) {
        return R.data(blogService.search(queryPageBean),"查询成功");
    }


    /**
     * 查看后台信息
     *
     * @return 后台信息
     */
    @OptLog(optType = OptTypeConst.Get)
    @ApiOperation(value = "查看后台信息")
    @GetMapping("/admin/getBlogBackInfo")
    public R getBlogBackInfo() {

        return R.data( blogService.getBlogBackInfo(),"获取后台信息成功");
    }

    /**
     * 查看后台信息
     *
     * @return 后台信息
     */
    @OptLog(optType = OptTypeConst.Get)
    @ApiOperation(value = "后台获取博客信息")
    @GetMapping("/admin/blogPage")
    public R adminBlogPage(QueryPageBean queryPageBean) {
        return R.data( blogService.adminBlogPage(queryPageBean),"获取后台信息成功");
    }

    @OptLog(optType = OptTypeConst.Get)
    @LoginRequired
    @ApiOperation(value = "收藏夹分页查询", notes = "返回分页数据")
    @PostMapping("/admin/findFavoritesPage")
    public R findFavoritesPage( QueryPageBean queryPageBean) {
        String uid=JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
        return R.data( blogService.findFavoritesPage(queryPageBean, Long.valueOf(uid)),"获取分页数据成功");
    }

    //获取博文，标签，类型的数量
    @OptLog(optType = OptTypeConst.Get)
    @ApiOperation(value = "博文信息", notes = "博文信息")
    @GetMapping("/blogInfo")
    public R blogInfo() {
        return R.data( blogService.blogInfo(),"获取分页数据成功");
    }
}
