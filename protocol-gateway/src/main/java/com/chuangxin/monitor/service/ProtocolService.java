package com.chuangxin.monitor.service;

import com.chuangxin.monitor.component.HystrixClientFallbackFactory;
import com.chuangxin.monitor.entity.BacnetEntity;
import com.chuangxin.monitor.entity.BmsEntity;
import com.chuangxin.monitor.entity.GatherSpotEntity;
import com.chuangxin.monitor.entity.ModbusEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient(name = "protocol-module",fallbackFactory = HystrixClientFallbackFactory.class)
public interface ProtocolService {

    /**
     * bacnet  写设备
     * @param bacnetEntity
     * @return
     */
    @RequestMapping(value = "/protocol/bacnet/writeproperty",method= RequestMethod.POST)
     String writeByBacnet(@SpringQueryMap BacnetEntity bacnetEntity);

    /**
     * bacnet  读设备
     * @param bacnetEntity
     * @return
     */
    @RequestMapping(value = "/protocol/bacnet/readproperty",method= RequestMethod.POST)
    String readByBacnet(@SpringQueryMap BacnetEntity bacnetEntity);


    /**
     * modbus 写设备
     * @param modbusEntity
     * @return
     */

    @RequestMapping(value = "/protocol/modbus/writeproperty",method= RequestMethod.POST)
    String writeBymodbus(@SpringQueryMap ModbusEntity modbusEntity);

    /**
     * modbus  读设备
     * @param modbusEntity
     * @return
     */
    @RequestMapping(value = "/protocol/modbus/readproperty",method= RequestMethod.POST)
    String readBymodbus(@SpringQueryMap ModbusEntity modbusEntity);


    /**
     * 共济 bms    写设备
     * @param bmsEntity
     * @return
     */
    @RequestMapping(value = "/protocol/bms/writeproperty",method= RequestMethod.POST)
    String writeBybms(@SpringQueryMap BmsEntity bmsEntity);


    /**
     * 共济 bms   读设备
     * @param   bmsEntity
     * @return
     */
    @RequestMapping(value = "/protocol/bms/readproperty",method= RequestMethod.POST)
    String readBybms(@SpringQueryMap BmsEntity bmsEntity);


    /**
     * 根据 数据中心id    更新共济bms采数的测点检测集合中
     * @param   bmsEntity
     * @return
     */
    @RequestMapping(value = "/protocol/bms/updateGatherSpotBydatcetnId",method= RequestMethod.POST)
    String updateGatherSpotBydatcetnId(@SpringQueryMap BmsEntity bmsEntity);




}



