package com.newland.spring.platcore.log;

/**
 * 日志接口
 * 
 * @author Administrator
 * 
 */
public interface Log {
    /**
     * 输出跟踪级别日志
     * 
     * @param key 日志类别(或关键字)
     * @param msgs 日志内容
     */
    public void trace(String key, String... msgs);

    /**
     * 输出调试级别日志
     * 
     * @param key 日志类别(或关键字)
     * @param msgs 日志内容
     */
    public void debug(String key, String... msgs);

    /**
     * 输出普通级别日志
     * 
     * @param key 日志类别(或关键字)
     * @param msgs 日志内容
     */
    public void info(String key, String... msgs);

    /**
     * 输出警告级别日志
     * 
     * @param key 日志类别(或关键字)
     * @param msg 日志内容
     */
    public void warn(String key, String... msg);

    /**
     * 输出警告级别日志
     * 
     * @param key 日志类别(或关键字)
     * @param errorCode 错误码
     * @param t 异常堆栈
     * @param msgs 日志内容
     */
    public void warn(String key, String errorCode, Throwable t, String... msgs);
    
    /**
     * 输出警告级别日志
     * 
     * @param key 日志类别(或关键字)
     * @param errorCode 异常编码
     * @param expStackTraces 具体异常堆栈信息
     * @param msgs 异常信息
     */
    public void warnLog(String key, String errorCode, String expStackTraces, String... msgs);

    /**
     * 输出错误级别日志
     * 
     * @param key 日志类别(或关键字)
     * @param errorCode 错误码
     * @param t 异常堆栈
     * @param msgs 异常信息
     */
    public void error(String key, String errorCode, Throwable t, String... msgs);
    
    /**
     * 输出错误级别日志
     * 
     * @param key 日志类别(或关键字)
     * @param errorCode 异常编码
     * @param expStackTraces 具体异常堆栈信息
     * @param msgs 异常信息
     */
    public void errorLog(String key, String errorCode, String expStackTraces, String... msgs);

    /**
     * 输出致命级别日志
     * 
     * @param key 日志类别(或关键字)
     * @param errorCode 错误码
     * @param msgs 异常信息
     * @param t 异常堆栈
     */
    public void fatal(String key, String errorCode, Throwable t, String... msgs);
    
    /**
     * 输出致命级别日志
     * 
     * @param key 日志类别(或关键字)
     * @param errorCode 异常编码
     * @param expStackTraces 具体异常堆栈信息
     * @param msgs 异常信息
     */
    public void fatalLog(String key, String errorCode, String expStackTraces, String... msgs);

    /**
     * 动态加载配置文件
     * 
     * @param configFileNeme 配置文件绝对路径
     */
    public void loadConfig(String configFileNeme);

    /**
     * 
     * @Function:     isDebugEnabled 
     * @Description:   用来判断是否开启了debug
     *
     * @return
     */
    public boolean isDebugEnabled();

    /**
     * 
     * @Function:     isInfoEnabled 
     * @Description:   用来判断是否开启了debug
     *
     * @return
     */
    public boolean isInfoEnabled();
    
    /**
     * @Function:     isWarnEnabled 
     * @Description:   用来判断是否开启了warn
     *                 <功能详细描述>
     *
     * @return
     */
    public boolean isWarnEnabled();
    
    /**
     * @Function:     isWarnEnabled 
     * @Description:   用来判断是否开启了Error
     *                 <功能详细描述>
     *
     * @return
     */
    public boolean isErrorEnabled();
    
    /**
     * @Function:     isWarnEnabled 
     * @Description:   用来判断是否开启了Trace
     *                 <功能详细描述>
     *
     * @return
     */
    public boolean isTraceEnabled();
    
    /**
     * @Function:     addMDC 
     * @Description:  添加线程MDC变量接口
     *                 <功能详细描述>
     *
     * @param key
     * @param value
     */
    public void    addMDC(String key, String value);
    
    /**
     * @Function:     rmMDC
     * @Description:  删除线程MDC变量
     *
     * @param key
     */
    public void    rmMDC(String key);
    
    /**
     * 获取MDC数据
     * @param key
     * @return
     */
    public String getMDC(String key);
}
