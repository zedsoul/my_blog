package com.example.blogserver.Controller.admin;

import cn.hutool.core.bean.BeanUtil;
import com.example.blogserver.Utils.JWTUtils;
import com.example.blogserver.Utils.MinioUtil;
import com.example.blogserver.Utils.WebUtil;
import com.example.blogserver.Vo.UserVo;
import com.example.blogserver.annotation.OptLog;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.service.impl.UserServiceImpl;
import com.zlc.blogcommon.constant.OptTypeConst;
import com.zlc.blogcommon.po.User;
import com.zlc.blogcommon.po.result.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 后台管理
 */
@Api(value = "后台模块")
@RequestMapping("/back")
@RestController

public class BackController {
    @Autowired
    UserServiceImpl userService;
    /**
     * @param file
     * @return {@link R}
     */
    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "上传图片")
    @PostMapping("/admin/{bucket}")
    public R avatarUpload(MultipartFile file,@PathVariable("bucket") String bucket ) {
        String email= JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("email").asString();
        System.out.println(bucket);
        return R.data( MinioUtil.upload(file,email,bucket),"上传成功");
    }
    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation(value = "更新用户信息")
    @PostMapping("/admin/resetuser")
    public R resetUser(@RequestBody  UserVo userVo){
        String id= JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
        User user = BeanUtil.copyProperties(userVo, User.class);
        user.setUid(Long.valueOf(id));
        return R.data( userService.updateById(user),"用户修改成功成功");
    }
}
