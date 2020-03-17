package com.newland.abdp.gateway;

import com.newland.spring.platcore.log.Log;
import com.newland.spring.platcore.log.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class GatewayApplication {

    private static Log LOG = LogFactory.getLogger(GatewayApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
        LOG.info("gateway server start successfully!");
    }

}
