package com.newland.spring.request.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.newland.spring.platcore.exceptions.CommonException;
import com.newland.spring.request.model.header.RequestHeaderVo;
import org.springframework.core.NamedInheritableThreadLocal;
import org.springframework.core.NamedThreadLocal;
import org.springframework.util.Base64Utils;
import org.springframework.util.StringUtils;

/**
 * 请求上下文
 *
 * @author WRP
 * @since 2019/11/4
 */
public class RequestContext {

    public static final String USER_INFO = "USER_INFO";

    /**
     * token信息
     */
    private static NamedThreadLocal<String> tokenThreadLocal = new NamedThreadLocal<>("TOKEN");

    /**
     * 用户信息
     */
    private static NamedThreadLocal<SessionInfo> userInfoThreadLocal = new NamedThreadLocal<>("USER_INFO");

    /**
     * 请求消息头
     */
    // private static NamedThreadLocal<RequestHeaderVo> headerThreadLocal = new NamedThreadLocal<>("REQUEST_HEADER");
    private static ThreadLocal<RequestHeaderVo> headerThreadLocal = new NamedInheritableThreadLocal<>("REQUEST_HEADER");

    /**
     * 请求路径
     */
    private static NamedThreadLocal<String> UrlThreadLocal = new NamedThreadLocal<>("REQUEST_URL");

    public static RequestHeaderVo getRequestHeader() {
        return headerThreadLocal.get();
    }

    public static void setRequestHeader(RequestHeaderVo header) {
        RequestContext.headerThreadLocal.set(header);
    }

    public static void setToken(String token) {
        if (StringUtils.isEmpty(token)) {
            tokenThreadLocal.set(null);
            userInfoThreadLocal.set(null);
            return;
        }
        tokenThreadLocal.set(token);
        String decodeToken = new String(Base64Utils.decodeFromString(token));
        SessionInfo sessionInfo = JSON.parseObject(decodeToken, new TypeReference<SessionInfo>() {
        });
        userInfoThreadLocal.set(sessionInfo);
    }

    public static String getToken() {
        return tokenThreadLocal.get();
    }

    public static String getUserId() {
        checkSession();
        return userInfoThreadLocal.get().getUserId();
    }

    public static String getDeptId() {
        checkSession();
        return userInfoThreadLocal.get().getDeptId();
    }

    public static SessionInfo getUserInfo() {
        checkSession();
        return userInfoThreadLocal.get();
    }

    private static void checkSession() {
        if (StringUtils.isEmpty(getToken()) || userInfoThreadLocal.get() == null) {
            throw new CommonException(TokenCode.TOKEN_NOT_FOUND);
        }
    }

    public static void setRequestUrl(String requestUri) {
        UrlThreadLocal.set(requestUri);
    }

    public static String getRequestUrl(){
        return UrlThreadLocal.get();
    }
}
