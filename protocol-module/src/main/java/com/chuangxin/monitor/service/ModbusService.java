package com.chuangxin.monitor.service;

import com.chuangxin.monitor.entity.ModbusEntity;

public interface ModbusService {


    public String writeproperty(ModbusEntity modbusEntity);


    public int readproperty(ModbusEntity modbusEntity);
}
