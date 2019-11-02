package com.chuangxin.monitor.controller;


import com.alibaba.fastjson.JSON;
import com.chuangxin.monitor.entity.ModbusEntity;
import com.chuangxin.monitor.service.ModbusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/protocol/modbus")
public class ModbusController {

    @Autowired
    private ModbusService modbusService;

    /**
     * modbus 写保持寄存器
     * @param modbusEntity      host 主站IP地址   port  端口号      slaveId 从站id     offset   偏移量    writeValue  修改值
     * @return
     */
    @RequestMapping(value = "/writeproperty",method = RequestMethod.POST)
    public String writeproperty(ModbusEntity modbusEntity){
        Map<String,Object> map = new HashMap<>();
        map.put("code","1");
        map.put("msg","success");
        String res = modbusService.writeproperty(modbusEntity);
        if (!"1".equals(res)){
            map.put("code","0");
            map.put("msg",res);
        }


        return JSON.toJSONString(map);
    }


    /**
     * modbus读保持寄存器
     * @param modbusEntity    host  主站IP地址   port  端口号      slaveId 从站id     offset   偏移量
     * @return
     */
    @RequestMapping(value = "/readproperty",method = RequestMethod.POST)
    public String readproperty(ModbusEntity modbusEntity){

        Map<String,Object> map = new HashMap<>();
        map.put("code","1");
        map.put("msg","success");

        int res = modbusService.readproperty(modbusEntity);
        if (!"1".equals(res)){
            map.put("code","0");
            map.put("msg",res);
        }


        return JSON.toJSONString(map);

    }
}
