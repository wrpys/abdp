package com.newland.spring.request.log;

import com.newland.spring.request.log.model.CmLog;

/**
 * Created by garfield on 2018/4/23.
 */

/**
 * 使用线程变量时，将无法为多线程流程提供日志记录
 */
public class ResponseThreadLocal {
    private static ThreadLocal<CmLog> CM_LOG = new ThreadLocal<>();

    private static ThreadLocal<CmLog> TEMP_CM_LOG = new ThreadLocal<>();

    private static ThreadLocal<Integer> order = new ThreadLocal<>();



    public static CmLog getCmLog() {
        checkOrder();
        if(CM_LOG.get() == null){
            return null;
        }
        CM_LOG.get().setOrderId(order.get());
        return CM_LOG.get().clone();
    }

    public static void setCmLog(CmLog cmLog) {
        orderRun();
        CM_LOG.set(cmLog);
    }




    public static CmLog getTempCmLog() {
        checkOrder();
        TEMP_CM_LOG.get().setOrderId(order.get());
        return TEMP_CM_LOG.get().clone();
    }

    public static void setTempCmLog(CmLog cmLog) {
        orderRun();
        TEMP_CM_LOG.set(cmLog);
    }


    /**
     * sometimes there is no need to set other attributes to cmlog except order id
     * caz it's create-time, request-params variables need to remain original value
     * if you have to change the original cmlog, calling post() for example,
     * invoke setCmLog(CmLog cmLog), so as other similar situations that changing request parameters
     * or changing request url
     */
    public static void orderRun(){
        order.set(order.get() + 1);
    }

    private static void setOrder(int orderId){
        order.set(orderId);
    }

    public static void changeOrder(int orderId){
        order.set(orderId);
    }

    public static void initThreadLocal(CmLog cmLog,int orderId){
        setOrder(orderId);
        CM_LOG.set(cmLog);
        TEMP_CM_LOG.set(cmLog);
    }

    private static void checkOrder(){
        if(order.get() == null){
            order.set(1);
        }
    }

}
