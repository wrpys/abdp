package com.newland.spring.platcore.log.annotation;

import java.lang.annotation.*;

/**
 * @Auther: garfield
 * @Date: 2019/5/13 下午2:09
 * @Description: 
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NlLog {
    
    /** 模块 */
    /**
     * @param: []
     * @return: java.lang.String
     * @auther: garfield
     * @date: 2019/5/13 下午2:11
     */
    String title() default "";
 
    /** 功能 */
    /**
     * @param: []
     * @return: java.lang.String
     * @auther: garfield
     * @date: 2019/5/13 下午2:11
     */
    String action() default "";
    
}

