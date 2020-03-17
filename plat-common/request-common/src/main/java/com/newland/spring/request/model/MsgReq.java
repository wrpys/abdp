package com.newland.spring.request.model;

import com.newland.spring.request.model.header.RequestHeaderVo;

public class MsgReq<T> extends MsgCustom<RequestHeaderVo, T> {
    @Override
    public String toString() {
        return "MsgReq [content=" +  getContent() + "]";
    }
}
