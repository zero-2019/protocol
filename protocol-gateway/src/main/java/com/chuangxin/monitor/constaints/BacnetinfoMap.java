package com.chuangxin.monitor.constaints;

import com.chuangxin.monitor.entity.DeviceBacnetInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BacnetinfoMap {
    private static final Logger logger = LoggerFactory.getLogger(BacnetinfoMap.class);

    private static Map<String,Map<Integer, DeviceBacnetInfo>>   map = new HashMap<>();



    private   void   addAllBacnetinfoBydatcentId(String datcentId ,JdbcTemplate jdbcTemplate){
        Map<Integer, DeviceBacnetInfo>  datcentIdMap = new HashMap<>();

        String sql =  "SELECT id,datcent_id, broadcast_ip,broeadcast_port ,device_ip ,device_id ,property_name ,status,device_no FROM device_bacnet_info WHERE  " +
                "datcent_id = ? AND status = '1' ";

        List<DeviceBacnetInfo> DeviceBacnetInfos = jdbcTemplate.query(sql, new DeviceBacnetInfo(), new Object[]{datcentId});

        for (DeviceBacnetInfo tmp: DeviceBacnetInfos){
            datcentIdMap.put(tmp.getId(),tmp);
        }
        map.put(datcentId,datcentIdMap);

        logger.info("bacnet设备共计："+ datcentIdMap.size());
    }

    /**
     * 获取  bacnet 设备列表
     * @param datcentId
     * @param jdbcTemplate
     * @return
     */
    public   Map<Integer, DeviceBacnetInfo>  listBacnetinfoBydatcentId(String datcentId ,JdbcTemplate jdbcTemplate){

        Map<Integer, DeviceBacnetInfo> datcentIdMap = map.get(datcentId);
        if (datcentIdMap == null ||  datcentIdMap.size() == 0){
            addAllBacnetinfoBydatcentId(datcentId,jdbcTemplate);
        }

        datcentIdMap = map.get(datcentId);

        return datcentIdMap ;
    }


    public  void updateBacnetinfosBydatcentId(String datcentId ,JdbcTemplate jdbcTemplate){

        addAllBacnetinfoBydatcentId(datcentId,jdbcTemplate);

    }




}
