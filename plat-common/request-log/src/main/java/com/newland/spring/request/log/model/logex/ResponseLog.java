package com.newland.spring.request.log.model.logex;


import com.newland.spring.request.log.model.AllLogBase;

/**
 * Created by garfield on 2018/1/23.
 */
public class ResponseLog extends AllLogBase {


    public ResponseLog(int type, String aspectLogId, String requestMethod, String level, String message) {
        super(type, level);
//        this.userId = userId;
        this.aspectLogId = aspectLogId;
        this.requestMethod = requestMethod;
        this.message = message;
    }

}
