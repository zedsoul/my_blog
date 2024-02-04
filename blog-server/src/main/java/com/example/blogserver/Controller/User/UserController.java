package com.example.blogserver.Controller.User;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.blogserver.Utils.JWTUtils;
import com.example.blogserver.Utils.RedisUtil;
import com.example.blogserver.Utils.WebUtil;
import com.example.blogserver.Vo.RegistedVo;
import com.example.blogserver.Vo.UserVo;
import com.example.blogserver.annotation.IpRequired;
import com.example.blogserver.annotation.LoginRequired;
import com.example.blogserver.annotation.OptLog;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.entity.TbUserRole;
import com.example.blogserver.entity.User;
import com.example.blogserver.mapper.TbUserRoleMapper;
import com.example.blogserver.service.ITbUserRoleService;
import com.example.blogserver.service.impl.UserServiceImpl;
import com.zlc.blogcommon.constant.OptTypeConst;
import com.zlc.blogcommon.po.result.R;
import com.zlc.blogcommon.po.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.time.ZoneOffset;

import static com.zlc.blogcommon.constant.RedisConst.TOKEN_ALLOW_LIST;

@Api(value = "用户模块", description = "用户模块的接口信息")
@RefreshScope
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    TbUserRoleMapper userRoleMapper;
    @Autowired
    ITbUserRoleService roleService;
    @Autowired
    RedisUtil redisUtil;
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
     * 管理员添加
     * @param register
     * @return {@link R}
     */
    @PostMapping("/adminregisted")
    @IpRequired
    public R adminregisted(@RequestBody RegistedVo register, HttpServletRequest request) {


        try {
            userService.adminRegisted(register,  request);
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
    public R resetPassword(@RequestBody  RegistedVo resetPassword){
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
        Long rid = userRoleMapper.rid(Long.valueOf(uid));
        com.example.blogserver.entity.User user = userService.getById(uid);
        UserVo userVo = BeanUtil.copyProperties(user, UserVo.class);
        userVo.setTimeStamp(userVo.getLastLoginTime().toInstant(ZoneOffset.UTC).toEpochMilli());
        userVo.setRid(rid);
        return R.data(200,userVo,"用户数据获取成功！");

    }

    @OptLog(optType = OptTypeConst.Get)
    @ApiOperation("获取用户分页信息")
    @GetMapping("/admin/getuserpage")
    public R getUserPage( QueryPageBean queryPageBean) {

        return R.data( userService.getUserPage(queryPageBean),"获取留言分页信息");
    }

    @OptLog(optType = OptTypeConst.Get)
    @ApiOperation("删除用户信息")
    @GetMapping("/deleteusered")
    public R deleteUser( String  uid) {
        return R.data( userService.remove(new LambdaQueryWrapper<com.example.blogserver.entity.User>().eq(User::getUid,uid)),"删除用户信息成功");
    }

    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation("更改用户权限")
    @PostMapping("/admin/update/user")
    public R updateUser( @RequestBody  TbUserRole tbUserRole) {
        return R.data( roleService.update(tbUserRole,new LambdaQueryWrapper<TbUserRole>().eq(TbUserRole::getUid,tbUserRole.getUid())),"更新用户权限用户信息成功");
    }

    @OptLog(optType = OptTypeConst.Get)
    @ApiOperation("获取用户头像，昵称")
    @GetMapping("/admin/getnickNames")

    public  R getNicknames(){
        return R.data(userService.selectAllNicknames(),"获取用户信息成功！");
    }


    @OptLog(optType = OptTypeConst.UPDATE)
    @ApiOperation("删除token")
    @GetMapping("/loginout")
    public R updateUser() {
        String email= TOKEN_ALLOW_LIST+JWTUtils.getTokenInfo(WebUtil.getHeader("jj-auth")).getClaim("email").asString();
         redisUtil.del(email);
        return R.data("退出成功！");
    }
}
