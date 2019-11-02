package com.chuangxin.monitor.controller;
import com.chuangxin.monitor.entity.BmsEntity;
import com.chuangxin.monitor.entity.DeviceBacnetInfo;
import com.chuangxin.monitor.producer.BacnetProducer;
import com.chuangxin.monitor.service.ProtocolService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value="/protocol/gateway")
public class GatewayController   {

    @Autowired
    private ProtocolService protocolService;

    @Autowired
    private BacnetProducer bacnetProducer;







    }
