package com.newland.spring.request.exception;

import com.newland.spring.platcore.exceptions.CommonException;
import com.newland.spring.platcore.log.Log;
import com.newland.spring.platcore.log.LogFactory;
import com.newland.spring.request.interceptor.RequestContext;
import com.newland.spring.request.model.MsgResp;
import com.newland.spring.request.model.header.ResponseHeaderVo;
import com.newland.spring.request.model.result.CommonCodeHandler;
import com.newland.spring.request.model.result.HttpResult;
import com.newland.spring.request.model.result.ResultCode;
import org.joda.time.LocalDateTime;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author: create garfield
 * @version: v1.0
 * @description: com.newland.dc.resolver
 * @date:2019/6/24/**
 * @Auther: garfield
 * @Date: 2019/6/24 14:40
 * @Description:
 */
@ControllerAdvice
@ResponseBody
public class CommonExceptionHandler<ServiceException extends CommonException> {

    private final Log log = LogFactory.getLogger(CommonExceptionHandler.class);


    private CommonExceptionHandler() throws Exception {
        // TODO: 2019/9/18
        CommonCodeHandler.init();
    }

    @ExceptionHandler(CommonException.class)  //业务抛出的异常
    public HttpResult unknownHandler(ServiceException e) throws Exception {
        String traceId = null;
        MsgResp responseDTO = new MsgResp();
        ResponseHeaderVo headerVo = new ResponseHeaderVo(RequestContext.getRequestHeader());
        responseDTO.setHeader(headerVo);
        headerVo.setRespCode(e.getCode());
        headerVo.setResponseTime(LocalDateTime.now().toString("yyyyMMddHHmmss"));
        headerVo.setRespDesc(e.getMessage());
//        log.error("业务异常:" + e.getMessage(), e);
        return ResultCode.newResult().setResultHeader(headerVo);
    }

    @ExceptionHandler(Exception.class)  //系统抛出的未知异常
    public HttpResult unknownHandler(Exception e) throws Exception {
        if (e!=null && e.getMessage()!=null && e.getMessage().indexOf("CommonException") > -1) {
            e.printStackTrace();
            return unknownHandler((ServiceException) getCommonException((Exception) e.getCause()));
        }
        String traceId = null;
        String msg = "系统异常，请求联系管理员！";
        ResponseHeaderVo headerVo = new ResponseHeaderVo(RequestContext.getRequestHeader());
        headerVo.setRespCode(CommonCodeHandler.ERROR_CODE_PRE + CommonCodeHandler.SYS_ERROR_CODE);
        headerVo.setResponseTime(LocalDateTime.now().toString("yyyyMMddHHmmss"));
        headerVo.setRespDesc(msg);
        log.error("error", CommonCodeHandler.ERROR_CODE_PRE + CommonCodeHandler.SYS_ERROR_CODE, e, msg);
        return ResultCode.newResult().setResultHeader(headerVo);
    }

    private static CommonException getCommonException(Exception e) {
        if (e instanceof CommonException) return (CommonException) e;
        return getCommonException((Exception) e.getCause());
    }

}
