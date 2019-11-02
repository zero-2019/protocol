package com.chuangxin.monitor.constants;


import com.chuangxin.monitor.entity.BmsEntity;
import com.chuangxin.monitor.entity.GatherSpotEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GatherSpotMap {

    private static final Logger logger = LoggerFactory.getLogger(GatherSpotMap.class);


    private static  Map<String, Map<String,String>> map = new HashMap<>();

    //检查测点是否需要存储
    public  boolean checkGatherSpot(String datcentId,String gatherSpot,JdbcTemplate jdbcTemplate){

        Map<String, String> datcentMap = map.get(datcentId);

        if (datcentMap == null || datcentMap.size() == 0){

            addAllGather(datcentId,jdbcTemplate);

        }
        logger.debug("当前map所有数据 : " + map);
        datcentMap = map.get(datcentId);

        if (datcentMap.containsKey(gatherSpot)){
            return true ;
        }
        return false;
    }

    //查询数据库中的 测点列表
    private   void addAllGather(String datcentId, JdbcTemplate  jdbcTemplate){

        List<GatherSpotEntity> gatherSpotEntities = new ArrayList<>();
        try {
            String sql = "SELECT datcentId,gatherSpot FROM gather_spot WHERE datcentId = ? ";
          gatherSpotEntities = jdbcTemplate.query(sql, new GatherSpotEntity(), new Object[]{datcentId});
          logger.info("查询数据库,根据数据中心ID" + datcentId + " 取出测点数据  :" +  gatherSpotEntities.size() +  "条");

        }catch (Exception e){
            logger.error(" jdbcTemplate查询出现问题 ：" + e.getMessage());
            e.printStackTrace();
        }

        Map<String, String> datcentMap = new HashMap<>();

        for (GatherSpotEntity gatherSpot:  gatherSpotEntities){
            datcentMap.put(gatherSpot.getGatherSpot(),"1");
        }
        map.put(datcentId,datcentMap);
        logger.info("当前map所有数据 : " + map);

    }


    // 更新集合中 需要采集的  bms测点
    public  void  updateGatherSpotBydatcetnId(BmsEntity bmsEntity, JdbcTemplate  jdbcTemplate){
        addAllGather(bmsEntity.getDatcentId(),jdbcTemplate);
    }



}
