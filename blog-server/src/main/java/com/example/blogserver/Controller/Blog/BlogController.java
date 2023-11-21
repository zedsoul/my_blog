package com.example.blogserver.Controller.Blog;


import com.example.blogserver.Utils.JWTUtils;
import com.example.blogserver.Utils.WebUtil;
import com.example.blogserver.Vo.BlogVo;
import com.example.blogserver.service.impl.BlogServiceImpl;
import com.zlc.blogcommon.po.result.R;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
       String uid=JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
       Long UID=null;
        try {
             UID = blogService.addOrUpdateBlog(blogVo, Long.valueOf(uid));
//            发消息给mq然后同步es
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(100,e.getMessage());
        }
        return  R.data(UID,"操作成功！");
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



}
