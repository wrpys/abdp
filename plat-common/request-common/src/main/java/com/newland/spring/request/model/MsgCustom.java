package com.newland.spring.request.model;

import javax.validation.Valid;

public class MsgCustom<TH, TC>{

    private TH header;

    @Valid
    private TC content;

    public TC getContent() {
        return content;
    }

    public void setContent(TC content) {
        this.content = content;
    }

    public TH getHeader() {
        return header;
    }

    public void setHeader(TH header) {
        this.header = header;
    }
}
