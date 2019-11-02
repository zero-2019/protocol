package com.chuangxin.monitor.service.impl;

import com.chuangxin.monitor.component.BmsClient;
import com.chuangxin.monitor.component.FeatureCollector;
import com.chuangxin.monitor.entity.BmsEntity;
import com.chuangxin.monitor.service.BmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class BmsServiceImpl implements BmsService {

    private static final Logger logger = LoggerFactory.getLogger(BmsServiceImpl.class);

    @Autowired
    private BmsClient bmsClient;

    @Autowired
    private FeatureCollector collector;



    @Override
    public Map<String,String> readproperty(BmsEntity bmsEntity) {


        //是否之前已经连接过socket    0是未 接入socket 1是已接入socket

        Integer datcentSign = FeatureCollector.sign.get(bmsEntity.getDatcentId());
        logger.info("数据中心" + bmsEntity.getDatcentId() + " , socket状态 【0和null未连接 1已连接】: " +  FeatureCollector.sign.get(bmsEntity.getDatcentId()));
        if (datcentSign ==null){
            FeatureCollector.sign.put(bmsEntity.getDatcentId(),0);
        }

        //  false  0   true  1   默认  true
        if (!bmsEntity.isSign()){
            FeatureCollector.sign.put(bmsEntity.getDatcentId(),0);
        }

        if (0 ==FeatureCollector.sign.get(bmsEntity.getDatcentId()) ){
            bmsClient.startBmsSocket(bmsEntity);
        }

        FeatureCollector.sign.put(bmsEntity.getDatcentId(),1);

        Map<String,String> message = collector.getMessage(bmsEntity.getDatcentId());

        return message;
    }


    public void    updateGatherSpotBydatcetnId(BmsEntity bmsEntity){
        collector.updateGatherSpotBydatcetnId(bmsEntity);


    }

    @Override
    public String writeproperty(BmsEntity bmsEntity) {
        return null;
    }
}
