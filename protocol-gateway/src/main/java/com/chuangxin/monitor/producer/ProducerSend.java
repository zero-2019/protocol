package com.chuangxin.monitor.producer;

import com.alibaba.fastjson.JSON;
import com.chuangxin.monitor.config.BmsConfig;
import com.chuangxin.monitor.config.PulsarConfig;
import com.chuangxin.monitor.constaints.BacnetinfoMap;
import com.chuangxin.monitor.entity.BmsEntity;
import com.chuangxin.monitor.entity.DeviceBacnetInfo;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class ProducerSend {

    private static final String TIMESTAMP_FIELD_NAME = "@timestamp";

    private static final Logger logger = LoggerFactory.getLogger(ProducerSend.class);

    private  static Producer<byte[]> producer = null;

    @Autowired
    private PulsarConfig pulsarConfig;

    @Autowired
    private BmsConfig bmsConfig;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BacnetProducer bacnetProducer;

    @Autowired
    private BmsProducer bmsProducer;

    @Autowired
    private BacnetinfoMap bacnetinfoMap;

    private   Producer initProducer(){
        try {
            PulsarClient client = PulsarClient.builder().serviceUrl(pulsarConfig.pulsarServiceUrl).build();

            Producer<byte[]> producer = client.newProducer()
                    .topic(pulsarConfig.sensorDataTopic)
                    .batchingMaxPublishDelay(10, TimeUnit.MILLISECONDS)
                    .sendTimeout(10, TimeUnit.SECONDS)
                    .blockIfQueueFull(true)
                    .create();

            return producer;
        } catch (PulsarClientException e) {
            logger.warn("connect pulsar  failed ，reason:"  + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public  void send(){

        Map<String,String>  tag2Value = collectTag2Value();
        if (tag2Value.size() == 0 ){
            logger.warn("水系统和风系统都没有获取到值");
            return;
        }
        //初始化  pulsar  producer
        if (producer == null ){
            logger.info("init pulsar  and new  producer ");
            producer = initProducer();
        }

        ZonedDateTime now = ZonedDateTime.now();
        String timestampNow = now.format(DateTimeFormatter.ISO_INSTANT);
        tag2Value.put(TIMESTAMP_FIELD_NAME,timestampNow);

        //send
        String message = JSON.toJSONString(tag2Value);
        logger.info("send message to :" + pulsarConfig.sensorDataTopic + " <===> message: "+  message);
        producer.sendAsync(message.getBytes());
    }

    private Map<String, String>  collectTag2Value() {
        Map<String, String> tag2Value = new HashMap<>();

        //1. 获取水系统  信息值  通过 bacnet协议
        Map<Integer, DeviceBacnetInfo> allBacnetinfo = bacnetinfoMap.listBacnetinfoBydatcentId(pulsarConfig.datcentId,jdbcTemplate);
        if (allBacnetinfo.values().size() == 0){
            logger.warn("水系统没有设备需要通过bacnet协议读取数据导入pulsar，请检查mysql里是否有数据");
        }
        if (allBacnetinfo.values().size() >  0){
            logger.debug("水系统读取设备数量  ===>"+allBacnetinfo.values().size());

            Map<String, String> waterTag2Value = bacnetProducer.readBacnetinfos(allBacnetinfo);
            if (waterTag2Value.size() ==  0) logger.warn("水系统读取设备信息值数量为零");

            if (waterTag2Value.size() >  0){
                tag2Value.putAll(waterTag2Value);
            }
        }


        //2.获取风系统 设备信息值  通过 共济 bms  socket
        BmsEntity bmsEntity = new BmsEntity();
        bmsEntity.setHost(bmsConfig.bmsHost);
        bmsEntity.setPort(bmsConfig.collectPort);
        bmsEntity.setDatcentId(pulsarConfig.datcentId);
        Map<String, String> bmsTag2Value = bmsProducer.readBmsGathers(bmsEntity);
        if (bmsTag2Value.size() ==  0) logger.warn("bms读取设备信息值数量为零");

        if (bmsTag2Value.size() >  0){
            tag2Value.putAll(bmsTag2Value);
        }


        //3.数据放到一起发送到pulsar
        return tag2Value;

    }


}
