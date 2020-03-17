package com.newland.spring.request.model;

import com.newland.spring.request.model.header.ResponseHeaderVo;

public class MsgResp<T> extends MsgCustom<ResponseHeaderVo,T> {
	@Override
	public String toString() {
		return "MsgResp [content=" +  getContent() + "]";
	}

}
