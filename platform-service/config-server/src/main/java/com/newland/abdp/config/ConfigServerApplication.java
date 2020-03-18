package com.newland.abdp.config;

import com.newland.spring.platcore.log.Log;
import com.newland.spring.platcore.log.LogFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class ConfigServerApplication {

	private static Log LOG = LogFactory.getLogger(ConfigServerApplication.class);

	public static void main(String[] args) {
		new SpringApplicationBuilder(ConfigServerApplication.class).run(args);
		LOG.info("config server start successfully!");
	}

}
