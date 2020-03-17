package com.newland.spring.request.log.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.newland.spring.platcore.log.annotation.NlLog;
import com.newland.spring.platcore.utils.JsonUtils;
import com.newland.spring.request.log.ResponseThreadLocal;
import com.newland.spring.request.log.StartInit;
import com.newland.spring.request.log.kafka.KafkaLog;
import com.newland.spring.request.log.model.CmLog;
import com.newland.spring.request.model.header.ResponseHeaderVo;
import com.newland.spring.request.model.result.CommonCodeHandler;

import org.apache.commons.lang.StringEscapeUtils;
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
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Optional;


/**
 * @Auther: garfield
 * @Date: 2019/5/13 下午2:15
 * @Description:
 */
@Aspect
@Component("logAspect")
public class LogAspect {

    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    @Autowired
    private KafkaLog kafkaLog;

    // 配置织入点
    @Pointcut("@annotation(com.newland.spring.platcore.log.annotation.NlLog)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object doBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        if (StartInit.LOG_KAFKA) {
            String result = handleLog(joinPoint, null);
            log.info("post do before result:" + result);
            result = JsonUtils.getString(result);
            
            //判断非文件格式才重写参数
            if(!"java.io.File".equals(joinPoint.getArgs()[1].getClass().getName()))
            	joinPoint.getArgs()[1] = JsonUtils.getObject(result, getParamType(joinPoint));
        }
        return joinPoint.proceed(joinPoint.getArgs());

    }

    @AfterReturning(returning = "dto", pointcut = "logPointCut()")
    public void doAfterReturning(Object dto) {
        if (StartInit.LOG_KAFKA) {
            handleAfterLog(dto);
        }
    }

    /**
     * 新建一个方法用来发送错误日志
     * 判断各种可能无法记录日志的可能，记住，这里不一定报错，允许不同形式
     * 变量在启动时初始化，使用启动前类，并包装成变量类，统一变量
     * 整理日志类，使得封装的日志使用起来更加便利
     * option map filter stream
     *
     * @param dto
     */
    private void handleAfterLog(Object dto) {
        try {


            String result = JSON.toJSONString(dto);
            log.info("post response:" + result);

            String resCode = null;
            String resDesc = null;
            CmLog cmLog = ResponseThreadLocal.getTempCmLog().clone();
            int orderId = cmLog.getOrderId();


            //init cmlog in thread local variable again when you change order id

            //因为这里的请求外地址，不限制格式，直接将返回值填入日志，下面这段代码注释
            //there could be no resCode node in response body
            if(result.indexOf("\\")> 0){
            	result = JsonUtils.getStringFromJson(result);
            }
            
            
            ResponseHeaderVo responseHeaderVo = JsonUtils.getNodeObject2(result, JsonUtils.JSON_NODE_HEADER, ResponseHeaderVo.class);

            if (responseHeaderVo != null && responseHeaderVo.getRespCode() != null &&
                    responseHeaderVo.getRespDesc() != null &&
                    responseHeaderVo.getOrderId() != 0) {
                resCode = responseHeaderVo.getRespCode();
                resDesc = responseHeaderVo.getRespDesc();
                orderId = responseHeaderVo.getOrderId() + 1;
            }
            cmLog.initPostResponseType(resCode, resDesc, result);
            cmLog.setOrderId(orderId);
            kafkaLog.sendCmLog(cmLog);
            ResponseThreadLocal.setTempCmLog(cmLog);
            /**
             * 由于内部调用改变了orderId，所以进行一次跳跃
             */
            ResponseThreadLocal.changeOrder(cmLog.getOrderId() + 1);

        } catch (Exception exp) {
            log.error("异常信息:{}", exp.getMessage());
            kafkaLog.minorError(CommonCodeHandler.RESULT_CODE_POST_RESPONSE_LOG, CommonCodeHandler.RESULT_DESC_POST_RESPONSE_LOG, exp, exp.getMessage());
        }
    }

    private String handleLog(JoinPoint joinPoint, Exception e) {

        String bodyStr = JsonUtils.getString2(joinPoint.getArgs()[1]);
        log.info("params:" + bodyStr);
        if(bodyStr.indexOf("\\")> 0){
        	bodyStr = JsonUtils.getStringFromJson(bodyStr);
        }
        try {
            // 获得注解
            NlLog controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return bodyStr;
            }
            String action = controllerLog.action();
            Object[] args = joinPoint.getArgs();

            switch (action) {
                case "postForObject": {
                    String url = (String) args[0];
                    String params = (String) args[1];
                    CmLog cmLogLocal = ResponseThreadLocal.getCmLog();
                    if(cmLogLocal == null){
                    	return bodyStr;
                    }
                    CmLog cmLog = cmLogLocal.clone();
                    cmLog.initPostRequestType(params, url, action, "", cmLog.getTraceId());
                    kafkaLog.sendCmLog(cmLog.clone());
                    ResponseThreadLocal.setTempCmLog(cmLog);
//                    bodyStr = JsonUtils.getStringFromJson(bodyStr);
                    if (JsonUtils.getNodeObject2(bodyStr, JsonUtils.JSON_NODE_HEADER, ResponseHeaderVo.class) == null) {
                        return bodyStr;
                    }
                    bodyStr = JsonUtils.modifyHeaderNodeValue(bodyStr, JsonUtils.JSON_NODE_ORDER_ID, String.valueOf(cmLog.getOrderId()));
                    return bodyStr;
                }
            }
        } catch (Exception exp) {
        	exp.printStackTrace();
            log.error("异常信息:{}", exp.getMessage());
            kafkaLog.minorError(CommonCodeHandler.ERROR_CODE_POST_REQUEST_LOG, CommonCodeHandler.ERROR_DESC_POST_REQUEST_LOG, exp, exp.getMessage());
        }
        return bodyStr;
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private static NlLog getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        return Optional.ofNullable(method).map(v -> v.getAnnotation(NlLog.class)).orElse(null);
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

