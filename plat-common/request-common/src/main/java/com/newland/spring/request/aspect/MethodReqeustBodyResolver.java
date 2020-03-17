package com.newland.spring.request.aspect;

import com.newland.spring.platcore.utils.JsonUtils;
import com.newland.spring.request.interceptor.RequestContext;
import com.newland.spring.request.model.MsgReq;
import com.newland.spring.request.model.header.RequestHeaderVo;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Aspect
@Component
@Order(-1)
public class MethodReqeustBodyResolver {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Pointcut("execution(public * com..controller.*Controller.*(..))")
    public void requestHeader() {
    }


    @Around("requestHeader()")
    public Object doBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
             /*
            获取请求头信息存入线程变量中
             */
            RequestHeaderVo headerVo = JsonUtils.getNodeObject(JsonUtils.getString(resolveRequestArg(joinPoint).get()), "header", RequestHeaderVo.class);
            RequestContext.setRequestHeader(headerVo);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("请求消息header解析失败，存入线程变量失败");
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }

    private Optional<Object> resolveRequestArg(JoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getArgs())
                .filter(arg -> arg instanceof MsgReq)
                .findFirst();
    }

}
