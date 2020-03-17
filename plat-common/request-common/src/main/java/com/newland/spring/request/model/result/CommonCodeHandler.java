package com.newland.spring.request.model.result;

import com.newland.spring.platcore.code.CommonCode;
import com.newland.spring.platcore.code.CommonCodeScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

/**
 * Created by garfield on 2018/5/17.
 */
public final class CommonCodeHandler {

    private final static Logger LOGGER = LoggerFactory.getLogger(CommonCodeHandler.class);

    public static String ERROR_CODE_PRE;

    private final static String scanPackage = "com.newland";

    public static String SYS_ERROR_CODE = "9999";

    /**
     * jar中的文件路径分隔符
     */
    private static final char SLASH_CHAR = '/';
    /**
     * 包名分隔符
     */
    private static final char DOT_CHAR = '.';
    /**
     * class标识符
     */
    private static final String CLASS_STR = ".class";

    public static void init() throws Exception {
        Enum appCode = null;
        try {
            List<Class<?>> ls = getClass(scanPackage, true);
            if (ls.size() == 0) {
                throw new Exception("类查询失败！");
            }
            List<Class<?>> lc = new ArrayList<Class<?>>();
            Set<String> lids = new HashSet<>();
            for (Class<?> clazz : ls) {
                ERROR_CODE_PRE = "";
                if (isAnnotations(clazz, CommonCode.class)) {
                    lc.add(clazz);
                    lids.add(ERROR_CODE_PRE);
                    if (lids.size() > 1) {
                        throw new Exception("结果代码初始化失败：服务内赋值多个应用前缀码[" + lids.toString() + "]，请核查服务代码：" + lc.toString());
                    }
                }
            }
            if (lc.size() == 0) {
                throw new Exception("结果代码初始化失败：应用内没有标注注解CommonCode的类，请核查应用代码！");
            } else {
                ERROR_CODE_PRE = lids.iterator().next();
                LOGGER.info("结果代码初始化成功！结果码实现类是类：{}，应用结果码前缀：{}", lc.get(0).toString(),ERROR_CODE_PRE);
            }

        } catch (Exception e) {
            LOGGER.error("结果代码初始化失败！", "9999", e);
            throw e;
        }
    }

    /*
     * @description 类是否有平台指定的注解声明
     * @author Hux
     * @date 2019-09-30 11:03
     * @param annotations	类包含的所有声明类
     * @param resultClass	平台错误码声明类
     * @return boolean 返回验证结果
     */
    private static boolean isAnnotations(Class clazz, Class<CommonCode> codeClass) throws Exception {
        Annotation[] annotations = clazz.getAnnotations();
        for (int i = annotations.length - 1; i >= 0; i--) {
            if (annotations[i].annotationType() == codeClass) {
                LOGGER.info("结果码实现类是类：" + clazz.toString());
                CommonCode code = (CommonCode) clazz.getAnnotation(CommonCode.class);
                if(code.scope() == CommonCodeScope.DISABLE){
                    return false;
                }
                checkCodeClass(clazz, code);
//                System.out.println("error code pre is " + ERROR_CODE_PRE);
                return true;
            }
        }
        return false;
    }

    private static void checkCodeClass(Class clazz, CommonCode code) throws Exception {
        String errorPre = code.errorPre();
        String codeParam = code.code();
        String msgParam = code.msg();
        if (errorPre.length() < 5) {
            throw new Exception("应用前缀码输入错误，位数少于5位：" + errorPre);
        }
        if (errorPre.length() > 5) {
            throw new Exception("应用前缀码输入错误，位数大于5位：" + errorPre);
        }
        checkCodeValue(clazz, codeParam);
        checkCodeValue(clazz, msgParam);
        ERROR_CODE_PRE = errorPre;
    }

    private static void checkCodeValue(Class clazz, String param) throws Exception {
        try {
            clazz.getDeclaredField(param);

        } catch (Exception e) {
            throw new Exception("结果码实现类格式错误，输入的结果码参数{ " + param + " }不在结果实现类参数集合{}" + Arrays.toString(clazz.getDeclaredFields()));
        }
    }


    /**
     * 判断一个类是否继承某个父类或实现某个接口
     */
    public static boolean isChildClass(Class clazz, Class parentClazz) {
        if (clazz == null)
            return false;

        try {
            if (Modifier.isAbstract(clazz.getModifiers())) {// 抽象类忽略
                return false;
            }
            if (Modifier.isInterface(clazz.getModifiers())) {// 接口忽略
                return false;
            }
            if (clazz == parentClazz) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return parentClazz.isAssignableFrom(clazz);

    }


    public static List<Class<?>> getClass(String packageName, boolean recursive) throws IOException {
        List<Class<?>> classList = new ArrayList<Class<?>>();
        // 获取当前线程的类装载器中相应包名对应的资源
        Enumeration<URL> iterator = Thread.currentThread().getContextClassLoader()
                .getResources(packageName.replace(DOT_CHAR, SLASH_CHAR));
        while (iterator.hasMoreElements()) {
            URL url = iterator.nextElement();
            String protocol = url.getProtocol();
            List<Class<?>> childClassList = Collections.emptyList();
            switch (protocol) {
                case "file":
                    childClassList = getClassInFile(url, packageName, recursive);
                    break;
                case "jar":
                    childClassList = getClassInJar(url, packageName, recursive);
                    break;
                default:
                    // 在某些WEB服务器中运行WAR包时，它不会像TOMCAT一样将WAR包解压为目录的，如JBOSS7，它是使用了一种叫VFS的协议
                    LOGGER.info("unknown protocol:" + protocol);
                    break;
            }
            classList.addAll(childClassList);
        }
        return classList;
    }


    /**
     * 在给定的文件或文件夹中寻找指定包下的所有类
     *
     * @param url         包的统一资源定位符
     * @param packageName 用'.'分隔的包名
     * @param recursive   是否递归搜索
     * @return 该包名下的所有类
     */
    public static List<Class<?>> getClassInFile(URL url, String packageName, boolean recursive) {
        try {
            Path path = Paths.get(url.toURI());
            return getClassInFile(path, packageName, recursive);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 在给定的文件或文件夹中寻找指定包下的所有类
     *
     * @param path        包的路径
     * @param packageName 用'.'分隔的包名
     * @param recursive   是否递归搜索
     * @return 该包名下的所有类
     */
    public static List<Class<?>> getClassInFile(Path path, String packageName, boolean recursive) {
        if (!Files.exists(path)) {
            return Collections.emptyList();
        }
        List<Class<?>> classList = new ArrayList<Class<?>>();
        if (Files.isDirectory(path)) {
            if (!recursive) {
                return Collections.emptyList();
            }
            try {
                // 获取目录下的所有文件
                Stream<Path> stream = Files.list(path);
                Iterator<Path> iterator = stream.iterator();
                while (iterator.hasNext()) {
                    classList.addAll(getClassInFile(iterator.next(), packageName, recursive));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // 由于传入的文件可能是相对路径, 这里要拿到文件的实际路径, 如果不存在则报IOException
                path = path.toRealPath();
                String pathStr = path.toString();
                if (!pathStr.contains(CLASS_STR)) {
                    return classList;
                }
//				System.out.println(pathStr);
                // 这里拿到的一般的"aa:\bb\...\cc.class"格式的文件名, 要去除末尾的类型后缀(.class)
                int lastDotIndex = pathStr.lastIndexOf(DOT_CHAR);
                // Class.forName只允许使用用'.'分隔的类名的形式
                String className = pathStr.replace(File.separatorChar, DOT_CHAR);
                // 获取包名的起始位置
                int beginIndex = className.indexOf(packageName);
                if (beginIndex == -1) {
                    return Collections.emptyList();
                }
                className = lastDotIndex == -1 ? className.substring(beginIndex)
                        : className.substring(beginIndex, lastDotIndex);
                classList.add(Class.forName(className));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return classList;
    }


    /**
     * 在给定的jar包中寻找指定包下的所有类
     *
     * @param url         jar包的统一资源定位符
     * @param packageName 用'.'分隔的包名
     * @param recursive   是否递归搜索
     * @return 该包名下的所有类
     */
    public static List<Class<?>> getClassInJar(URL url, String packageName, boolean recursive) {
        try {
            JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
            return getClassInJar(jar, packageName, recursive);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    /**
     * 在给定的jar包中寻找指定包下的所有类
     *
     * @param jar         jar对象
     * @param packageName 用'.'分隔的包名
     * @param recursive   是否递归搜索
     * @return 该包名下的所有类
     */
    public static List<Class<?>> getClassInJar(JarFile jar, String packageName, boolean recursive) {
        List<Class<?>> classList = new ArrayList<Class<?>>();
        // 该迭代器会递归得到该jar底下所有的目录和文件
        Enumeration<JarEntry> iterator = jar.entries();
        while (iterator.hasMoreElements()) {
            // 这里拿到的一般的"aa/bb/.../cc.class"格式的Entry或 "包路径"
            JarEntry jarEntry = iterator.nextElement();
            if (!jarEntry.isDirectory()) {
                String name = jarEntry.getName();
                // 对于拿到的文件,要去除末尾的.class
                int lastDotClassIndex = name.lastIndexOf(".class");
                if (lastDotClassIndex != -1) {
                    int lastSlashIndex = name.lastIndexOf(SLASH_CHAR);
                    name = name.replace(SLASH_CHAR, DOT_CHAR);
                    if (name.startsWith(packageName)) {
                        if (recursive || packageName.length() == lastSlashIndex) {
                            String className = name.substring(0, lastDotClassIndex);
//							System.out.println(className);
                            try {
                                classList.add(Class.forName(className));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return classList;
    }

    public static String SYS_LEVEL = "1";
    public static String BUSS_LEVEL = "2";

    public static String ERROR_CODE_TRACE_ID_NULL = "8000";
    public static String ERROR_DESC_TRACE_ID_NULL = "trace id is null";

    public static String ERROR_CODE_POST_REQUEST_LOG = ERROR_CODE_PRE + "8001";
    public static String ERROR_DESC_POST_REQUEST_LOG = "log before post error";

    public static String RESULT_CODE_POST_RESPONSE_LOG = ERROR_CODE_PRE + "8010";
    public static String RESULT_DESC_POST_RESPONSE_LOG = "log after post error";


    public static String ERROR_CODE_LOG_KAFKA = ERROR_CODE_PRE + "8020";
    public static String ERROR_DESC_LOG_KAFKA = "log in kafka error";

    public static String ERROR_CODE_THREAD_LOG_MISS = ERROR_CODE_PRE + "8030";
    public static String ERROR_DESC_THREAD_LOG_MISS = "thread log missing";

    public static String ERROR_CODE_LOG_SYSTEM = ERROR_CODE_PRE + "8034";
    public static String ERROR_DESC_LOG_SYSTEM = "system log error,thread log is missing or never be initiated";


    public static String ERROR_CODE_CONTROLLER_POST;
    public static String ERROR_DESC_CONTROLLER_POST;

    static void initContants() {
        ERROR_CODE_TRACE_ID_NULL = "8000";
        ERROR_DESC_TRACE_ID_NULL = "trace id is null";

        ERROR_CODE_POST_REQUEST_LOG = ERROR_CODE_PRE + BUSS_LEVEL + "8001";
        ERROR_DESC_POST_REQUEST_LOG = "log before post error";

        RESULT_CODE_POST_RESPONSE_LOG = ERROR_CODE_PRE + BUSS_LEVEL + "8010";
        RESULT_DESC_POST_RESPONSE_LOG = "log after post error";


        ERROR_CODE_LOG_KAFKA = ERROR_CODE_PRE + BUSS_LEVEL + "8020";
        ERROR_DESC_LOG_KAFKA = "log in kafka error";

        ERROR_CODE_THREAD_LOG_MISS = ERROR_CODE_PRE + BUSS_LEVEL + "8030";
        ERROR_DESC_THREAD_LOG_MISS = "thread log missing";

        ERROR_CODE_LOG_SYSTEM = ERROR_CODE_PRE + BUSS_LEVEL + "8034";
        ERROR_DESC_LOG_SYSTEM = "system log error,thread log is missing or never be initiated";

        SYS_ERROR_CODE = ERROR_CODE_PRE + BUSS_LEVEL + "9999";


        ERROR_CODE_CONTROLLER_POST = ERROR_CODE_PRE + "8038";
        ERROR_DESC_CONTROLLER_POST = "controller post error";


    }

}
