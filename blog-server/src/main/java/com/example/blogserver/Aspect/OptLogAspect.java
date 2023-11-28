package com.example.blogserver.Aspect;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.blogserver.Utils.IpUtils;
import com.example.blogserver.annotation.OptLog;
import com.example.blogserver.entity.TbOperationLog;
import com.example.blogserver.exception.BizException;
import com.example.blogserver.service.ITbOperationLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Objects;

import static com.example.blogserver.Utils.JWTUtils.getTokenInfo;

/**
 * 操作日志切面处理
 *
 */
@Aspect
@Component
public class OptLogAspect {

    @Resource
    private ITbOperationLogService operationLogDao;

    /**
     * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
     */
    @Pointcut("@annotation(com.example.blogserver.annotation.OptLog)")
    public void optLogPointCut() {}


    /**
     * 正常返回通知，拦截用户操作日志，连接点正常执行完成后执行， 如果连接点抛出异常，则不会执行
     *
     * @param joinPoint 切入点
     * @param keys      返回结果
     */
    @AfterReturning(value = "optLogPointCut()", returning = "keys")
    public void saveOptLog(JoinPoint joinPoint, Object keys) {

        // 获取RequestAttributes
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) Objects.requireNonNull(requestAttributes).resolveReference(RequestAttributes.REFERENCE_REQUEST);
        TbOperationLog operationLog = new TbOperationLog();
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取操作
        Api api = (Api) signature.getDeclaringType().getAnnotation(Api.class);
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        OptLog optLog = method.getAnnotation(OptLog.class);
        // 操作模块
        operationLog.setOptModule(api.value());
        // 操作类型
        operationLog.setOptType(optLog.optType());
        // 操作描述
        operationLog.setOptDesc(apiOperation.value());
        // 获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 获取请求的方法名
        String methodName = method.getName();
        methodName = className + "." + methodName;
        // 请求方式
        operationLog.setRequestMethod(Objects.requireNonNull(request).getMethod());
        // 请求方法
        operationLog.setOptMethod(methodName);
        // 请求参数
        operationLog.setRequestParam(JSON.toJSONString(joinPoint.getArgs()));
        // 返回结果
        operationLog.setResponseData(JSON.toJSONString(keys));
        // 请求用户ID
        String token=request.getHeader("jj-Auth");
        if(token!=null) {
            String userId;
            String email;
            try {
                DecodedJWT verify = getTokenInfo(token);
                userId = verify.getClaim("id").asString();
                email = verify.getClaim("email").asString();
            } catch (JWTDecodeException e) {
                throw new BizException("token无效，请重新登录");
            }
            operationLog.setUid(Long.valueOf(userId));
            // 请求用户
            operationLog.setEmail(email);
        }else{
            operationLog.setUid(0L);
            operationLog.setEmail("暂无信息！");
        }        // 请求IP
        String ipAddress = IpUtils.getIpAddr(request);
        operationLog.setIpAddress(ipAddress);
        operationLog.setIpSource(IpUtils.getIpSource(ipAddress));
        // 请求URL
        operationLog.setOptUrl(request.getRequestURI());
        operationLog.setCreateTime(LocalDateTime.now());
        operationLogDao.save(operationLog);
    }

}
