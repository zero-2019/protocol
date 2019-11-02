package com.chuangxin.monitor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class ZkConfig {


    @Value("${zookeeper.ipaddress}")
    public String ipaddress;

    @Value("${zookeeper.port:2181}")
    public String port;

    @Value("${zookeeper.connectTimeout:120000}")
    public String connectTimeout;

    @Value("${zookeeper.datcent-id}")
    public String datcentId;

}
