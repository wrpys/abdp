package com.newland.spring.request.log.model.logex;

import com.newland.spring.request.log.model.AllLogBase;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by garfield on 2018/1/24.
 */
public class MgrLog extends AllLogBase {

    public MgrLog(int type, String level, String message, String exception) {
        super(type, level);
        this.message = message;
        this.exception = exception;
    }

    public MgrLog(String level, String service, String ...strings) {
        super(AllLogBase.LOG_TYPE_BUSINESS_MGR, level);
        StringBuffer stringBuffer = new StringBuffer();
        for (String string : strings) {
            stringBuffer.append(string);
        }
        this.message = String.valueOf(stringBuffer);
        this.serviceName = service;
    }

    public MgrLog(String level, String service, String error, Throwable t, String ...strings) {
        super(AllLogBase.LOG_TYPE_BUSINESS_MGR, level);
        StringBuffer stringBuffer = new StringBuffer();
        for (String string : strings) {
            stringBuffer.append(string);
        }
        this.message = String.valueOf(stringBuffer);
        this.errorCode = error;
        this.serviceName = service;
        this.exception = printStackTraceToString(t);
    }

    public static String printStackTraceToString(Throwable t) {
        if(t != null){
            StringWriter sw = new StringWriter();
            t.printStackTrace(new PrintWriter(sw, true));
            return sw.getBuffer().toString();
        }
        return null;
    }
}
