package com.chuangxin.monitor.producer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chuangxin.monitor.constaints.BacnetinfoMap;
import com.chuangxin.monitor.entity.BacnetEntity;
import com.chuangxin.monitor.entity.DeviceBacnetInfo;
import com.chuangxin.monitor.service.ProtocolService;
import org.apache.commons.lang.StringUtils;
import org.apache.pulsar.shade.org.apache.http.client.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class BacnetProducer {

    private static final Logger logger = LoggerFactory.getLogger(BacnetProducer.class);

    @Autowired
    private ProtocolService  protocolService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BacnetinfoMap bacnetinfoMap;



    public  Map<String, String> readBacnetinfos(Map<Integer, DeviceBacnetInfo> allBacnetinfo) {
        Map<String, String>  tag2Value = new HashMap<>();
        for (DeviceBacnetInfo deviceBacnetInfo: allBacnetinfo.values()){
            BacnetEntity bacnetEntity = new BacnetEntity();
            bacnetEntity.setIp(deviceBacnetInfo.getDeviceIp());
            bacnetEntity.setDeviceId(Integer.parseInt(deviceBacnetInfo.getDeviceId()));
            bacnetEntity.setPropertyName(deviceBacnetInfo.getPropertyName());
            bacnetEntity.setBroadcastIp(deviceBacnetInfo.getBroadcastIp());

            String res = protocolService.readByBacnet(bacnetEntity);
            logger.debug("水系统获取设备返回 ==>"+res);
            JSONObject jsonObject = JSON.parseObject(res);
            if (jsonObject == null ){
                logger.warn("水系统没有获取到设备值,原因是调用接口没有返回数据，调用接口返回信息为空设备原始信息 ==>" + deviceBacnetInfo.toString());
                continue;
            }
            int status = jsonObject.getIntValue("status");
            String result = jsonObject.getString("result");

            if (status == 200 && StringUtils.isNotBlank(result)){
               //拼接key   deviceid +  "#" + property_name
                String key =  deviceBacnetInfo.getDeviceId() + "#"  + deviceBacnetInfo.getPropertyName();
                tag2Value.put(key,result);

            }else{
                logger.warn("水系统没有获取到设备值，设备原始信息  ==>" + deviceBacnetInfo.toString());
                String key =  deviceBacnetInfo.getDeviceId() + "#"  + deviceBacnetInfo.getPropertyName();
            }

        }
        return tag2Value;
    }



    public  void  updateBacnetinfosBydatcentId(String datcentId){
        bacnetinfoMap.updateBacnetinfosBydatcentId(datcentId,jdbcTemplate);


    }

}
