package com.example.blogserver.Aspect;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.blogserver.Utils.IpUtils;
import com.example.blogserver.Utils.RedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

import static com.example.blogserver.Utils.JWTUtils.getTokenInfo;
import static com.zlc.blogcommon.constant.RedisConst.CODE_EXPIRE_TIME;
import static com.zlc.blogcommon.constant.RedisConst.TOKEN_ALLOW_LIST;

@Aspect
@Component
public class LoggingAspect {
 @Autowired
 RedisUtil redisUtil;

    @Pointcut("execution(* com.example.blogserver.Controller.*.*.*(..))")
    public void logMethods() {

    }

    @Before("logMethods()")
    public void logBeforeMethodCall(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = getRequestFromArgs(args);

        String methodName = joinPoint.getSignature().toShortString();
        String methodPath = request != null ? request.getRequestURI() : "Unknown";
        String host = request != null ?  IpUtils.getIpAddr(request) : "Unknown";
        String token = request != null ? request.getHeader("jj-auth") : "No headers";

        System.out.println("=========================接口调用=========================");
        System.out.println("-----------------------------------------------------------");
        System.out.println("Method Path: " + methodPath);
        System.out.println("Method Name: " + methodName);
        System.out.println("Host: " + host);
        System.out.println("Headers: " + token);
        System.out.println("Request Info: " + Arrays.toString(args));
        System.out.println("-----------------------------------------------------------");
    }

    private HttpServletRequest getRequestFromArgs(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof HttpServletRequest) {
                return (HttpServletRequest) arg;
            }
        }
        return null;
    }

    private String extractHeaders(HttpServletRequest request) {
        // Extract and format headers as needed
        // For example:
         Enumeration<String> headerNames = request.getHeaderNames();
         StringBuilder headers = new StringBuilder();
         while (headerNames.hasMoreElements()) {
             String header = headerNames.nextElement();
             headers.append(header).append(": ").append(request.getHeader(header)).append(", ");
         }
         return headers.toString();

    }

}
