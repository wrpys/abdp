package com.newland.spring.platcore.code;

import org.springframework.context.annotation.Configuration;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface CommonCode {

    /*
     * @description 错误码前缀设置
     * @author Hux
     * @date 2019-09-30 8:30
     */
    CommonCodeScope scope() default CommonCodeScope.ENABLE;

    /*
     * @description 错误码前缀设置
     * @author Hux
     * @date 2019-09-30 8:30
    */
    String errorPre();

    /*
     * @description 错误码参数名，默认为code，该值用于反射检索参数名
     * @author Hux
     * @date 2019-09-30 8:30
    */
    String code() default "code";

    /*
     * @description 错误描述参数名，默认为msg，该值用于反射检索参数名
     * @author Hux
     * @date 2019-09-30 8:31
    */
    String msg() default "msg";
}
