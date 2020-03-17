package com.newland.spring.request.log.model;

import com.alibaba.druid.support.http.WebStatFilter;
import com.newland.spring.request.log.aspect.MethodArgumentResolver;
import com.newland.spring.request.model.MsgReq;
import com.newland.spring.request.model.header.RequestHeaderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanMap;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


public class AllLogBase extends LogBase implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient Logger logger = LoggerFactory.getLogger(this.getClass());


    protected String appId;
    protected String userToken;
    protected String resourceId;
    protected String openId;
    protected String serviceName;
    protected String traceId;
    protected String spanId;
    protected String parentId;

    protected String errorCode;

    private int orderId;

    public CommonLog getCommonLog() {
        return commonLog;
    }

    public void setCommonLog(CommonLog commonLog) {
        this.commonLog = commonLog;
    }

    private CommonLog commonLog;


    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public AllLogBase(int type, String level) {
        super(type, level);
        writeTraceInfo();
        writeBusinessInfo();
    }

    public AllLogBase() {

    }

    /**
     * 如果Response为空则获取Response
     * 如果获取到的Response为空，则不更新
     * 如果不为空，则更新
     * 2018/09/27 更改api
     */
    public void writeTraceInfo() {
//		TraceInfo traceInfo = TraceInfo.create();
//		this.spanId = traceInfo.getSpanId();
//		this.traceId = traceInfo.getTraceId();
//		this.parentId = traceInfo.getParentId();
//		this.orderId = ResponseThreadLocal.getOrderId();
    }

    public void writeBusinessInfo() {
//		this.openId = ResponseThreadLocal.getOpenId();
//		this.resourceId = ResponseThreadLocal.getResourceId();
//		this.userId = ResponseThreadLocal.getUserId();
//		this.appId = ResponseThreadLocal.getAppId();
//		this.userToken = ResponseThreadLocal.getUserToken();
    }

//	@Override
//	public String toString() {
//		return appId +
//				"," + id +
//				"," + userToken +
//				"," + type +
//				"," + threadName +
//				"," + resourceId +
//				"," + aspectLogId +
//				"," + openId +
//				"," + serviceName +
//				"," + requestUrl +
//				"," + traceId +
//				"," + remoteAddr +
//				"," + spanId +
//				"," + requestArgs +
//				"," + parentId +
//				"," + requestMethod +
//				"," + level +
//				"," + message +
//				"," + exception +
//				"," + simpleDateFormat.format(createTime) +
//				"," + userId;
//	}


    @Override
    public String toString() {
        return "AllLogBase{" +
                "appId='" + appId + '\'' +
                ", userToken='" + userToken + '\'' +
                ", resourceId='" + resourceId + '\'' +
                ", openId='" + openId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", traceId='" + traceId + '\'' +
                ", spanId='" + spanId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", orderId=" + orderId +
                "} " + super.toString();
    }

//	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    public void parseParams(MsgReq obj) {

        try {
//			Map<String, Map<String, String>> map = new HashMap<>();
            //性能低下，自己写
//			String str = gson.toJson(obj);
//			map = gson.fromJson(str, map.getClass());
//			Map<String, String> header = map.get("header");
            Map<String, String> header = new HashMap<>();

            header = getHeaderMap(header, (RequestHeaderVo) obj.getHeader());
            if (header != null) {
                this.userId = header.get("userId");
                this.openId = header.get("openId");
                this.resourceId = header.get("resourceId");
                this.userToken = header.get("userToken");
                this.appId = header.get("appId");
                logger.info("userId:" + userId +
                        ",openId:" + openId +
                        ",resourceId:" + resourceId +
                        ",userToken:" + userToken +
                        ",appId:" + appId);
            }


        } catch (Exception e) {

            logger.error("parse params error:", e);
            return;
        }

    }

    /**
     * 获取请求头
     * 1、通过ObjectMapper先将bean转换为json，再将json转换为map，但是这种方法比较绕，且效率很低，经测试，循环转换10000个bean，就需要12秒！！！不推荐使用
     * <p>
     * 2、通过java反射，获取bean类的属性和值，再转换到map对应的键值对中，这种方法次之，但稍微有点麻烦
     * <p>
     * 3、通过net.sf.cglib.beans.BeanMap类中的方法，这种方式效率极高，它跟第二种方式的区别就是因为使用了缓存，初次创建bean时需要初始化，之后就使用缓存，所以速度极快，经测试，循环bean和map的转换10000次，仅需要300毫秒左右。
     *
     * @return
     */
    private Map<String, String> getHeaderMap(Map<String, String> map, RequestHeaderVo headerVo) {

        if (headerVo != null) {
            BeanMap beanMap = BeanMap.create(headerVo);
            for (Object key : beanMap.keySet()) {
                if (beanMap.get(key) != null) {
                    map.put(key + "", (String) beanMap.get(key));
                }
            }
        }
        return map;
    }


    private HttpServletResponse getResponse() {
        HttpServletResponse response = null;
        try {
            response = MethodArgumentResolver.getHttpServletResponse();
            if (response instanceof WebStatFilter.StatHttpServletResponseWrapper) {
                HttpServletResponseWrapper httpServletResponseWrapper = (HttpServletResponseWrapper) response;

                Method _getHttpServletResponse = HttpServletResponseWrapper.class.
                        getDeclaredMethod("_getHttpServletResponse", null);
                _getHttpServletResponse.setAccessible(true);
                response = (HttpServletResponse) _getHttpServletResponse.invoke(httpServletResponseWrapper, null);
            }

            logger.info("response is " + String.valueOf(response == null ? "response is null" : response.getClass()));
        } catch (NoSuchMethodException e) {
            logger.error("getResponse" + e.getMessage());
        } catch (InvocationTargetException e) {
            logger.error("getResponse" + e.getMessage());
        } catch (IllegalAccessException e) {
            logger.error("getResponse" + e.getMessage());
        }
        return response;
    }

}
