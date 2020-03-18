package com.newland.abdp.dfs;

import com.newland.spring.platcore.log.Log;
import com.newland.spring.platcore.log.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class DfsApplication {

    private static Log LOG = LogFactory.getLogger(DfsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(DfsApplication.class, args);
        LOG.info("dfs-server server start successfully!");
    }

}
