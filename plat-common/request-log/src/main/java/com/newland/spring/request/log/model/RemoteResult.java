package com.newland.spring.request.log.model;

/**
 * Created by garfield on 2018/9/28.
 */
public class RemoteResult extends Result {

    private Long longBeginTime;

    private String url;

    private String args;

    private String remoteAddr;

    private String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public Long getLongBeginTime() {
        return longBeginTime;
    }

    public void setLongBeginTime(Long longBeginTime) {
        this.longBeginTime = longBeginTime;
    }
}
