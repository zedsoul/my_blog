package com.example.blogserver.Controller.User;

import com.example.blogserver.Vo.RegistedVo;
import com.example.blogserver.annotation.IpRequired;
import com.example.blogserver.service.impl.UserServiceImpl;
import com.zlc.blogcommon.po.User;
import com.zlc.blogcommon.po.result.R;
import com.zlc.blogcommon.po.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RefreshScope
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    UserServiceImpl userService;

    /**
     * 用户注册
     * @param register
     * @return {@link R}
     */
    @PostMapping("/registed")
    @IpRequired
    public R registed( RegistedVo register, HttpServletRequest request) {


        try {
            userService.registed(register,  request);
        } catch (Exception e) {
            e.printStackTrace();
           return R.fail(100,e.getMessage());
        }

        return R.fail(200,"用户账号注册成功");
    }

    /**
     * @param loginer
     * @return {@link R}
     * 用户登录
     */
    @PostMapping("/logined")
    public R logined(RegistedVo loginer, HttpServletRequest request){
        String Token="";
        try {
            Token = userService.logined(loginer, request);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(100,e.getMessage());
        }

        return  R.data(200,Token,"登录成功！");
    }

    /**
     * @param resetPassword
     * @return {@link R}
     * 用户修改密码
     */
    @PostMapping("/resetpassword")
    public R resetPassword(RegistedVo resetPassword){
        try {
            userService.resetPassword(resetPassword);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(100,e.getMessage());
        }

        return  R.data(200,"修改成功！");

    }

    @ApiOperation(value = "发送邮箱验证码")
    @GetMapping("/code")
    public R sendCode(String email) {
        try {
            userService.sendCode(email);
        } catch (Exception e) {
            e.printStackTrace();
            return R.fail(100,e.getMessage());
        }
        return R.success("获取验证码成功（30分钟内有效）");
    }
}
