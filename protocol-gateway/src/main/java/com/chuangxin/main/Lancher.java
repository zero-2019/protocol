package com.chuangxin.main;

import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.DispatcherServlet;

@SpringBootApplication
@EnableFeignClients(basePackages={"com.chuangxin.monitor.service"})
@EnableDiscoveryClient
@ImportResource({"classpath:/applicationContext.xml"})
@EnableScheduling
@ComponentScan(basePackages={"com.chuangxin.monitor.*"})
public class Lancher {

    @Bean   //打印 feign日志
    public Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }


    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(Lancher.class, args);

        DispatcherServlet dispatcherServlet = (DispatcherServlet) ctx.getBean("dispatcherServlet");
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);


    }
    /**
     * 实例化RestTemplate
     *   data = restTemplate.getForObject("http://service-provider/getUser?id="+id,Map.class);
     * @return
     */
    @LoadBalanced
    @Bean
    public RestTemplate rest() {
        return new RestTemplate();
    }
}
