package com.example.blogserver.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.blogserver.Utils.IpUtils;
import com.example.blogserver.Utils.JWTUtils;
import com.example.blogserver.Vo.RegistedVo;
import com.example.blogserver.exception.BizException;
import com.example.blogserver.mapper.UserMapper;
import com.example.blogserver.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.blogcommon.Utill.HashUtil;
import com.zlc.blogcommon.Utill.RSAUtil;
import com.zlc.blogcommon.dto.EmailDTO;
import com.zlc.blogcommon.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;

import static com.example.blogserver.Utils.CommonUtils.checkEmail;
import static com.example.blogserver.Utils.CommonUtils.getRandomCode;
import static com.example.blogserver.interceptors.AuthenticationInterceptor.redisUtil;
import static com.zlc.blogcommon.constant.RabbitMQConst.EMAIL_EXCHANGE;
import static com.zlc.blogcommon.constant.RedisConst.CODE_EXPIRE_TIME;
import static com.zlc.blogcommon.constant.RedisConst.USER_CODE_KEY;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zlc
 * @since 2023-11-15
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * @param register
     * @return {@link Boolean}
     *
     * 用户注册
     */
    @Override
    public Boolean registed(RegistedVo register, HttpServletRequest request) {
        User user = BeanUtil.toBean(register, User.class);
        RegistedVo registed=new RegistedVo();
        String realCode = (String) redisUtil.get(USER_CODE_KEY + register.getEmail());    // 先验证邮箱验证码是否正确
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getEmail,register.getEmail());
      if( getOne(wrapper)!=null){
          throw new BizException("该账号已经注册过了");

      }else if (!realCode.equals(String.valueOf(register.getVertifyCode()))) {
          throw new BizException("您输入的邮箱验证码不正确");
        }
      else{
        user.setPassword( HashUtil.hashPassword(user.getPassword()));
        user.setLastIp(request.getAttribute("host").toString());

          if(save(user)){
              log.info(user.getEmail()+":注册成功！");

              registed.setEmail(register.getEmail());
          }
          else{
              log.info(user.getEmail()+":注册失败！");
          }
      }
      return  true;
    }

    @Override
    public String logined(RegistedVo register, HttpServletRequest request) {
        User userDB = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getEmail, register.getEmail()));
        if(userDB==null){
            throw new BizException("该用户不存在，请重新确认");
        }
        if(HashUtil.verifyPassword(register.getPassword(),userDB.getPassword())){
            throw new BizException("输入密码错误！");
        }
        userDB.setLastIp(request.getRemoteHost());
        userDB.setLastLoginTime(LocalDateTime.now());
        updateById(userDB);
        return  getToken(userDB);
    }

    /**
     * @param username
     * @return boolean
     *
     * 查找用户是否存在
     */
    public boolean UserExist(String username) {//搜索用户名是否存在
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username).last("limit 1");
        return count(wrapper) != 0;
    }

    /**
     * @param userId
     * @return {@link User}
     * 根据Id查找指定用户
     */
    @Override
    public User findById(Long userId) {
        User one = getOne(new LambdaQueryWrapper<User>().eq(User::getUid, userId));
        return one;
    }


    /**
     * @param email
     * 发送邮箱验证码
     */
    @Override
    public void sendCode(String email) {
// 校验账号是否合法
        if (!checkEmail(email)) {
            throw new BizException("请输入正确邮箱");
        }
        // 生成六位随机验证码发送
        String code = getRandomCode();
        // 发送验证码
        EmailDTO emailDTO = EmailDTO.builder()
                .email(email)
                .subject("验证码")
                .content("您的验证码为 " + code + " 有效期30分钟，请不要告诉他人哦！")
                .build();

        rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, null, new Message(JSON.toJSONBytes(emailDTO), new MessageProperties()));
        // 将验证码存入redis，设置过期时间为30分钟
        redisUtil.set(USER_CODE_KEY + email, code, CODE_EXPIRE_TIME);

    }

    /**
     * @param resetPassword
     * @return {@link Boolean}
     *
     * 修改用户密码
     */
    @Override
    public void resetPassword(RegistedVo resetPassword) {
        User userDB = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, resetPassword.getUsername())
                .eq(User::getEmail, resetPassword.getEmail()));
        if(userDB==null){
            throw new BizException("该用户不存在，请重新确认");
        }

        if(!HashUtil.verifyPassword(resetPassword.getPassword(),userDB.getPassword())){
            throw new BizException("输入密码错误！");
        }
        String realCode = (String) redisUtil.get(USER_CODE_KEY + resetPassword.getEmail());    // 先验证邮箱验证码是否正确
        if (!realCode.equals(String.valueOf(resetPassword.getVertifyCode()))) {
            throw new BizException("您输入的邮箱验证码不正确");
        }

        userDB.setPassword(HashUtil.hashPassword(userDB.getPassword()));
        userDB.setUpdateTime(LocalDateTime.now());
       updateById(userDB);
    }


    @Override
    public String getToken(User user) {
        HashMap<String, String> payload = new HashMap<>();
        payload.put("id", String.valueOf(user.getUid()));
        payload.put("lastIp", user.getLastIp());
        payload.put("username", user.getUsername());
        payload.put("email", user.getUsername());
        return JWTUtils.getToken(payload);
    }
}
