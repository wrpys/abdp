package com.newland.spring.request.model.header;

import java.io.Serializable;

/**
 * 用户请求报文头
 *
 * @author Li Siqi
 * @since 1.0.0-SNAPSHOT
 */
public class BaseRequestHeaderVo implements Serializable {

    private String requestSeq;  // 请求序列号
    private String requestTime; // 请求时间，格式：’yyyymmddhh24miss’
    private String appId;       // 应用ID
    private String appVersion;  // 版本号
    private String userId;      // 用户ID，未登录则为空
    private int orderId;        // 日志跟踪ID

    public String getRequestSeq() {
        return requestSeq;
    }

    public void setRequestSeq(String requestSeq) {
        this.requestSeq = requestSeq;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


}
