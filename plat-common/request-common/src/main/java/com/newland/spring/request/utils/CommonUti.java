package com.newland.spring.request.utils;

/**
 * description 类描述
 *
 * @author Hux
 * @date 2019/11/24 10:46
 */
public class CommonUti {

    /**
     * @description 通用抛出异常，用户lambda表达式抛出
     * @author Hux
     * @date 2019-11-24 10:46
     * @param e
     * @return void
    */
    public static <E extends Exception> void doThrow(Exception e) throws E {
        throw (E)e;
    }


    //首字母转大写
    public static String toUpCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }
}
