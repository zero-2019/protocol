package com.chuangxin.monitor.consumer;

import com.chuangxin.monitor.config.AlgoOutput;
import com.chuangxin.monitor.config.NameValues;
import com.chuangxin.monitor.entity.BacnetEntity;
import com.chuangxin.monitor.entity.DeviceBacnetInfo;
import com.chuangxin.monitor.service.ProtocolService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pulsar.client.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AlgoConsumer  implements  Runnable{

    private static final Logger logger = LoggerFactory.getLogger(AlgoConsumer.class);
    private static  String  PROPERTY_CHW_OUT = "t_chw_out";  //冷冻水出水温度
    private PulsarClient client;


    private String topicName;

    private String subscriptionName;

    private ProtocolService protocolService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run() {


        Consumer consumer = null;
        try {
            do {
                //订阅 topic
                 consumer = client.newConsumer()
                        .topic(topicName)
                        .subscriptionType(SubscriptionType.Exclusive)
                        .subscriptionName(subscriptionName)
                        .subscribe();
                consumer.seek(MessageId.latest);

                Message msg = consumer.receive(1200, TimeUnit.SECONDS);

                logger.info("algo-output consumer started");
                if (null == msg) {
                    logger.warn("Timeout to receive algo message, start to turn ac switch to auto");

                } else {
                    consumer.acknowledgeCumulative(msg);
                    logger.info("message received");
                    onReceiveAlgoOutput(msg);
                }
            } while (true);
        } catch ( Exception e) {
            logger.error("Fail to consume algo result", e);
        } finally {
            logger.info("Exit");
            try {
                consumer.unsubscribe();
            } catch (PulsarClientException e) {
                e.printStackTrace();
            }
        }

    }


    //解析消息    分别判断是 bacnet协议 还是 modbus协议 然后进行读写操作  现在只有 bacnet
    private void onReceiveAlgoOutput(Message msg) {
        //解析msg
        try {
            AlgoOutput algoOutput = objectMapper.readValue(String.valueOf(msg.getValue()), AlgoOutput.class);
            //没有要求控制
            if (!algoOutput.isToControl()){
                return;
            }

            control(algoOutput.getControl().getCowCircuit());
            control(algoOutput.getControl().getChiller());
            control(algoOutput.getControl().getChwCircuit().getFirstChwCircuit());
            control(algoOutput.getControl().getChwCircuit().getSecondChwCircuit());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void control(List<NameValues<Integer>> cowCircuits) {

        if (cowCircuits == null) {
            return;
        }
        List<DeviceBacnetInfo>   machineUnitControlInfos = new ArrayList<>();
        //此处需要根据情况去数据库查询到设备的列表送
        if (null == machineUnitControlInfos) {
            logger.warn("No BA control point info found.");
            return;
        }

        for ( int i=0;i<cowCircuits.size();i++){
            String unitName = cowCircuits.get(i).getName();
            Map<String, Integer> values = cowCircuits.get(i).getValues();

            for (Map.Entry<String, Integer> entry : values.entrySet()) {
                String propertyType = entry.getKey();
                int value = entry.getValue();


                if (machineUnitControlInfos.size() > 0) {
                    int  target =  value;
                    if (propertyType.equalsIgnoreCase(PROPERTY_CHW_OUT)) {
                        target = (value - 13) * 10;
                    }


                    BacnetEntity bacnetEntity = new BacnetEntity();
                    String res = protocolService.writeByBacnet(bacnetEntity);

                } else {
                    logger.warn("Fail to find bacnet config for $unitName:$propertyType");
                }

            }

        }
    }




    public PulsarClient getClient() {
        return client;
    }

    public void setClient(PulsarClient client) {
        this.client = client;
    }



    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public ProtocolService getProtocolService() {
        return protocolService;
    }

    public void setProtocolService(ProtocolService protocolService) {
        this.protocolService = protocolService;
    }

    public AlgoConsumer(PulsarClient client, String topicName, String subscriptionName, ProtocolService protocolService) {
        this.client = client;
        this.topicName = topicName;
        this.subscriptionName = subscriptionName;
        this.protocolService = protocolService;
    }


}
