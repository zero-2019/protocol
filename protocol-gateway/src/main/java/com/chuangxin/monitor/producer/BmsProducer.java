package com.chuangxin.monitor.producer;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chuangxin.monitor.entity.BmsEntity;
import com.chuangxin.monitor.service.ProtocolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class BmsProducer {

    private static final Logger logger = LoggerFactory.getLogger(BmsProducer.class);

    @Autowired
    private ProtocolService protocolService;


    public Map<String, String>  readBmsGathers(BmsEntity bmsEntity){
        Map<String, String>  tag2Value = new HashMap<>();

        String res = protocolService.readBybms(bmsEntity);
        JSONObject jsonObject = JSON.parseObject(res);

        if (jsonObject == null ){
            logger.warn("bms 没有获取到设备值,原因是调用接口没有返回数据，调用接口返回信息为空,设备原始信息 ==>" + bmsEntity.toString());
            return  tag2Value;
        }
        int status = jsonObject.getIntValue("status");
        if (status != 200){
            logger.warn("bms没有获取到设备值，检查protocol-module模块，设备原始信息  ==>" + bmsEntity.toString());
            return tag2Value;
        }
        tag2Value = jsonObject.getObject("result",Map.class );


        return tag2Value;
    }
}

