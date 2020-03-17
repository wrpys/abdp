package com.newland.spring.platcore.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import org.springframework.cglib.beans.BeanMap;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: create garfield
 * @version: v1.0
 * @description: com.newland.dc.resolver
 * @date:2019/5/15/**
 * @Auther: garfield
 * @Date: 2019/5/15 14:47
 * @Description:
 */
public class JsonUtils {

    public static final String JSON_NODE_APP_ID = "appId";
    public static final String JSON_NODE_HEADER = "header";
    public static final String JSON_NODE_HEADER_VERSION = "appVersion";
    public static final String JSON_NODE_HEADER_SDK_VERSION = "sdkVersion";
    public static final String JSON_NODE_HEADER_ORG_TOKEN = "orgToken";
    public static final String JSON_NODE_REQUEST_SEQ = "requestSeq";
    public static final String JSON_NODE_ORDER_ID = "orderId";

    public static Gson gson = new Gson();


    /**
     * @param: [body, key]
     * @return: java.lang.String
     * @auther: garfield
     * @date: 2019/5/15 下午3:07
     *
     */
    public static String getHeaderValue(String body, String key){
        JSONObject jsonObject = JSON.parseObject(body);
        JSONObject jsonObject1 = jsonObject.getJSONObject(JSON_NODE_HEADER);
        return  "" + Optional.ofNullable(jsonObject1).map(v -> v.get(key)).orElse(null);
    }

    public static <T> T  getNodeObject(String body, String key,Class<T> clazz){

        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONObject jsonObject1 = jsonObject.getJSONObject(key);
        if(jsonObject1 == null)
            return null;
        String o = jsonObject1.toString();
        return getObject(o,clazz);

    }

    public static <T> T  getNodeObject2(String body, String key,Class<T> clazz){

//        if(body.contains("\\")){
//            body = gson.toJson(body);
//        }
        JSONObject jsonObject = JSONObject.parseObject(body);
        JSONObject jsonObject1 = jsonObject.getJSONObject(key);
        if(jsonObject1 == null)
            return null;
        String o = jsonObject1.toString();
        return getObject(o,clazz);

    }

    /**
     * JSONObject目前这个方法会导致解析出来的字符串多出斜杠，当心。
     * 后续需要提供修改版
     * 需要遍历节点，废弃该方法
     * @param body
     * @param key
     * @param value
     * @return
     */
    public static String putNodeObject(String body, String key, String value){
//        Map<String,String> map = gson.fromJson(body,Map.class);
//        map.put(key,value);
//        body = gson.toJson(map);
//        return body;
        JSONObject jsonObject = JSON.parseObject(body);
        jsonObject.put(key,value);
        return jsonObject.toJSONString();
    }

    public static String modifyHeaderNodeValue(String body, String key, String value){
        JSONObject jsonObject = JSON.parseObject(body);
        JSONObject jsonObject1 = (JSONObject) jsonObject.get(JSON_NODE_HEADER);
        jsonObject1.put(key,value);
        jsonObject.put(JSON_NODE_HEADER,jsonObject1);
        return jsonObject.toJSONString();
    }


    public static String getString(Object body){
        return gson.toJson(body);

    }

    public static String getString2(Object body){
        return gson.toJson(body);

    }

    public static String getStringFromJson(String body){
        return gson.fromJson(body,String.class);

    }

    public static <T> T getObject(String body,Class<T> clazz){
        return gson.fromJson(body,clazz);

    }

    public static <T> T getObject(String body,Type clazz){
        return gson.fromJson(body,clazz);

    }

    /**
     * 将对象装换为map
     * 一层
     *
     * @param bean
     * @return
     */
    public static <T> Map<String, Object> beanToMap(T bean) {
        Map<String, Object> map = Maps.newHashMap();
        if (bean != null) {
            BeanMap beanMap = BeanMap.create(bean);
            for (Object key : beanMap.keySet()) {
                map.put(key + "", beanMap.get(key));
            }
        }
        return map;
    }

    /**
     * 将map装换为javabean对象
     *     * 一层
     * @param map
     * @param bean
     * @return
     */
    public static <T> T mapToBean(Map<String, Object> map, T bean) {
        BeanMap beanMap = BeanMap.create(bean);
        beanMap.putAll(map);
        return bean;
    }

    /**
     * 将List<T>转换为List<Map<String, Object>>
     *
     * @param objList
     * @return
     */
    public static <T> List<Map<String, Object>> objectsToMaps(List<T> objList) {
        List<Map<String, Object>> list = Lists.newArrayList();
        if (objList != null && objList.size() > 0) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0, size = objList.size(); i < size; i++) {
                bean = objList.get(i);
                map = beanToMap(bean);
                list.add(map);
            }
        }
        return list;
    }

    /**
     * 将List<Map<String,Object>>转换为List<T>
     *
     * @param maps
     * @param clazz
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static <T> List<T> mapsToObjects(List<Map<String, Object>> maps, Class<T> clazz)
            throws InstantiationException, IllegalAccessException {
        List<T> list = Lists.newArrayList();
        if (maps != null && maps.size() > 0) {
            Map<String, Object> map = null;
            T bean = null;
            for (int i = 0, size = maps.size(); i < size; i++) {
                map = maps.get(i);
                bean = clazz.newInstance();
                mapToBean(map, bean);
                list.add(bean);
            }
        }
        return list;
    }

//    public static void main(String[] args) {
////        String a = getHeaderValue("{\"header\":{\"requestTime\":\"2018100910320000\",\"orderId\":2,\"requestSeq\":\"e379853dd93e4249b9be8a272b\",\"appId\":\"bc1f4a2a995b47db9018031801984857\"},\"content\":{}}"
////        ,"appId");
////        Object o = getNodeObject("{\"header\":{\"requestTime\":\"2018100910320000\",\"orderId\":2,\"requestSeq\":\"e379853dd93e4249b9be8a272b\",\"appId\":\"bc1f4a2a995b47db9018031801984857\"},\"content\":{}}"
////                ,"header");
//
////        String body = "{\"header\": {\"requestTime\": \"2018100910320000\",\"requestSeq\": \"e379853dd93e4249b9be8a272b\",\"appId\": \"bc1f4a2a995b47db9018031801984857\",\"orderId\":\"0\"},\"content\": {}}";
//
//
//        String s = "{\"authMode\":\"G010\",\"duration\":\"180\",\"ctid\":\"MIICuQYKKoEcz1UGAQQCA6CCAqkwggKlAgEAMYHQMIHNAgEAMD0wMTELMAkGA1UEBhMCQ04xETAPBgNVBAoTCEdMQ1RJRDAxMQ8wDQYDVQQDEwZHTENBMDECCH96wSriUCnPMA0GCSqBHM9VAYItAwUABHoweAIgdRv582SQke3xOLrM0tPELui5DC4oeE1AlNHKh1nRL9cCICOKuP1A8NeTAL42VLQ+Ipn0Evq7i7XmnhOpYtVdY2o9BCDGR/tsjwn/KFZxT2AetfwiWPSBZDQ6SxTzDZ0zzGDq3gQQHNBx3ITnUVir05rsoGqEXzCCAcsGCiqBHM9VBgEEAgEwCQYHKoEcz1UBaICCAbDLxnZsPah+ssTYlI/Easzbs4dtXeAF9/8kTXIkb+c4lTfTBYt1ma+xy45zFnBGBDUw9Gwo3oyp81ko3Sk1r3/bN/CLp+iFRpHpzWWilf1jWw1pHAhBoUWwbNLVn6pCIiS6dcLznaYPfj29WdjCl1Kaorn9lUfZ5d6cokHojtMkefOZ886eyUQUs4vZ5xGB8wNXfh3WmYDBrgJaNrlZTWTwbhDWJ2wLYX1Z8sNq2108ZljtSZgZGn+LflgL3gDGGhQwB/5tt6rwB/KcRUsIUpN0abNrMxkeTgkXbrXmYT4bvuQmdnKMVQvKIZLlHfTiyctDi952q4k155Oeuvbpj2m62Ew/GdVU8d+QpK526f1inqv8tXhRThB9fDsHWN60ygZGUGR+SJCFsMwl9VeRKQolrpXtHel6mPwynUYGlWvHm0OYL3zR/ByIRhoyVAcMiA2jjDrUwxXW2u9L9W8cs6k/QDxIlVDSZHboAo1o8u5NhwAGi5+DSXP5f+GVmWhFi2TnnDQpi7tB3uLzwpAJDoiIrBCJV+lchGmPGUdsEzED5P758tGzOma1b8vfSFQO+AM=\",\"requestId\":\"3303bd1160014628ae781fc7302deaca\",\"appId\":\"8138adc1c0d7tme6a4007d024e8f3crm\",\"bizData\":\"13788885680\",\"bizSerialNum\":\"48C826FA120E0513D7\",\"availableTimes\":\"\"}";
//        String b = getString2(s);
//    }
}
