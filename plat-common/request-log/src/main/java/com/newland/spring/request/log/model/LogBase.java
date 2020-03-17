package com.newland.spring.request.log.model;

import java.util.Date;

/**
 * Created by garfield on 2018/1/19.
 */
public class LogBase {


    public static final int LOG_TYPE_BUSINESS_REQUEST = 1;
    public static final int LOG_TYPE_BUSINESS_RESPONSE= 2;
    public static final int LOG_TYPE_BUSINESS_MGR= 3;
    public static final int LOG_TYPE_BUSINESS_REMOTE= 4;

    public static final String LOG_LEVEL_DEBUG = "debug";
    public static final String LOG_LEVEL_INFO = "info";


    protected int type;
    protected String threadName;
    protected String aspectLogId;
    protected String requestUrl;
    protected String remoteAddr;
    protected String requestArgs;
    protected String requestMethod;
    protected String level;
    protected String message;
    protected String exception;
    protected Date createTime;

    protected String userId;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getAspectLogId() {
        return aspectLogId;
    }

    public void setAspectLogId(String aspectLogId) {
        this.aspectLogId = aspectLogId;
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

    public String getRequestArgs() {
        return requestArgs;
    }

    public void setRequestArgs(String requestArgs) {
        this.requestArgs = requestArgs;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LogBase(int type, String level){
        this.type = type;
        this.threadName = Thread.currentThread().getName();
        this.level = level;
        this.createTime = new Date();
    }

    public LogBase(){

    }

    @Override
    public String toString() {
        return "LogBase{" +
                ", type=" + type +
                ", threadName='" + threadName + '\'' +
                ", aspectLogId='" + aspectLogId + '\'' +
                ", requestUrl='" + requestUrl + '\'' +
                ", remoteAddr='" + remoteAddr + '\'' +
                ", requestArgs='" + requestArgs + '\'' +
                ", requestMethod='" + requestMethod + '\'' +
                ", level='" + level + '\'' +
                ", message='" + message + '\'' +
                ", exception='" + exception + '\'' +
                ", createTime=" + createTime +
                ", userId='" + userId + '\'' +
                '}';
    }

    //    public LogBase(int type,String aspectLogId, String requestUrl, String requestMethod, String remoteAddr, String requestArgs, String level) {
//        this.type = type;
//        this.threadName = Thread.currentThread().getName();
//        this.aspectLogId = aspectLogId;
//        this.requestUrl = requestUrl;
//        this.requestMethod = requestMethod;
//        this.remoteAddr = remoteAddr;
//        requestArgs = requestArgs.replace("\'","\"");
//        this.requestArgs = requestArgs;
//        this.level = level;
//    }
//
//    public LogBase(int type, String aspectLogId, String requestMethod, String level, String message) {
//        this.type = type;
//        this.threadName = Thread.currentThread().getName();
//        this.aspectLogId = aspectLogId;
//        this.requestMethod = requestMethod;
//        this.level = level;
//        this.message = message;
//    }

}
