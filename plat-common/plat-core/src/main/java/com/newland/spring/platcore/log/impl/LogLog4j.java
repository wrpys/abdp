package com.newland.spring.platcore.log.impl;
//


import com.newland.spring.platcore.log.ErrorStack;
import com.newland.spring.platcore.log.Log;
import com.newland.spring.platcore.log.LogProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;

//import com.newland.spring.platcore.log.startLog.StartLog;

@SuppressWarnings({"rawtypes"})
public class LogLog4j implements Log {

    private Logger log;

    //需过滤的堆栈包
    private String filterPage;

    private LogLog4j() {
        log = LogManager.getRootLogger();
    }

    private LogLog4j(Class clazz) {
        log = LogManager.getLogger(clazz);
    }

    private LogLog4j(String name) {
        log = LogManager.getLogger(name);
    }

    public static Log getRootLogger() {
        return new LogLog4j();
    }

    public static Log getRootLogger(String filterPage) {
        LogLog4j logLog4j = new LogLog4j();
        logLog4j.setFilterPage(filterPage);
        return logLog4j;
    }

    public static Log getLogger(Class clazz) {
        return new LogLog4j(clazz);
    }

    public static Log getLogger(Class clazz, String filterPage) {
        LogLog4j logLog4j = new LogLog4j(clazz);
        logLog4j.setFilterPage(filterPage);
        return logLog4j;
    }

    public static Log getLogger(String name) {
        return new LogLog4j(name);
    }

    public static Log getLogger(String name, String filterPage) {
        LogLog4j logLog4j = new LogLog4j(name);
        logLog4j.setFilterPage(filterPage);
        return logLog4j;
    }

    public void loadConfig(String configFileNeme) {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.setConfigLocation(new File(configFileNeme).toURI());
        context.reconfigure();
    }

    public String toString(String... msgs) {
        if (msgs == null)
            return null;
        StringBuffer buf = new StringBuffer();
        for (String msg : msgs)
            buf.append(msg);
        return buf.toString();
    }

    /**
     * 输出跟踪级别日志
     *
     * @param key  日志类别(或关键字)
     * @param msgs 日志内容
     */
    public void trace(String key, String... msgs) {
        putMDC(key, "");
        String str = printStartLog(msgs);
        if (str == null)
            str = toString(msgs);
        log.trace(str);
        removeMDC();
    }

    /**
     * 输出调试级别日志
     *
     * @param key  日志类别
     * @param msgs 日志内容
     */
    public void debug(String key, String... msgs) {
        String str = printStartLog(msgs);
        if (log.isDebugEnabled()) {
            putMDC(key, "");
            if (str == null)
                str = toString(msgs);
            log.debug(str);
            removeMDC();
        }
    }

    /**
     * 输出普通级别日志
     *
     * @param key  日志类别
     * @param msgs 日志内容
     */
    public void info(String key, String... msgs) {
        String str = printStartLog(msgs);
        if (log.isInfoEnabled()) {
            putMDC(key, "");
            if (str == null)
                str = toString(msgs);
            log.info(str);
            removeMDC();
        }
    }

    /**
     * 输出警告级别日志
     *
     * @param key  日志类别
     * @param msgs 日志内容
     */
    public void warn(String key, String... msgs) {
        String str = printStartLog(msgs);
        if (log.isWarnEnabled()) {
            putMDC(key, "");
            if (str == null)
                str = toString(msgs);
            log.warn(str);
            removeMDC();
        }
    }

    /**
     * 输出警告级别日志
     *
     * @param key  日志类别
     * @param msgs 日志内容
     * @param t    异常堆栈
     */
    public void warn(String key, String errorCode, Throwable t, String... msgs) {
        warnLog(key, errorCode, ErrorStack.getErrorMsg(t), msgs);
    }

    /**
     * 输出错误级别日志
     *
     * @param key  日志类别
     * @param msgs 异常信息
     * @param t    异常堆栈
     */
    public void error(String key, String errorCode, Throwable t, String... msgs) {
        errorLog(key, errorCode, ErrorStack.getErrorMsg(t), msgs);
    }

    /**
     * 输出致命级别日志
     *
     * @param key  日志类别
     * @param msgs 异常信息
     * @param t    异常堆栈
     */
    public void fatal(String key, String errorCode, Throwable t, String... msgs) {
        fatalLog(key, errorCode, ErrorStack.getErrorMsg(t), msgs);
    }

    private void putMDC(String key, String errorCode) {
//        if (errorCode == null)
//            errorCode = "";
        //        ThreadContext.put(LogProperty.LOGCONFIG_THREAD_ID, String.valueOf(Thread.currentThread().getId()));
        ThreadContext.put(LogProperty.LOGCONFIG_THREAD_NAME, Thread.currentThread().getName());
        ThreadContext.put(LogProperty.LOGCONFIG_TYPE, key);
        //        ThreadContext.put(LogProperty.LOGCONFIG_TIMESTAMP, String.valueOf(System.nanoTime()));
        if (errorCode != null)
            ThreadContext.put(LogProperty.LOGCONFIG_ERRORNO, errorCode);
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        for (int i = 0; i < stacks.length; i++) {
            String className = stacks[i].getClassName();
            if (className.indexOf("com.newland.dc.log.impl.LogLog4j") != -1) {
                continue;
            }
            if (filterPage != null) {
                if (className.indexOf(filterPage) != -1) {
                    continue;
                }
            }
            sb.append(stacks[i].getClassName())
                    .append(".")
                    .append(stacks[i].getMethodName())
                    .append("(")
                    .append(stacks[i].getFileName())
                    .append(":")
                    .append(stacks[i].getLineNumber())
                    .append(")");
            break;
        }
        ThreadContext.put(LogProperty.LOGCONFIG_CODELINE, sb.toString());
    }

    /**
     * @return
     * @Function:     isDebugEnabled 
     * @Description:   用来判断是否开启了debug
     */
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    public void setFilterPage(String filterPage) {
        this.filterPage = filterPage;
    }

    //	private final boolean isDebugEnabled=com.newland.dc.log.isDebugEnabled();
    private void removeMDC() {
        //        ThreadContext.clearMap();
        //		ThreadContext.remove(LogProperty.LOGCONFIG_THREAD_ID);
        ThreadContext.remove(LogProperty.LOGCONFIG_THREAD_NAME);
        //		ThreadContext.remove(LogProperty.LOGCONFIG_TYPE);
        ThreadContext.remove(LogProperty.LOGCONFIG_ERRORNO);
        //		ThreadContext.remove(LogProperty.LOGCONFIG_TIMESTAMP);
        ThreadContext.remove(LogProperty.LOGCONFIG_CODELINE);
    }

    public void warnLog(String key, String errorCode, String expStackTraces, String... msgs) {
        String str = printStartLog(expStackTraces, msgs);
        if (log.isWarnEnabled()) {
            putMDC(key, errorCode);
            if (str == null) {
                str = toString(msgs);
                if (expStackTraces != null) {
                    str += "\n" + expStackTraces;
                }
            }
            log.warn(str);
            removeMDC();
        }
    }

    public void errorLog(String key, String errorCode, String expStackTraces, String... msgs) {
        String str = printStartLog(expStackTraces, msgs);
        if (str == null) {
            str = toString(msgs);
            if (expStackTraces != null) {
                str += "\n" + expStackTraces;
            }
        }
        putMDC(key, errorCode);
        log.error(str);
        removeMDC();
    }

    public void fatalLog(String key, String errorCode, String expStackTraces, String... msgs) {
        String str = printStartLog(expStackTraces, msgs);
        if (str == null) {
            str = toString(msgs);
            if (expStackTraces != null) {
                str += "\n" + expStackTraces;
            }
        }
        putMDC(key, errorCode);
        log.fatal(str);
        removeMDC();
    }

    /**
     * 启动日志打印
     */
    private String printStartLog(String... msgs) {
        return printStartLog(null, msgs);
    }

    private String printStartLog(String expStackTraces, String... msgs) {
        return null;
    }

    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    public boolean isTraceEnabled() {
        return log.isTraceEnabled();

    }

    public void addMDC(String key, String value) {
        ThreadContext.put(key, value);
    }

    public void rmMDC(String key) {
        ThreadContext.remove(key);
    }

    public String getMDC(String key) {
        return ThreadContext.get(key);
    }
}
