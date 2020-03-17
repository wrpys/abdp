package com.newland.spring.request.model.result;

import com.newland.spring.platcore.code.CommonCode;
import com.newland.spring.platcore.exceptions.CommonException;
import com.newland.spring.request.model.MsgRespCustom;
import com.newland.spring.request.model.header.BaseResponseHeaderVo;

import java.lang.reflect.Field;

/*
 * @description 结果码整合类
 * @author Hux
 * @date 2019-09-30 10:15
 */
public class ResultCode {


    /*
     * @description 新建返回结果实体
     * @author Hux
     * @date 2019-09-30 10:16
     */
    public static MsgRespCustom newResult() {
        return new MsgRespCustom();
    }

    /*
     * @description 新建返回结果实体
     * @author Hux
     * @date 2019-09-30 10:16
     */
    public static MsgRespCustom newResult(Enum e) {
        BaseResponseHeaderVo header = invokeGetHeader(e);
        MsgRespCustom response = new MsgRespCustom();
        response.setHeader(header);
        return response;
    }
    /*
     * @description 新建返回结果实体-传入Exception
     * @author Hux
     * @date 2019-10-14 9:36
     * @param e
     * @return com.newland.spring.request.model.MsgRespCustom
    */
    public static MsgRespCustom newResult(Exception e) {
        BaseResponseHeaderVo headerVo = new BaseResponseHeaderVo();
        if (e instanceof CommonException) {
            headerVo.setRespCode(((CommonException) e).getCode());
            headerVo.setRespDesc(((CommonException) e).getMessage());
        } else {
            headerVo.setRespCode(CommonCodeHandler.ERROR_CODE_PRE + "29997");
            headerVo.setRespDesc(e.getMessage());
        }
        MsgRespCustom response = new MsgRespCustom();
        response.setHeader(headerVo);
        return response;
    }

    /*
     * @description 根据应用传入的结果枚举类，反射获取结果码和结果描述返回标准头
     * @author Hux
     * @date 2019-09-30 10:16
     * @param e 应用自定义的枚举实体，必须有CommonCode注解
     */
    private static BaseResponseHeaderVo invokeGetHeader(Enum e) {
        BaseResponseHeaderVo headerVo = new BaseResponseHeaderVo();
        Class clazz = e.getClass();
        try {
            CommonCode ano_result = (CommonCode) clazz.getAnnotation(CommonCode.class);
            Field f_code = clazz.getDeclaredField(ano_result.code());
            f_code.setAccessible(true);
            String code = (String) f_code.get(e);
            if (!code.equals("0000") && !code.equals("0")) {
                code = ano_result.errorPre() + code;
            }
            headerVo.setRespCode(code);
            Field f_msg = clazz.getDeclaredField(ano_result.msg());
            f_msg.setAccessible(true);
            headerVo.setRespDesc((String) f_msg.get(e));
        } catch (Exception e1) {
            e1.printStackTrace();
            throw new CommonException(CommonCodeHandler.ERROR_CODE_PRE + "29998", "系统解析错误码值错误！" + e1.getMessage());
        }
        return headerVo;
    }


    public static void main(String[] args) throws Exception {
//        BaseResponseHeaderVo h = invokeGetHeader(DaEnum.error);
////        System.out.println(invokeGetHeader(DaEnum.kk));
//        System.out.println(h.toString());
    }

}
