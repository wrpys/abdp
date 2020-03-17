package com.newland.spring.platcore.exceptions;

import com.newland.spring.platcore.code.CommonCode;

import java.lang.reflect.Field;

/**
 * @author: create garfield
 * @version: v1.0
 * @description: com.newland.dc.exception
 * @date:2019/6/24/**
 * @Auther: garfield
 * @Date: 2019/6/24 14:40
 * @Description:
 */
public class CommonException extends RuntimeException {


    private String code;

    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public CommonException(String code, String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.code = code;
    }

    public CommonException(String code, String message) {
        super(message);
        this.message = message;
        this.code = code;
    }

    public CommonException(Enum e) {
        this(e, "");
    }


    public CommonException(Enum e, Throwable ex) {
        this(e, "", ex);
    }

    public CommonException(Enum e, String msg, Throwable ex) {
        super(msg, ex);
        Class clazz = e.getClass();
        try {
            CommonCode ano_result = (CommonCode) clazz.getAnnotation(CommonCode.class);
            Field f_code = clazz.getDeclaredField(ano_result.code());
            f_code.setAccessible(true);
            String code = (String) f_code.get(e);
            if (!code.equals("0000") && !code.equals("0")) {
                code = ano_result.errorPre() + code;
            }
            this.code = code;
            Field f_msg = clazz.getDeclaredField(ano_result.msg());
            f_msg.setAccessible(true);
            this.message = (String) f_msg.get(e) + msg;
        } catch (Exception e1) {
            throw new CommonException("2099929999", "系统解析错误码值错误！" + e1.getMessage());
        }
    }

    public CommonException(Enum e, String msg) {
        this(e, msg, null);
    }

}
