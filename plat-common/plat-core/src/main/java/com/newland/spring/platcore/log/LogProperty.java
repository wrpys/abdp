package com.newland.spring.platcore.log;

/**
 * 日志属性
 * 
 * @author Administrator
 * 
 */
public class LogProperty {
    /**
     * 日志类型
     * 请求
     */
    public static final String LOGTYPE_REQUEST = "REQ";

    /**
     * 日志类型
     *  调用
     */
    public static final String LOGTYPE_CALL = "CALL";

    /**
     * 日志类型
     *  系统
     */
    public static final String LOGTYPE_SYS = "SYS";

    /**
     * 日志类型
     *  明细
     */
    public static final String LOGTYPE_DETAIL = "DTL";

    /**
     * 配置属性
     * 时间戳
     */
    public static final String LOGCONFIG_TIMESTAMP = "timestamp";

    /**
     * 配置属性
     * 日志类型
     */
    public static final String LOGCONFIG_TYPE = "logtype";

    /**
     * 配置属性
     * 交互ID
     */
    public static final String LOGCONFIG_DEALID = "dealid";

    /**
     * 配置属性
     * 异常编号
     */
    public static final String LOGCONFIG_ERRORNO = "errorno";

    /**
     * 配置属性
     * 包名+类名+方法名+行号
     */
    public static final String LOGCONFIG_CODELINE = "codeline";

    /**
     * 配置属性
     * 会话ID
     */
    public static final String LOGCONFIG_SESSIONID = "sessionid";

    /**
     * 配置属性
     * 日志按线程ID
     */
    public static final String LOGCONFIG_THREAD_ID = "ThreadID";

    /**
     * 配置属性
     * 日志按线程名
     */
    public static final String LOGCONFIG_THREAD_NAME = "ThreadName";
}
