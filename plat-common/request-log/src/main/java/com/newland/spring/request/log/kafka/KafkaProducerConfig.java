package com.newland.spring.request.log.kafka;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaProducerConfig {
 
    @Bean("kafkaTemplate")
    public KafkaTemplate<String, String> kafkaTemplate() {
        KafkaTemplate<String, String> kafkaTemplate = new KafkaTemplate<String, String>(producerFactory());
        return kafkaTemplate;
    }

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServers;

    @Value("${spring.kafka.producer.retries}")
    private String retry;

    @Value("${spring.kafka.producer.batch-size}")
    private String batch;

    @Value("${spring.kafka.producer.buffer-memory}")
    private String mem;

    @Value("${spring.kafka.producer.key-serializer}")
    private String keySerializer;

    @Value("${spring.kafka.producer.value-serializer}")
    private String valueSerializer;

    @Value("${spring.kafka.producer.linger.ms}")
    private String lingerMs;

    @Value("${spring.kafka.producer.max.in.flight.requests.per.connection}")
    private String maxRequests;
    
   /* 不使用newhadoop搭建kafka集群，改自行搭建kafka集群，无需进行kb认证 ，注释一下代码
    @Value("${spring.kafka.security.protocol:SASL_PLAINTEXT}")
    private String protocol;
    
    @Value("${spring.kafka.sasl.mechanism:GSSAPI}")
    private String mechanism;
    
    @Value("${spring.kafka.kerberos.service.name:kafka}")
    private String kerberosServiceName;
    
    @Value("${java.security.auth.login.config}")
    private String authLoginConfig;
    
    @Value("${java.security.krb5.conf}")
    private String krb5Conf;*/
    
    public ProducerFactory<String, String> producerFactory() {
    	/* 不使用newhadoop搭建kafka集群，改自行搭建kafka集群，无需进行kb认证 ，注释一下代码
    	System.out.println("======set kb======");
    	System.setProperty("java.security.auth.login.config", authLoginConfig);
        System.setProperty("java.security.krb5.conf", krb5Conf);*/
    	
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,kafkaServers);
        properties.put(ProducerConfig.RETRIES_CONFIG, retry);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, batch);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, mem);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        properties.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxRequests);
        
       /* 不使用newhadoop搭建kafka集群，改自行搭建kafka集群，无需进行kb认证 ，注释一下代码
        //添加Kerberos认证配置
        properties.put("security.protocol", protocol);
        properties.put("sasl.mechanism", mechanism);
        properties.put("sasl.kerberos.service.name", kerberosServiceName);*/
        
        return new DefaultKafkaProducerFactory<String, String>(properties);
    }
}

