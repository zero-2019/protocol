package com.chuangxin.monitor.controller;

import com.chuangxin.monitor.entity.BacnetEntity;
import com.chuangxin.monitor.service.BacnetReader;
import com.chuangxin.monitor.service.BacnetService;
import com.chuangxin.monitor.service.BacnetWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/protocol/bacnet")
public class BacnetController   {

    private static final Logger logger = LoggerFactory.getLogger(BacnetController.class);


    @Autowired
    private BacnetService bacnetService;
    /**
     * bacnet  控制设备 控制水系统的温度的操作
     * @param bacnetEntity
     * @return
     */
    @RequestMapping(value = "/writeproperty",method = RequestMethod.POST)
    public void writeproperty(BacnetEntity bacnetEntity) throws Exception {


        float value = Float.parseFloat((String)bacnetEntity.getChangeValue());
        BacnetWriter bacnetWriter = new BacnetWriter(bacnetEntity.getBroadcastIp());
        String result = bacnetWriter.writeProperty(bacnetEntity.getIp(), bacnetEntity.getDeviceId(), bacnetEntity.getPropertyName(), value);
        if (!"1".equals(result)){
            throw new Exception();
        }

    }

    /**
     * bacnet 协议 读取  设备 信息
     * @param bacnetEntity
     * @return
     */
    @RequestMapping(value = "/readproperty",method = RequestMethod.POST)
    public String readproperty(BacnetEntity bacnetEntity) throws Exception {

        BacnetReader bacnetReader = new BacnetReader(bacnetEntity.getBroadcastIp());
        String res = bacnetReader.readProperty(bacnetEntity.getIp(),bacnetEntity.getDeviceId(),bacnetEntity.getPropertyName());

        if ("error_return".equals(res)) {
           return "";
        }


        return res;
    }
}
