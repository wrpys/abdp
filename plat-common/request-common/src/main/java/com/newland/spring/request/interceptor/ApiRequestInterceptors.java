package com.newland.spring.request.interceptor;

import com.newland.spring.platcore.log.Log;
import com.newland.spring.platcore.log.LogFactory;
import com.newland.spring.platcore.log.LogProperty;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 拦截器
 *
 * @author wrp
 */
@Component
public class ApiRequestInterceptors extends HandlerInterceptorAdapter {

    private static final Log LOGGER = LogFactory.getLogger(ApiRequestInterceptors.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String requestUri = request.getRequestURI();
        LOGGER.info(LogProperty.LOGCONFIG_DEALID, "请求路径:" + requestUri);
        // 保存请求路径
        RequestContext.setRequestUrl(requestUri);
        // 清除用户信息
        RequestContext.setToken(null);
        String token = request.getHeader(RequestContext.USER_INFO);
        LOGGER.info(LogProperty.LOGCONFIG_DEALID, "token:" + token);
        if (!StringUtils.isEmpty(token)) {
            RequestContext.setToken(token);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {
    }

}
