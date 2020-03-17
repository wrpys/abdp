package com.newland.spring.platcore.code;


/**
 * description 类描述
 *
 * @author Hux
 * @date 2019/9/29 19:20
 */
//在编译和系统启动的时候这个要关闭
@CommonCode(scope = CommonCodeScope.DISABLE, errorPre = "20000", code = "code1", msg = "msg1")
public enum DefaultCode {
    kk("0000", "成功了！"),
    error("0001", "错误了"),
    ;

    private String code1;

    private String msg1;

    DefaultCode(String code, String msg) {
        this.code1 = code;
        this.msg1 = msg;
    }

}
