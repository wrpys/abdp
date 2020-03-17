package com.newland.spring.request.model.header;

import java.io.Serializable;

/**
 * 用户请求报文头
 *
 * @author Li Siqi
 * @since 1.0.0-SNAPSHOT
 */
public class RequestHeaderVo extends BaseRequestHeaderVo implements Serializable {

    private String dev; //是否是开发环境,0开发环境，1测试环境，2生产环境

    private String userToken;   // 用户令牌，每次登录的时候生成，登出时删除

    private String orgToken; // 机构令牌

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getOrgToken() {
        return orgToken;
    }

    public void setOrgToken(String orgToken) {
        this.orgToken = orgToken;
    }


    public String getDev() {
        return dev;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }
}
