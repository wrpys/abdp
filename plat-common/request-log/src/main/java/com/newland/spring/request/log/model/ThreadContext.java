package com.newland.spring.request.log.model;//package com.newland.dc.ctid;

/**
 * Created by garfield on 2018/9/27.
 */
public class ThreadContext {

    private static ThreadLocal<CommonLog> commonLogThreadLocal = new ThreadLocal<>();;


    public static CommonLog getCommonLog() {
        return commonLogThreadLocal.get();
    }

    public static void setCommonLog(CommonLog commonLog) {
        commonLogThreadLocal.set(commonLog);
    }

}
