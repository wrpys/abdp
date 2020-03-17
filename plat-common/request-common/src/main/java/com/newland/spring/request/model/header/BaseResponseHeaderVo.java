package com.newland.spring.request.model.header;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户响应报文头
 *
 * @author Li Siqi
 * @since 1.0.0-SNAPSHOT
 */
public class BaseResponseHeaderVo implements Serializable {

    private String respDesc;     // 结果描述
    private String respCode;     // 应答结果编码，成功则返回0，异常则参看返回错误代码定义
    private String requestSeq;   // 请求序列号
    private String responseTime; // 应答时间，格式：’yyyymmddhh24miss
    private String appId;
    private int orderId;         // 日志跟踪ID

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRequestSeq() {
        return requestSeq;
    }

    public void setRequestSeq(String requestSeq) {
        this.requestSeq = requestSeq;
    }

    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }


    public BaseResponseHeaderVo(RequestHeaderVo requestHeaderVo) {
        this.setOrderId(requestHeaderVo.getOrderId() + 1);
        this.setRequestSeq(requestHeaderVo.getRequestSeq());
        this.setResponseTime(new Date().toString());
    }

    public BaseResponseHeaderVo() {

    }

    public BaseResponseHeaderVo(String code,String msg){
        this.setRespCode(code);
        this.setRespDesc(msg);
    }

}
