package com.newland.spring.request.log.aspect;

import com.newland.spring.platcore.utils.JsonUtils;
import com.newland.spring.request.log.ResponseThreadLocal;
import com.newland.spring.request.log.StartInit;
import com.newland.spring.request.log.kafka.KafkaLog;
import com.newland.spring.request.log.model.CmLog;
import com.newland.spring.request.model.MsgReq;
import com.newland.spring.request.model.header.ResponseHeaderVo;
import com.newland.spring.request.model.result.CommonCodeHandler;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * http请求日志，对每个 com..controller.*Controller公共方法进行切面代理并为每个http请求分配一个请求id
 * 异常控制器com.nldc.auth.agency.handle.ExceptionHandle.exceptionProcess进行切面代理
 * Created by Mr.J  on 2017/12/14.
 */

/**
 * 与网关不同的是，放入线程变量的orderId已经+1,超前
 */
@Aspect
@Component
@Order(-5)
public class MethodArgumentResolver {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private KafkaLog kafkaLog;

    @Value("${app_id:10000}")
    private String appId;

    @Pointcut("execution(public * com..controller.*Controller.*(..))")
    public void webLog() {
    }


    @Around("webLog()")
    public Object doBefore(ProceedingJoinPoint joinPoint) throws Throwable {

        HttpServletRequest request = getHttpServletRequest();

        logger.debug("接收到请求,URL:{},ARGS:{}", request.getRequestURL().toString(), Arrays.toString(joinPoint.getArgs()));
        /**
         * only accept post operation
         */
        if (request.getMethod().equals("POST") && StartInit.LOG_KAFKA) {
            String result = logKafkaRequest(request, joinPoint);
//            joinPoint.getArgs()[0] = JsonUtils.getObject(result, getParamType(joinPoint));
        }
        return joinPoint.proceed(joinPoint.getArgs());
    }

    @AfterReturning(returning = "dto", pointcut = "webLog()")
    public void doAfterReturning(Object dto) {
        if (dto != null)
            responseDebug(dto);
    }

    private void responseDebug(Object dto) {
        HttpServletRequest request = getHttpServletRequest();
        if (request.getMethod().equals("POST") && StartInit.LOG_KAFKA) {
            logKafkaResponse(dto);
        }
    }

    private HttpServletRequest getHttpServletRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request;
    }

    public static HttpServletResponse getHttpServletResponse() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletResponse response = attributes.getResponse();
            return response;
        } catch (Exception e) {
            return null;
        }
    }


    private String logKafkaRequest(HttpServletRequest request, JoinPoint joinPoint) {
        String bodyStr = null;
        try {
        	Object obj = resolveRequestArg(joinPoint);
        	if(!Objects.isNull(obj)){
        		bodyStr = JsonUtils.getString(((Optional<Object>) obj).get());	
        	}else {
            	bodyStr = "{\"header\":{\"requestSeq\":\"" + System.currentTimeMillis() + "\"},\"content\":\"\"}";;
        	}
            logger.debug("--------begin to insert request log into kafka--------");

            /**
             * get request params
             * parse order id from params
             * tps: 27000->23700
             */
            String requestSeq = JsonUtils.getHeaderValue(bodyStr, JsonUtils.JSON_NODE_REQUEST_SEQ);
            int orderId = Integer.parseInt("null".equals(JsonUtils.getHeaderValue(bodyStr, JsonUtils.JSON_NODE_ORDER_ID)) ? "0" : JsonUtils.getHeaderValue(bodyStr, JsonUtils.JSON_NODE_ORDER_ID));
            CmLog cmLog = new CmLog();
            cmLog.setOrderId(orderId + 1);
            cmLog.initAopRequestType(bodyStr, request.getRequestURL().toString(), request.getMethod(), request.getRemoteAddr(), requestSeq);
            if(StringUtils.isEmpty(cmLog.getAppId()) || "null".equals(cmLog.getAppId())){
            	cmLog.setAppId(appId);
            }
            ResponseThreadLocal.initThreadLocal(cmLog, cmLog.getOrderId() + 1);
            kafkaLog.sendCmLog(cmLog.clone());
            return bodyStr;

        } catch (Exception e) {
            logger.error("kafka log error:", e);
            //不抛出异常是为了不中断程序
            kafkaLog.minorError(CommonCodeHandler.ERROR_CODE_LOG_KAFKA, CommonCodeHandler.ERROR_DESC_LOG_KAFKA, e, e.getMessage());
        }
        return bodyStr;
    }

    private Optional<Object> resolveRequestArg(JoinPoint joinPoint) {
        return Arrays.stream(joinPoint.getArgs())
                .filter(arg -> arg instanceof MsgReq)
                .findFirst();
    }

    private void logKafkaResponse(Object dto) {

        try {
            CmLog cmLog = ResponseThreadLocal.getCmLog();
            ResponseHeaderVo responseHeaderVo = JsonUtils.getNodeObject(JsonUtils.getString(dto), JsonUtils.JSON_NODE_HEADER, ResponseHeaderVo.class);
            String respDesc = responseHeaderVo.getRespDesc();
            String respCode = responseHeaderVo.getRespCode();
            cmLog.initAopResponseType(respCode, respDesc, JsonUtils.getString(dto));
            ResponseThreadLocal.orderRun();
            kafkaLog.sendCmLog(cmLog);
            logger.debug("--------end inserting response log into kafka--------");
        } catch (Exception e) {
            logger.error("kafka log error:", e);
            //不抛出异常是为了不中断程序
            kafkaLog.minorError(CommonCodeHandler.ERROR_CODE_LOG_KAFKA, CommonCodeHandler.ERROR_DESC_LOG_KAFKA, e, e.getMessage());
        }
    }


    /**
     * @return
     * @throws
     * @description 获取方法参数类型，通过反射目标类的方法
     * @author Hux
     * @date 2019-03-20 20:18
     */
    private Type getParamType(ProceedingJoinPoint joinPoint) {
        Signature sig = joinPoint.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = joinPoint.getTarget();
        Method currentMethod = null;
        try {
            currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        Type[] o = currentMethod.getGenericParameterTypes();
        return o[0];
    }

}
