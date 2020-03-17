package com.newland.spring.request.log.model;


import com.newland.spring.platcore.utils.JsonUtils;
import com.newland.spring.platcore.utils.TimeUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Date;


/**
 * @Auther: garfield
 * @Date: 2019/3/8 14:55
 * @Description:
 */
public class CmLog implements Cloneable {


    private int type;
    private String threadName;
    private String requestUrl;
    private String remoteAddr;
    private String requestMethod;
    private String level;
    private String content;
    private Date createTime;

    private String serviceName;
    private String traceId;

    private String errorCode;
    private String exceptionMessage;


    private String startTime;
    private Long startTimeLong;


    private Long endTimeLong;
    private String endTime;
    private Long costTime;
    private String resultCode;
    private String resultDesc;


    private int orderId;


    //系统改造，添加几个字段
    //这个是为了记录全流程时
    private String requestParams;
    private String responseBody;
    //记录机构
    private String appId;

    public String getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(String requestParams) {
        this.requestParams = requestParams;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getStartTimeLong() {
        return startTimeLong;
    }

    public void setStartTimeLong(Long startTimeLong) {
        this.startTimeLong = startTimeLong;
    }

    public Long getEndTimeLong() {
        return endTimeLong;
    }

    public void setEndTimeLong(Long endTimeLong) {
        this.endTimeLong = endTimeLong;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        this.requestMethod = requestMethod;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultDesc() {
        return resultDesc;
    }

    public void setResultDesc(String resultDesc) {
        this.resultDesc = resultDesc;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


    public CmLog() {
        this.createTime = new Date();
    }

    private static String LOG_LEVEL_INFO = "info";
    private static String LOG_LEVEL_DEBUG = "debug";


    public static int LOG_TYPE_GATEWAY_REQUEST = 101;

    public static int LOG_TYPE_GATEWAY_RESPONSE = 102;

    public static int LOG_TYPE_AOP_RESPONSE = 202;

    public static int LOG_TYPE_AOP_REQUEST = 201;

    public static int LOG_TYPE_RUNTIME_DEBUG = 210;

    public static int LOG_TYPE_RUNTIME_INFO = 211;

    public static int LOG_TYPE_RUNTIME_WARN = 212;


    public static int LOG_TYPE_RESULT = 401;

    public static int LOG_TYPE_POST_REQUEST = 301;

    public static int LOG_TYPE_POST_RESPONSE = 302;

    public static int LOG_TYPE_ERROR_MINOR = 801;
    public static int LOG_TYPE_ERROR_MAJOR = 802;


    /**
     * when there exist 999 type of log, please check the log system
     */
    public static int LOG_TYPE_ERROR_SYSTEM = 999;


    public static int LOG_TYPE_REQUEST_PARAMS_TRANSFORM = 501;

    public static int LOG_TYPE_RESPONSE_PARAMS_TRANSFORM = 502;


    public void initAopRequestType(String args, String url, String method, String addr, String traceId) {
        this.type = LOG_TYPE_AOP_REQUEST;
        initRequestInfo(args, url, method, addr, traceId);
    }

    public void initPostRequestType(String args, String url, String method, String addr, String traceId) {
        initRequestInfo(args, url, method, addr, traceId);
        this.type = LOG_TYPE_POST_REQUEST;
    }

    public void initAopResponseType(String resultCode, String resultDesc, String responseBody) throws ParseException {
        initResponseInfo(resultCode, resultDesc, responseBody);
        this.type = LOG_TYPE_AOP_RESPONSE;
    }

    public void initPostResponseType(String resultCode, String resultDesc, String responseBody) throws ParseException {
        initResponseInfo(resultCode, resultDesc, responseBody);
        this.type = LOG_TYPE_POST_RESPONSE;
    }

    public void initRuntimeDebugType(String... strings) {
        initRuntimeInfo(bufferToString(strings));
        this.level = "debug";
        this.type = LOG_TYPE_RUNTIME_DEBUG;
    }

    public void initRuntimeInfoType(String... strings) {
        initRuntimeInfo(bufferToString(strings));
        this.level = "info";
        this.type = LOG_TYPE_RUNTIME_INFO;
    }

    public void initRuntimeWarnType(String... strings) {
        initRuntimeInfo(bufferToString(strings));
        this.level = "warn";
        this.type = LOG_TYPE_RUNTIME_WARN;
    }


    public void initMinorErrorType(String errorCode, String errorDesc, Throwable t, String... strings) {
        this.type = LOG_TYPE_ERROR_MINOR;
        initErrorLog(errorCode, errorDesc, t, strings);
    }

    public void initMajorErrorType(String errorCode, String errorDesc, Throwable t, String... strings) {
        this.type = LOG_TYPE_ERROR_MAJOR;
        initErrorLog(errorCode, errorDesc, t, strings);
    }

    public void initSystemErrorType() {
        this.type = LOG_TYPE_ERROR_SYSTEM;
    }

    public void initRequestParamsTransformType(String requestParams) {
        this.type = LOG_TYPE_REQUEST_PARAMS_TRANSFORM;
        this.requestParams = requestParams;
    }

    public void initResponseParamsTransformType(String responseBody) {
        this.type = LOG_TYPE_RESPONSE_PARAMS_TRANSFORM;
        this.responseBody = responseBody;
    }

    public void initResultInfoType(String requestParams) {
        this.type = LOG_TYPE_RESULT;
        this.requestParams = requestParams;
    }


    private void initRequestInfo(String args, String url, String method, String addr, String traceId) {
        this.threadName = Thread.currentThread().getName();
        this.startTime = TimeUtils.getStringTime(new Date());
        this.startTimeLong = System.currentTimeMillis();
        this.requestUrl = url;
        this.remoteAddr = addr;
        this.requestMethod = method;
        this.traceId = traceId;
        this.level = LOG_LEVEL_INFO;


        this.requestParams = args;

        /**
         * appId is allowed to be null, coz when args json may be lack of appId node
         */
        if (this.appId == null)
            this.appId = JsonUtils.getHeaderValue(args, "appId");
    }

    public void initGatewayRequestInfoType(String args, String url, String method, String addr, String traceId) {
        initRequestInfo(args, url, method, addr, traceId);
        this.orderId = 1;
        this.type = LOG_TYPE_GATEWAY_REQUEST;
    }


    public CmLog initOrder() {
        ++this.orderId;
        return this;
    }

    private void initResponseInfo(String resultCode, String resultDesc, String responseBody) throws ParseException {
        this.endTime = TimeUtils.getStringTime(new Date());
        this.endTimeLong = System.currentTimeMillis();
        this.costTime = this.endTimeLong - this.startTimeLong;
        this.resultCode = resultCode;
        this.resultDesc = resultDesc;
        this.responseBody = responseBody;
        this.level = LOG_LEVEL_INFO;
        this.requestParams = null;
    }

    public void initGatewayResponseInfoType(String resultCode, String resultDesc, String responseBody) throws ParseException {
        initResponseInfo(resultCode, resultDesc, responseBody);
        this.type = LOG_TYPE_GATEWAY_RESPONSE;
    }


    private void initRuntimeInfo(String content) {
        this.content = content;
    }


    public CmLog initErrorLog(String errorCode, String errorDesc, Throwable t, String... strings) {
        this.threadName = Thread.currentThread().getName();
        this.level = "error";
        this.endTime = TimeUtils.getStringTime(new Date());
        this.endTimeLong = System.currentTimeMillis();
        if (this.startTimeLong != null)
            this.costTime = this.endTimeLong - this.startTimeLong;
        this.content = errorDesc;
        this.errorCode = errorCode;
        this.exceptionMessage = printStackTraceToString(t);
        this.resultCode = errorCode;
        this.content = bufferToString(strings);
        this.resultDesc = this.content;
        return this;
    }

    private static String printStackTraceToString(Throwable t) {
        if (t != null) {
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw, true));
            return sw.getBuffer().toString();
        }
        return null;
    }

    private String bufferToString(String... strings) {
        StringBuffer stringBuffer = new StringBuffer();
        for (String string : strings) {
            stringBuffer.append(string);
        }
        return stringBuffer.toString();
    }

    public CmLog clone() {
        CmLog o = null;
        try {
            o = (CmLog) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }


    @Override
    public String toString() {
        return "CmLog{" +
                "type=" + type +
                ", threadName='" + threadName + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", remoteAddr='" + remoteAddr + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", level='" + level + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", serviceName='" + serviceName + '\'' +
                ", traceId='" + traceId + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", exceptionMessage='" + exceptionMessage + '\'' +
                ", startTime='" + startTime + '\'' +
                ", startTimeLong=" + startTimeLong +
                ", endTimeLong=" + endTimeLong +
                ", endTime='" + endTime + '\'' +
                ", costTime=" + costTime +
                ", resultCode='" + resultCode + '\'' +
                ", resultDesc='" + resultDesc + '\'' +
                ", orderId=" + orderId +
                ", requestParams='" + requestParams + '\'' +
                ", responseBody='" + responseBody + '\'' +
                ", appId='" + appId + '\'' +
                '}';
    }
}
