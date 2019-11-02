package com.chuangxin.monitor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class BmsConfig {

    @Value("${bms.host}")
    public String bmsHost;

    @Value("${bms.collect-port}")
    public String collectPort;


    @Value("${bms.control-port}")
    public String controlPort;

}
