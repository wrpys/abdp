package com.newland.spring.request.model.result;

import com.newland.spring.request.model.header.BaseRequestHeaderVo;

public interface HttpResult<T> {

    HttpResult setResultContent(Object content);

    HttpResult setResultHeader(Object header);

    HttpResult setRequest(BaseRequestHeaderVo request);
}
