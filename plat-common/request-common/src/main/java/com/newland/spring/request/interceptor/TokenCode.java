package com.newland.spring.request.interceptor;


import com.newland.spring.platcore.code.CommonCode;
import com.newland.spring.platcore.code.CommonCodeScope;

/**
 * token错误码
 *
 * @author WRP
 * @since 2019/11/18
 */
//在编译和系统启动的时候这个要关闭
@CommonCode(scope = CommonCodeScope.DISABLE, errorPre = "21105")
public enum TokenCode {
    OK("0000", "成功了！"),
    TOKEN_NOT_FOUND("23001", "token不存在！");

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    TokenCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
