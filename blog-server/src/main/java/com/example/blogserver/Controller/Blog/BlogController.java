package com.example.blogserver.Controller.Blog;


import com.example.blogserver.Utils.JWTUtils;
import com.example.blogserver.Utils.WebUtil;
import com.example.blogserver.Vo.BlogVo;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.service.impl.BlogServiceImpl;
import com.zlc.blogcommon.po.result.R;
import com.zlc.blogcommon.po.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RefreshScope
@RestController
@RequestMapping("/blog")
@Slf4j
public class BlogController {
@Autowired
    BlogServiceImpl blogService;

    /**
     * @param blogVo
     * @return {@link R}
     * 添加或更新博客
     */
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

    /**
     * 删除博客
     * @param blogIdList 博客id列表
     */
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


    @ApiOperation(value = "点赞")
    @GetMapping("/admin/thumbUp/{blogId}/{uid}")
    public R thumbsUp(@PathVariable("blogId") Long blogId, @PathVariable("uid") Long uid) {
        boolean flag = blogService.thumbsUp(blogId, uid);
        if (flag)
            return R.data("点赞成功");
        return R.data("取消点赞成功");
    }

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
    @PostMapping("/search")
    public R search( QueryPageBean queryPageBean) {
        return R.data(blogService.search(queryPageBean),"查询成功");
    }


    /**
     * 查看后台信息
     *
     * @return 后台信息
     */
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
    @ApiOperation(value = "后台获取博客信息")
    @GetMapping("/admin/blogPage")
    public R adminBlogPage(QueryPageBean queryPageBean) {
        return R.data( blogService.adminBlogPage(queryPageBean),"获取后台信息成功");
    }

}
