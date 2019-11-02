package com.chuangxin.monitor.service;

import com.chuangxin.monitor.entity.BmsEntity;

import java.util.Map;

/**
 * 通过共济bms读写数据
 */
public interface BmsService {

    public Map<String,String> readproperty(BmsEntity bmsEntity);

    public String writeproperty(BmsEntity bmsEntity);

    public void    updateGatherSpotBydatcetnId(BmsEntity bmsEntity);

}
