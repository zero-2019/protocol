package com.chuangxin.monitor.task;


import com.chuangxin.monitor.component.PulsarCompent;
import com.chuangxin.monitor.consumer.Zkwatched;
import com.chuangxin.monitor.producer.ProducerSend;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledService.class);


    @Autowired
    private ProducerSend producerSend;

    @Autowired
    private PulsarCompent pulsarCompent;

    @Autowired
    private Zkwatched zkwatched;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void scheduled(){

        //每分钟执行一次 读取水系统设备信息  和 风系统设备信息  发送到pulsar 中
        producerSend.send();


        //    新增设备和 测点出发下 zookeeper 这两个 路径得值  好 更新 原始值
        //  监听 zookeeper  路径是 /master/{LOC} /update-bacnetinfo  和  /master/{LOC}/update-gatherspot    LOC数据中心id,如果有测点添加或者有 bacnet设备新增，就要调用对用接口  zk版本 3.4.10
        if (!zkwatched.isIsStartListener()){
            try {
                logger.info("监听zookeeper，数据发生变更更新, 收集bacnet信息列表和共济bms测点列表,"+zkwatched.isIsStartListener());
                zkwatched.setIsStartListener(true);
                zkwatched.listenCurator();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }



        //检查是否开启pulsar，如果没有则开启    监听consumer   解析  algo  发送过来得JSON数据  解析  ，然后分为 bacnet 和 共济bms   分别发出控制信息

        //这个完成就 ba  bac和 bms  整合好了
        if (!pulsarCompent.isIsStartListener()){
            try {
                pulsarCompent.setIsStartListener(true);
               // pulsarCompent.watched();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }
}
