package com.newland.spring.request.log.model.logex;


import com.newland.spring.request.log.model.AllLogBase;

/**
 * Created by garfield on 2018/1/23.
 */
public class RequestLog extends AllLogBase {



    public RequestLog(int type,String aspectLogId, String requestUrl, String requestMethod, String remoteAddr, String requestArgs, String level) {
        super(type, level);
        this.aspectLogId = aspectLogId;
        this.requestUrl = requestUrl;
        this.requestMethod = requestMethod;
        this.remoteAddr = remoteAddr;
        requestArgs = requestArgs.replace("\'","\"");
        this.requestArgs = requestArgs;
    }
}
