package com.chuangxin.monitor.component;


import com.chuangxin.monitor.constants.GatherSpotMap;
import com.chuangxin.monitor.entity.BmsEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.*;


@Component
public class FeatureCollector {

    private static final Logger logger = LoggerFactory.getLogger(FeatureCollector.class);
    public static   Map<String, Integer> sign = new HashMap<>() ;
    private static Map<String, Map<String,String>> map = new HashMap<>();


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GatherSpotMap gatherSpotMap;

    /*
     * 由于BMS初始包发送设备结构树，数据量大，等初始包接收结束后再记录，避免记录大量未初始的测点值；
     * 另外初始包中测点值可能不准，延迟记录数据可能会等来正确的数据
     * */



    public  void  process(String datcentId,String rawKv ) {
        if (rawKv.contains("-NA")) {

            String[] kv = rawKv.split("\\|");
            String tagNo  = "";
            if (kv[0].endsWith("-NA")){
                String[] tagNoSplit = kv[0].substring(0, kv[0].length() - "-NA".length()).split("-");
                for (int i= 0; i < tagNoSplit.length ; i++){
                    tagNo += tagNoSplit[i];
                }
                tagNo = tagNo.toUpperCase();
            }

            String  tagName  = kv[1];
            logger.debug("包含-NA ==>tagNo =>%s  :  tagName =>%s".format(tagNo, tagName));
        }

        if (!rawKv.contains("-VA")) {
            return;
        }
        String [] kv = rawKv.split("\\|");

        if (kv.length != 2) {
            return;
        }
        String tagNo = "";
        if (kv[0].endsWith("-VA")){
            String[] tagNoSplit = kv[0].substring(0, kv[0].length() - "-VA".length()).split("-");
            for (int i= 0; i < tagNoSplit.length ; i++){
                tagNo += tagNoSplit[i];
            }
            tagNo = tagNo.toUpperCase();
        }
        String  value  = kv[1];
        logger.debug("包含 -VA的值 ===> tagNo : "+tagNo+" ,value:  " + value);

        //需要判断测点是否需要存储  查询mysql

        Map<String, String> tag2Value = map.get(datcentId);
        if (tag2Value ==null ){
            tag2Value = new HashMap<>();
            map.put(datcentId,tag2Value);
        }


        boolean checkResult = gatherSpotMap.checkGatherSpot(datcentId, tagNo, jdbcTemplate);
        if (checkResult){
            tag2Value.put(tagNo,value);
        }

    }

    public void updateGatherSpotBydatcetnId(BmsEntity bmsEntity){
        gatherSpotMap.updateGatherSpotBydatcetnId(bmsEntity,jdbcTemplate);

    }

    public Map<String,String> getMessage(String datcentId){
       // String   message = JSON.toJSONString(tag2Value);
        return map.get(datcentId);
    }






}
