package com.newland.spring.request.log.model;

/**
 * Created by garfield on 2018/9/27.
 */
public class Result {


    private String serviceName;
    private String businessSeq;
    private String traceId;
    private int totalTime;
    private String beginTime;
    private String endTime;
    private String resultContent;
    private String resultCode;


    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getBusinessSeq() {
        return businessSeq;
    }

    public void setBusinessSeq(String businessSeq) {
        this.businessSeq = businessSeq;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(int totalTime) {
        this.totalTime = totalTime;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Result{" +
                "serviceName='" + serviceName + '\'' +
                ", businessSeq='" + businessSeq + '\'' +
                ", traceId='" + traceId + '\'' +
                ", totalTime=" + totalTime +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", resultContent='" + resultContent + '\'' +
                ", resultCode='" + resultCode + '\'' +
                '}';
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getResultContent() {
        return resultContent;
    }

    public void setResultContent(String resultContent) {
        this.resultContent = resultContent;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}