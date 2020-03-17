package com.newland.spring.platcore.log;


import com.newland.spring.platcore.log.impl.LogLog4j;
import com.newland.spring.platcore.utils.FileListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;

/**
 * LOG工厂类
 * 支持log4j2.xml存放在classes/config目录下或者直接放在classes下
 *
 * @author Administrator
 */
@SuppressWarnings("rawtypes")
public class LogFactory {

    private static String STATIC_LOGGERCONFIG_PATH = "config/log4j2.xml";
    private static String STATIC_LOGGER_PATH = "log4j2.xml";

    private static boolean isInit = false;

    private static void initConfig() {
        if (isInit)
            return;
        initLoad();
    }

    private synchronized static void initLoad() {
        if (isInit)
            return;
        URL url = null;
        Enumeration paths;
        try {
            //没有读到配置路径文件，读classes下资源文件
            if (url == null) {
                url = LogFactory.class.getClassLoader().getResource(STATIC_LOGGERCONFIG_PATH);
                if (url == null) {
                    url = LogFactory.class.getClassLoader().getResource(STATIC_LOGGER_PATH);
                }
            }
            System.out.println("log4j2 path：LogFactory.class.getClassLoader().getResource=" + url);

            //读取根目录下配置文件，读取到则直接替换
            String appPath = System.getProperty("user.dir");
            System.out.println("apppath: " + appPath);
            File f = new File(appPath + "/" + STATIC_LOGGERCONFIG_PATH);
            if (f.exists()) {
                url = new URL("file://" + appPath + "/" + STATIC_LOGGERCONFIG_PATH);
            } else {
                f = new File(appPath + "/" + STATIC_LOGGER_PATH);
                if (f.exists()) {
                    url = new URL("file://" + appPath + "/" + STATIC_LOGGER_PATH);
                }
            }
            System.out.println("log4j2 path：url=" + url);

            if (url != null) {
                loadConfigFile(url.toURI());
                isInit = true;
            } else {
                System.out.println("配置文件没有读取到");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 加载配置文件
     *
     * @param uri
     */
    public static void loadConfigFile(URI uri) {
        System.out.println("03 log4j2 loadConfigFile uri=" + uri);
        // 加载配置文件
//        LoggerContext context = (LoggerContext) LogManager.getContext(LogLog4j.class.getClassLoader().getParent(),
//                false, uri);
//        context.reconfigure();
        reLoadConfigFile(uri);
        if (uri.getPath() != null && !uri.getPath().startsWith("jar\\:")) {
            new FileListener(uri).setLogChangeDo(f -> reLoadConfigFile(uri)).start();
        }

    }

    private static void reLoadConfigFile(URI filePath) {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        context.setConfigLocation(filePath);
        context.reconfigure();
    }

    /**
     * 获取日志操作实例
     *
     * @return
     */
    public static Log getRootLogger() {
        initConfig();
        return LogLog4j.getRootLogger();
    }

    /**
     * 获取日志操作实例
     *
     * @param filterPage 日志打印位置过滤名 , 过滤codeline打印信息
     * @return
     */
    public static Log getRootLogger(String filterPage) {
        initConfig();
        return LogLog4j.getRootLogger(filterPage);
    }

    /**
     * 获取日志操作实例
     *
     * @param clazz 通过类定义
     * @return
     */
    public static Log getLogger(Class clazz) {
        initConfig();
        return LogLog4j.getLogger(clazz);
    }

    /**
     * 获取日志操作实例
     *
     * @param clazz      通过类定义
     * @param filterPage 日志打印位置过滤名 , 过滤codeline打印信息
     * @return
     */
    public static Log getLogger(Class clazz, String filterPage) {
        initConfig();
        return LogLog4j.getLogger(clazz, filterPage);
    }

    /**
     * 获取日志操作实例
     *
     * @param name
     * @return
     */
    public static Log getLogger(String name) {
        initConfig();
        return LogLog4j.getLogger(name);
    }

    /**
     * @param name
     * @param filterPage
     * @return
     */
    public static Log getLogger(String name, String filterPage) {
        initConfig();
        return LogLog4j.getLogger(name, filterPage);
    }

}
