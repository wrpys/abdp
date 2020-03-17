package com.newland.spring.request.commonPost;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.util.concurrent.TimeUnit;


@Configuration
public class RestTemplateConfig {

    private final static Logger LOGGER = LoggerFactory.getLogger(RestTemplateConfig.class);

    /**
     * 长连接保持时长
     */
    @Value("${resttemplate.config.duration:30}")
    private Integer duration;

    /**
     * 最大连接数
     */
    @Value("${resttemplate.config.maxTotal:30}")
    private Integer maxTotal;

    /**
     * 单路由最大并发数
     */
    @Value("${resttemplate.config.maxPerRoute:15}")
    private Integer maxPerRoute;

    /**
     * 连接重试次数
     */
    @Value("${resttemplate.config.retryTimes:2}")
    private Integer retryTimes;

    /**
     * 读取超时
     */
    @Value("${resttemplate.config.readTimeOut:5000}")
    private Integer readTimeOut;

    /**
     * 连接超时
     */
    @Value("${resttemplate.config.connectTimeOut:15000}")
    private Integer connectTimeOut;


    /**
     * 自定义RestTemplate配置
     * 1、设置最大连接数
     * 2、设置路由并发数
     * 3、设置重试次数
     *
     * @return
     * @author Hux
     */
    @Bean("dcClientHttpRequestFactory")
    public ClientHttpRequestFactory newClientHttpRequestFactory() {
        LOGGER.info("初始化RestTemplate连接配置，duration={}，maxTotal={},maxPerRoute={},retryTimes={},readTimeOut={},connectTimeOut={}", duration, maxTotal, maxPerRoute, retryTimes, readTimeOut, connectTimeOut);
        // 长连接保持时长30秒
        PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(
                duration, TimeUnit.SECONDS);
        // 最大连接数
        pollingConnectionManager.setMaxTotal(maxTotal);
        // 单路由的并发数
        pollingConnectionManager.setDefaultMaxPerRoute(maxPerRoute);
        pollingConnectionManager.setValidateAfterInactivity(2000);
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(pollingConnectionManager);
        // 重试次数2次，并开启
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(retryTimes,
                true));
        // 保持长连接配置，需要在头添加Keep-Alive
        httpClientBuilder
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());
        HttpClient httpClient = httpClientBuilder.build();
        // httpClient连接底层配置clientHttpRequestFactory
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(httpClient);
        clientHttpRequestFactory.setReadTimeout(readTimeOut);// ms
        clientHttpRequestFactory.setConnectTimeout(connectTimeOut);//
        return clientHttpRequestFactory;
    }

}