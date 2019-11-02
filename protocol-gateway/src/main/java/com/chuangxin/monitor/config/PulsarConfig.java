package com.chuangxin.monitor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class PulsarConfig {


    @Value("${pulsar.service-url:pulsar://localhost:6650}")
    public String pulsarServiceUrl;

    @Value("${pulsar.topic.algo-output}")
    public String algoOutputTopicName;

    @Value("${pulsar.topic.sensor-data}")
    public String sensorDataTopic;

    @Value("${pulsar.datcent-id}")
    public String datcentId;




}
