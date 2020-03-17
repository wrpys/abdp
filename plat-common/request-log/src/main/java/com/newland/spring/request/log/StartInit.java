package com.newland.spring.request.log;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * @Auther: garfield
 * @Date: 2019/5/15 上午8:52
 * @Description:
 */
@Component
@Order(value=3)
public class StartInit implements CommandLineRunner {


    public static Boolean LOG_KAFKA = false;


    @Value("${log.kafka:true}")
    private boolean logStart;

    @Value("${log.kafka.local:true}")
    private boolean logStartLocal;

    @Override
    public void run(String... args) throws Exception {
        /**
         * avoid variables (logStart and logStartLocal) being initialized in multiple places
         */
        System.out.println("===============init===============");
        System.out.println("logStart:" + logStart + " logStartLocal:" + logStartLocal);
        LOG_KAFKA = logStart&&logStartLocal;
    }

}

