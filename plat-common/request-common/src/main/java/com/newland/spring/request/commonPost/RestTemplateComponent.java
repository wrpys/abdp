package com.newland.spring.request.commonPost;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

//import okhttp3.Cache;
//import okhttp3.Dispatcher;
//import okhttp3.OkHttpClient;

/**
 * RestTemplate注入设置
 * Created by Mr.J  on 2018/1/19.
 */
@Component
public class RestTemplateComponent {
	
	@Autowired
	@Qualifier("dcClientHttpRequestFactory")
	private ClientHttpRequestFactory clientHttpRequestFactory;

	/**
	 * RestTemplate主要注入bean，使用ribbon负载
	 * 1、使用serviceID请求的时候使用该bean
	 * 2、注入名为restTemplate，使用默认方式取出
	 * @author Hux
	 * @return
	 */
	@Bean
	@Primary
    @LoadBalanced
    public RestTemplate getRestTemplateID() {
        // TODO set up your restTemplate
        return getRestTemplate();
    }
	
	/**
	 * RestTemplateId注入bean，使用IP请求
	 * 1、使用IP请求的时候使用该bean
	 * 2、注入名为restTemplateId，使用的时候需要@Qualifier特定取出
	 * @author Hux
	 * @return
	 */
	@Bean("restTemplateIP")
	public RestTemplate getRestTemplateIP() {
		// TODO set up your restTemplate
		return getRestTemplate();
	}

	private RestTemplate getRestTemplate(){
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(clientHttpRequestFactory);
		StringHttpMessageConverter m = new StringHttpMessageConverter(Charset.forName("UTF-8"));
		restTemplate.getMessageConverters().set(1, m);
		return restTemplate;
	}

}
