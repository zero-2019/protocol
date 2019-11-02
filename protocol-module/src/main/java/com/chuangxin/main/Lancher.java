package com.chuangxin.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@ImportResource({"classpath:/applicationContext.xml"})
@EnableAsync
@ComponentScan(basePackages={"com.chuangxin.monitor.*"})
public class Lancher {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Lancher.class, args);
        DispatcherServlet dispatcherServlet = (DispatcherServlet) ctx.getBean("dispatcherServlet");
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
    }


}

