
package com.newland.spring.platcore.utils;


import com.newland.spring.platcore.log.Log;
import com.newland.spring.platcore.log.LogFactory;
import com.newland.spring.platcore.log.LogProperty;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Copyright (c) 2015, NEWLAND , LTD All Rights Reserved.
 *
 * @ClassName:LogUtils
 * @Description: 日志处理工具类
 * @Funtion List:TODO 主要函数及其功能
 *
 * @author   chenzc
 * @version  
 * @Date	 2015年7月2日		下午5:22:19
 *	 
 * @History: // 历史修改记录 
 * <author>  // 作者
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述
 */
public class LogUtils {
    
    private static final Log log = LogFactory.getLogger(LogUtils.class);
    /**
     * 
     * @Function:     getObjFiledsInfo 
     * @Description:  获取对象属性及值信息
     *
     * @param obj
     * @return
     */
    public static String  getObjFiledsInfo(Object obj){
        
        if(obj == null) return "";
        Class<?>  c = obj.getClass();
        
        StringBuffer buf = new StringBuffer(" Class=");
        buf.append(c.getName());
        buf.append(", params=[");
        
        Field[] fs = c.getDeclaredFields();
        if(fs != null) {
            
            Object ob = "";
            for(int i=0;i<fs.length;i++){
                Field f = fs[i];
                f.setAccessible(true);
                //过滤static 或 final 修饰属性
                if(Modifier.isFinal(f.getModifiers()) || 
                        Modifier.isStatic(f.getModifiers()))
                    continue;
                
                try {
                    ob = f.get(obj);
                }catch(Exception e){
                    log.warn(LogProperty.LOGTYPE_DETAIL, null, e, "获取属性:",f.getName(),"值失败");
                }
                
                buf.append(f.getName())
                   .append("=").append(ob == null ? "" : ob.toString());
                
                if(i != fs.length - 1)
                   buf.append(", ");
            }
        }
        
        buf.append("]");
        
        return buf.toString();
    }
    
    /**
     * @Function:     appendLogs 
     * @Description:  添加参数信息  
     *
     * @param buf
     * @param infos
     */
    public static StringBuffer appendLogs(StringBuffer buf,Object ... infos) {
        if(buf == null){
            buf = new StringBuffer();
        }
        if(infos!=null && infos.length > 0){
            buf.append(" ");
            for(Object info : infos){
                if (info == null) {
                    buf.append("null");
                } else {
                    buf.append(info);
                    if (buf.charAt(buf.length() - 1) != '=') {
                        buf.append(", ");
                    }
                }
            }
            buf.delete(buf.length() - 2, buf.length());
        }
        return buf;
    }
    /**
     * @Function:     appendLogsStr 
     * @Description:  转换日志字符串参数信息
     *
     * @param infos
     * @return
     */
    public static String     logStrs(Object ... infos){
        return appendLogs(new StringBuffer(),infos).toString();
    }
    /**
     * @Function:     logTag 
     * @Description:  转换日志字符串参数信息
     *
     * @param infos
     * @return
     */
    public static String     logTag(String tag,Object ... infos){
        return appendLogs(new StringBuffer(tag),infos).toString();
    }
}

