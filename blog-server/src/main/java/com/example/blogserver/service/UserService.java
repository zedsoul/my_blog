package com.example.blogserver.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.blogserver.Vo.RegistedVo;
import com.zlc.blogcommon.po.User;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zlc
 * @since 2023-11-15
 */
public interface UserService extends IService<User> {


    /**
     * 用户注册
     *
     * @param register
     * @return {@link Boolean}
     */
    Boolean registed(RegistedVo register, HttpServletRequest request);


    String  logined(RegistedVo register,HttpServletRequest request);

    /**
     * 查询用户是否存在
     *
     * @param username
     * @return
     */
    boolean UserExist(String username);


    /**
     * 根据用户id查询用户
     *
     * @param userId
     * @return user
     */
    User findById(Long userId);


    /**
     * 发送邮箱验证码
     * @param email 用户邮箱
     */
    void sendCode(String email);


    /**
     * 用户修改密码
     * @param
     */
    void resetPassword(RegistedVo resetPassword);



    /**
     * 生成token
     * @return token
     */
    String getToken(User user);
}
