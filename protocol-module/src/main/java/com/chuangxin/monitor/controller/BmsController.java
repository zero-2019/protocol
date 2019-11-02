package com.chuangxin.monitor.controller;


import com.chuangxin.monitor.constants.GatherSpotMap;
import com.chuangxin.monitor.entity.BmsEntity;
import com.chuangxin.monitor.entity.GatherSpotEntity;
import com.chuangxin.monitor.service.BmsService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/protocol/bms")
public class BmsController {
    private static final Logger logger = LoggerFactory.getLogger(BmsController.class);

    @Autowired
    private BmsService bmsService;



    /**
     * 获取共济bms 测点数据
     * @param bmsEntity  host
     * @param bmsEntity  port
     * @param bmsEntity  datcentId
     * @param bmsEntity  sign    默认为 true   如果传false 会对socket 进行重新连接
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/readproperty",method = RequestMethod.POST)
    public Map<String,String> readproperty(BmsEntity bmsEntity) throws Exception {
       if (StringUtils.isBlank(bmsEntity.getHost()) ){
           logger.error("必传参数 : key = host  => socket连接的IP没有给出  ");
           throw new Exception("缺少参数  : key = host  => socket连接的IP没有给出");
       }
        if (StringUtils.isBlank(bmsEntity.getPort()) ){
            logger.error("必传参数 : key = port  => socket连接的port没有给出 ");
            throw new Exception("缺少参数 : key = port  => socket连接的port没有给出 ");
        }
        if (StringUtils.isBlank(bmsEntity.getDatcentId()) ){
            logger.error("必传参数 : key = datcentId   => 需要指定连接的数据中心id");
            throw new Exception("缺少参数  : key = datcentId   => 需要指定连接的数据中心id");
        }

        Map<String,String> result = bmsService.readproperty(bmsEntity);

       return  result;
    }

    /**
     * 根据 数据中心id    更新共济bms采数的测点检测集合中
     * @param bmsEntity  datcentId
     * @throws Exception
     */
    @RequestMapping(value = "/updateGatherSpotBydatcetnId",method = RequestMethod.POST)
    public void addGatherSpot(BmsEntity bmsEntity) throws Exception {
        if (StringUtils.isBlank(bmsEntity.getDatcentId()) ){
            logger.error("必传参数 : key = datcentId   => 需要指定连接的数据中心 添加测点 ");
            throw new Exception("缺少参数  : key = datcentId   => 需要指定连接的数据中心 添加测点 ");
        }
        bmsService.updateGatherSpotBydatcetnId( bmsEntity);

    }




    @RequestMapping(value = "/writeproperty",method = RequestMethod.POST)
    public String  writeproperty(BmsEntity bmsEntity) throws Exception {

        return "";

    }

    
    
}
