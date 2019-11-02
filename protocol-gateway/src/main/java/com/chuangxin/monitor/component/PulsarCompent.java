package com.chuangxin.monitor.component;

import com.chuangxin.monitor.config.PulsarConfig;
import com.chuangxin.monitor.consumer.AlgoConsumer;
import com.chuangxin.monitor.service.ProtocolService;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class PulsarCompent  {

    private static final Logger logger = LoggerFactory.getLogger(PulsarCompent.class);

    @Autowired
    private PulsarConfig pulsarConfig;

    @Autowired
    private ProtocolService protocolService;

    private String  algoOutputSubscriptionName = "algo-output-consumer";


    public  boolean isStartListener = false;



    public void watched() throws Exception {
        try {
            PulsarClient client = PulsarClient.builder()
                    .serviceUrl(pulsarConfig.pulsarServiceUrl)
                    .build();


            //订阅pulsar的 topic   接收算法输出的消息 控制设备
            AlgoConsumer algoConsumer = new AlgoConsumer(client,pulsarConfig.algoOutputTopicName,algoOutputSubscriptionName,protocolService);
            logger.info("start AlgoConsumer.............");
            Thread algo = new Thread(algoConsumer);
            algo.start();
            algo.join();



        } catch (PulsarClientException e) {
            e.printStackTrace();
        }

    }

    public  boolean isIsStartListener() {
        return this.isStartListener;
    }

    public  void setIsStartListener(boolean isStartListener) {
        this.isStartListener = isStartListener;
    }
}
