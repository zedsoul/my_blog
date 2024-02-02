package com.example.blogserver.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.blogserver.Utils.JWTUtils;
import com.example.blogserver.Utils.RedisUtil;
import com.example.blogserver.Vo.RegistedVo;
import com.example.blogserver.Vo.UserVo;
import com.example.blogserver.entity.QueryPageBean;
import com.example.blogserver.entity.TbUserRole;
import com.example.blogserver.entity.User;
import com.example.blogserver.exception.BizException;
import com.example.blogserver.mapper.UserMapper;
import com.example.blogserver.service.ITbUserRoleService;
import com.example.blogserver.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlc.blogcommon.Utill.HashUtil;
import com.zlc.blogcommon.dto.EmailDTO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import static com.example.blogserver.Utils.CommonUtils.checkEmail;
import static com.example.blogserver.Utils.CommonUtils.getRandomCode;

import static com.zlc.blogcommon.constant.RabbitMQConst.EMAIL_EXCHANGE;
import static com.zlc.blogcommon.constant.RedisConst.*;

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
public class UserServiceImpl extends ServiceImpl<UserMapper, com.example.blogserver.entity.User> implements UserService {
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private ITbUserRoleService roleService;
    @Resource
    private  UserMapper userMapper;
    @Resource
    private RedisUtil redisUtil;

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
        String realCode = (String) redisUtil.get(USER_CODE_KEY + register.getEmail());
        System.out.println("用户--"+register.getEmail()+"--验证码为:"+realCode);// 先验证邮箱验证码是否正确
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getEmail,register.getEmail());
      if( getOne(wrapper)!=null){
          throw new BizException("该账号已经注册过了");
      }else if (!realCode.equals(String.valueOf(register.getVertifyCode()))) {
          throw new BizException("您输入的邮箱验证码不正确");
        }
      else{
        user.setPassword( HashUtil.hashPassword(register.getPassword()));
        user.setLastIp(request.getAttribute("host").toString());

          if(save(user)){
              User one = getOne(new LambdaQueryWrapper<User>().eq(User::getEmail, register.getEmail()));
              Long uid = one.getUid();
              new TbUserRole().setUid(uid).setRid(2);
              roleService.save( new TbUserRole().setUid(uid).setRid(2) );
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
    public Boolean adminRegisted(RegistedVo register, HttpServletRequest request) {
        User user = BeanUtil.toBean(register, User.class);
        RegistedVo registed=new RegistedVo();
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(User::getEmail,register.getEmail());
        if( getOne(wrapper)!=null){
            throw new BizException("该账号已经注册过了");
        }
        else{
            user.setPassword( HashUtil.hashPassword(register.getPassword()));
            user.setLastIp(request.getAttribute("host").toString());

            if(save(user)){
                User one = getOne(new LambdaQueryWrapper<User>().eq(User::getEmail, register.getEmail()));
                Long uid = one.getUid();
                new TbUserRole().setUid(uid).setRid(2);
                roleService.save( new TbUserRole().setUid(uid).setRid(2) );
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
        boolean b = HashUtil.verifyPassword(register.getPassword(), userDB.getPassword());
        if(!HashUtil.verifyPassword(register.getPassword(),userDB.getPassword())){
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
            throw new BizException("请输入正确邮箱或未填写邮箱！");
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
        String email = resetPassword.getEmail();
        String username = resetPassword.getUsername();
        com.example.blogserver.entity.User userDB = userMapper.getUser(email, username);
        if(userDB==null){
            throw   new BizException("该用户不存在，请重新确认");
        }



        String realCode = (String) redisUtil.get(USER_CODE_KEY + resetPassword.getEmail());
        if(realCode==null){
            throw new BizException("验证码已经过期！");
        }// 先验证邮箱验证码是否正确
        if (resetPassword.getVertifyCode()==null) {
            throw new BizException("您没有输入邮箱验证码！");
        }
        if (!realCode.equals(String.valueOf(resetPassword.getVertifyCode()))) {
            throw new BizException("您输入的邮箱验证码不正确");
        }

        userDB.setPassword(HashUtil.hashPassword(resetPassword.getPassword()));
        userDB.setUpdateTime(LocalDateTime.now());
       updateById(userDB);
    }


    @Override
    public String getToken(User user) {
        HashMap<String, String> payload = new HashMap<>();
        payload.put("id", String.valueOf(user.getUid()));
        payload.put("lastIp", user.getLastIp());
        payload.put("username", user.getUsername());
        payload.put("email", user.getEmail());
        redisUtil.set(TOKEN_ALLOW_LIST + user.getEmail(), JWTUtils.getToken(payload), CODE_EXPIRE_TIME);
        return JWTUtils.getToken(payload);
    }

    @Override
    public Page<UserVo> getUserPage(QueryPageBean queryPageBean) {
        Integer pageSize = queryPageBean.getPageSize();
        Integer currentPage = queryPageBean.getCurrentPage();
        int offset = pageSize * (currentPage - 1);
        List<UserVo> records=null;
        if(queryPageBean.getQueryString()!=null){
           records = userMapper.RecordsBynickname(offset, pageSize,queryPageBean.getQueryString());
        }
        else{
         records = userMapper.Records(offset, pageSize);
        }
        Page<UserVo> page = new Page<>(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        page.setRecords(records);
        page.setTotal(count());

        return page;
    }
    @Override
    public User selectByUsername(String username) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("nickname", username)
                .select( "nickname", "avatar", "uid");
        return userMapper.selectOne(wrapper);
    }


    public List<User> selectAllNicknames() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.select( "nickname", "avatar", "uid");
        List<User> users = userMapper.selectList(wrapper);
        return users;
    }

    @Override
    public User selectByUid(Long uid) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("uid", uid)
                .select( "nickname", "avatar", "username");
        return userMapper.selectOne(wrapper);
    }
}
