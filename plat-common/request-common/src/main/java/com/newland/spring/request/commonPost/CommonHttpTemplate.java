package com.newland.spring.request.commonPost;

import com.newland.spring.platcore.log.annotation.NlLog;
import com.newland.spring.request.interceptor.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 公共HTTP请求封装，兼容service_id和ip形式请求
 *
 * @author Hux
 * @date 2018年5月30日上午9:46:34
 */
@Service
public class CommonHttpTemplate {

    @Autowired
    @Qualifier("restTemplateIP")
    private RestTemplate restTemplateIP;

    @Autowired
    private RestTemplate restTemplateID;

    //获取原生请求request
    private static HttpServletRequest request;

    private static MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");

    /**
     * 公共HTTP-POST请求，服务请求先判断是否是IP地址，如果不是则使用servie_id请求
     *
     * @param url    请求的目的地址
     * @param params 请求报文body
     * @param clazz  请求返回类型
     *               n     * @return
     * @author Hux
     * @date 2018年5月30日上午9:50:38
     */
    @SuppressWarnings("unchecked")
    @NlLog(title = "HttpTemplate", action = "postForObject")
    public <T> T postForObject(String url, String params, Class<T> clazz) {
        HttpHeaders headers = buildHttpHeaders();
        headers.setContentType(type);
        HttpEntity<String> formEntity = new HttpEntity<>(params, headers);
        if (isIP(url)) {
            return (T) restTemplateIP.postForObject(url, formEntity, clazz);
        } else {
            return (T) restTemplateID.postForObject(url, formEntity, clazz);
        }
    }
    
    /**
     * 公共HTTP-POST请求，服务请求先判断是否是IP地址，如果不是则使用servie_id请求
     *
     * @param url    请求的目的地址
     * @param params 请求报文body
     * @param clazz  请求返回类型
     *               n     * @return
     * @author Hux
     * @date 2018年5月30日上午9:50:38
     */
    @SuppressWarnings("unchecked")
    @NlLog(title = "HttpTemplate", action = "postForObject")
    public <T> T postForObject(String url, String params, Class<T> clazz, boolean isIp) {
        HttpHeaders headers = buildHttpHeaders();
        headers.setContentType(type);
        HttpEntity<String> formEntity = new HttpEntity<>(params, headers);
        if (isIP(url) || isIp) {
            return (T) restTemplateIP.postForObject(url, formEntity, clazz);
        } else {
            return (T) restTemplateID.postForObject(url, formEntity, clazz);
        }
    }

    /**
     * 公共HTTP-GET请求，服务请求先判断是否是IP地址，如果不是则使用servie_id请求
     *
     * @param url   请求的目的地址
     * @param clazz 请求返回类型
     * @return
     * @author Hux
     * @date 2018年5月30日上午10:06:32
     */
    @SuppressWarnings("unchecked")
    public <T> T getForObject(String url, Class<T> clazz) {
        HttpHeaders headers = buildHttpHeaders();
        headers.setContentType(type);
        if (isIP(url)) {
            return (T) restTemplateIP.getForObject(url, String.class);
        } else {
            return (T) restTemplateID.getForObject(url, String.class);
        }
    }

    /**
     * 公共文件上传请求，服务请求先判断是否是IP地址，如果不是则使用servie_id请求
     *
     * @param url      请求的目的地址
     * @param file     文件
     * @param subSysId 子系统ID
     * @param part     子目录
     * @param clazz    请求返回类型
     *                 n     * @return
     * @author Hux
     * @date 2018年5月30日上午9:50:38
     */
    @SuppressWarnings("unchecked")
    @NlLog(title = "HttpTemplate", action = "fileUpload")
    public <T> T fileUpload(String url, File file, String subSysId, String part, Class<T> clazz) {
        HttpHeaders headers = buildHttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("subSysId", subSysId);
        bodyMap.add("part", part);
        bodyMap.add("file", new FileSystemResource(file));

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        if (isIP(url)) {
            return (T) restTemplateIP.postForObject(url, requestEntity, clazz);
        } else {
            return (T) restTemplateID.postForObject(url, requestEntity, clazz);
        }
    }
    /*重载：用于调用搜索接口*/
    @NlLog(title = "HttpTemplate", action = "fileUpload")
    public <T> T fileUpload(String url, File file, String fileId, Class<T> clazz) {
        HttpHeaders headers = buildHttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("fileId", fileId);
        bodyMap.add("file", new FileSystemResource(file));
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
        if (isIP(url)) {
            return (T) restTemplateIP.postForObject(url, requestEntity, clazz);
        } else {
            return (T) restTemplateID.postForObject(url, requestEntity, clazz);
        }
    }


    @SuppressWarnings("unchecked")
    /**
     * @description 通过远程下载文件
     * @author Hux
     * @date 2019-11-12 20:44
     * @param url
     * @param fileName
     * @param subSysId
     * @return void
     */
    public void fileDownload(String url, String fileName, String subSysId) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Resource> httpEntity = new HttpEntity<Resource>(headers);
        ResponseEntity<byte[]> response = null;
        if (isIP(url)) {
            response = restTemplateIP.exchange(url, HttpMethod.GET,
                    httpEntity, byte[].class);
        } else {
            response = restTemplateID.exchange(url, HttpMethod.GET,
                    httpEntity, byte[].class);
        }
        if (fileName.substring(fileName.lastIndexOf("/")).indexOf(".") < 0) {
            String headerStr = response.getHeaders().toString();
            Pattern p = Pattern.compile("filename=.*(\\.[^\"]*)\"");
            Matcher m = p.matcher(headerStr);
            if(m.find()){
                fileName = fileName + m.group(1);
            }
        }
         FileOutputStream fos = new FileOutputStream(new File(fileName ));
        if (response.getBody() != null) {
            fos.write(response.getBody());
        }
        fos.flush();
        fos.close();
    }

    /**
     * 判断是否是IP地址
     *
     * @param addr
     * @return
     * @author Hux
     * @date 2018年5月30日上午10:02:10
     */
    public boolean isIP(String addr) {
//		if (addr.length() < 7 || addr.length() > 15 || "".equals(addr)) {
//			return false;
//		}
        //判断IP格式和范围
        String rexp = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pat = Pattern.compile(rexp);
        Matcher mat = pat.matcher(addr);
        return mat.find();
    }


    /**
     * 获取服务URL
     *
     * @return
     * @author Hux
     * @date 2018年6月11日下午3:09:43
     */
    public static String getReferUrl() {
        // TODO Auto-generated method stub
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            ServletRequestAttributes sattributes = (ServletRequestAttributes) attributes;
            if (sattributes != null) {
                request = sattributes.getRequest();
                if (request != null) {
                    return request.getRequestURI();
                }
            }
        }
        return "";
    }

    /**
     * 构造header
     *
     * @return
     */
    private HttpHeaders buildHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("referurl", CommonHttpTemplate.getReferUrl());
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        headers.add(RequestContext.USER_INFO, RequestContext.getToken());
        return headers;
    }

}
