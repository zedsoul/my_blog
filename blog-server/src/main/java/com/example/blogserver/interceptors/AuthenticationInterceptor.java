package com.example.blogserver.interceptors;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.blogserver.Utils.IpUtils;
import com.example.blogserver.Utils.JWTUtils;
import com.example.blogserver.Utils.RedisUtil;
import com.example.blogserver.annotation.IpRequired;
import com.example.blogserver.annotation.LoginRequired;
import com.example.blogserver.exception.BizException;
import com.example.blogserver.service.UserService;
import com.zlc.blogcommon.po.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Objects;

import static com.example.blogserver.Utils.JWTUtils.getTokenInfo;
import static com.zlc.blogcommon.constant.RedisConst.CODE_EXPIRE_TIME;
import static com.zlc.blogcommon.constant.RedisConst.TOKEN_ALLOW_LIST;
import static com.zlc.blogcommon.enums.StatusCodeEnum.TOKEN_EXPIRED;


@Slf4j
@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
    @Resource
    private UserService userServiceAuto;
    @Resource
    private RedisUtil redisUtilAuto;

    private static UserService userService;
    public static RedisUtil redisUtil;

    @PostConstruct
    public void init() {
        userService = this.userServiceAuto;
        redisUtil = this.redisUtilAuto;//将注入的对象交给静态对象管理
    }

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        log.info("request:[{}]", request.toString());
        log.info("request.getRemoteHost():[{}]", request.getRemoteHost());
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        IpRequired ipRequired = method.getAnnotation(IpRequired.class);
        if (ipRequired != null) {
            String ipAddress = IpUtils.getIpAddr(request);
            log.info("ipAddress:[{}]", ipAddress);
            request.setAttribute("host", ipAddress);
            return true;
        }

        // 判断接口是否需要登录
        LoginRequired loginRequired = method.getAnnotation(LoginRequired.class);
        // 有 @LoginRequired 注解，需要认证
        if (loginRequired != null) {
            // 执行认证
            String token = request.getHeader("jj-auth");
            if (token == null) {
             return    responsed("401", "用户不存在，请重新登录", response);


            }
            String userId;
            String email;
            try {
                DecodedJWT verify = getTokenInfo(token);
                userId = verify.getClaim("id").asString();
                email = verify.getClaim("email").asString();
            } catch (JWTDecodeException e) {
                return   responsed("401",   "token无效，请重新登录", response);
            }
            if (Objects.isNull(redisUtil.get(TOKEN_ALLOW_LIST + email))) {    // token已经失效
                return   responsed("401", "token已经失效", response);
            }
            User user = userService.findById(Long.parseLong(userId));
            if (user == null) {

                return    responsed("401", "用户不存在，请重新登录", response);
            }
            // 验证 token
            try {
                JWTUtils.verify(token);
            } catch (JWTVerificationException e) {
                return    responsed("401", "token无效，请重新登录", response);
            }
            request.setAttribute("currentUser", user);
                DecodedJWT verify = getTokenInfo(token);
                String email1 = verify.getClaim("email").asString();
                redisUtil.expire(TOKEN_ALLOW_LIST + email1,CODE_EXPIRE_TIME);

            return true;
        }
        return true;
    }

    static boolean responsed(String code, String msg, HttpServletResponse response) {
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 返回JSON格式的错误信息
        httpResponse.setContentType("application/json");
        httpResponse.setCharacterEncoding("UTF-8");

        PrintWriter writer;
        try {
            writer = httpResponse.getWriter();
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("code", code);
            errorResponse.put("error", msg);
            writer.write(errorResponse.toJSONString());
        } catch (IOException e) {
            // 处理IO异常
            e.printStackTrace();
            // 可能需要返回特殊的错误信息，比如：
            // writer.write("{\"code\": -1, \"error\": \"Internal Server Error\"}");
            return false;
        }
        return false;
    }
}

