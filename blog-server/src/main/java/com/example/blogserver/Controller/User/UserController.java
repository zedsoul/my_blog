package com.example.blogserver.Controller.User;

import com.example.blogserver.Utils.JWTUtils;
import com.example.blogserver.Utils.WebUtil;
import com.example.blogserver.Vo.RegistedVo;
import com.example.blogserver.annotation.IpRequired;
import com.example.blogserver.annotation.LoginRequired;
import com.example.blogserver.annotation.OptLog;
import com.example.blogserver.service.impl.UserServiceImpl;
import com.zlc.blogcommon.constant.OptTypeConst;
import com.zlc.blogcommon.po.User;
import com.zlc.blogcommon.po.result.R;
import com.zlc.blogcommon.po.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
@Api(value = "用户模块", description = "用户模块的接口信息")
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
    public R registed(@RequestBody RegistedVo register, HttpServletRequest request) {


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
    public R logined(@RequestBody RegistedVo loginer, HttpServletRequest request){
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

    @OptLog(optType = OptTypeConst.Get)
    @LoginRequired
    @ApiOperation(value = "根据token获取用户的信息", notes = "返回用户信息")
    @GetMapping("/userinfo")
    public R userInfo(){
        String uid= JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("id").asString();
        User user = userService.getById(uid);
        return R.data(200,user,"用户数据获取成功！");

    }
}
