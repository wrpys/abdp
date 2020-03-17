package com.newland.spring.request.model.header;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户响应报文头
 *
 * @author Li Siqi
 * @since 1.0.0-SNAPSHOT
 */
public class ResponseHeaderVo extends BaseResponseHeaderVo implements Serializable {


    public ResponseHeaderVo(RequestHeaderVo requestHeaderVo) {
        if (requestHeaderVo != null) {
            this.setOrderId(requestHeaderVo.getOrderId() + 1);
            this.setRequestSeq(requestHeaderVo.getRequestSeq());
        }
        this.setResponseTime(new Date().toString());
    }

    public ResponseHeaderVo(String code, String msg) {
        this.setRespCode(code);
        this.setRespDesc(msg);
    }

    public ResponseHeaderVo() {
    }

}
