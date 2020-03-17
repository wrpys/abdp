package com.newland.spring.request.model;

import com.newland.spring.request.model.header.BaseRequestHeaderVo;
import com.newland.spring.request.model.header.BaseResponseHeaderVo;
import com.newland.spring.request.model.header.RequestHeaderVo;
import com.newland.spring.request.model.result.HttpResult;
import org.joda.time.LocalDateTime;

public class MsgRespCustom<TH extends BaseResponseHeaderVo, TC> extends MsgCustom<TH, TC> implements HttpResult {

    @Override
    public HttpResult setRequest(BaseRequestHeaderVo request) {
        BaseResponseHeaderVo headerVo = this.getHeader();
        headerVo.setOrderId(request.getOrderId() + 1);
        headerVo.setRequestSeq(request.getRequestSeq());
        headerVo.setResponseTime(LocalDateTime.now().toString("yyyyMMddHHmmss"));
        return this;
    }

    @Override
    public HttpResult setResultContent(Object content) {
        this.setContent((TC) content);
        return this;
    }

    @Override
    public HttpResult setResultHeader(Object header) {
        this.setHeader((TH) header);
        return this;
    }

}
