package com.chuangxin.monitor.consumer;


import com.chuangxin.monitor.component.PulsarCompent;
import com.chuangxin.monitor.config.ZkConfig;
import com.chuangxin.monitor.entity.BmsEntity;
import com.chuangxin.monitor.producer.BacnetProducer;
import com.chuangxin.monitor.service.ProtocolService;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;


@Component
public class Zkwatched {

    private static final Logger logger = LoggerFactory.getLogger(PulsarCompent.class);

    public  boolean isStartListener = false;

    private static RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
    private static CountDownLatch watch = new CountDownLatch(1);

    private static   String  UPDATE_BACNETINFO = "update_bacnetinfo" ;
    private static   String  UPDATE_GATHERSPOT = "update_gatherspot" ;

    @Autowired
    private ZkConfig zkConfig;

    @Autowired
    private ProtocolService protocolService;

    @Autowired
    private BacnetProducer bacnetProducer;

    private  CuratorFramework client;


    public void listenCurator()  {

            logger.info("init  curator  client.........");
            client = initCurator();
            // 两个节点   /master/{LOC} /update-bacnetinfo  和  /master/{LOC}/update-gatherspot
            String nodePath1 = "/master/" + zkConfig.datcentId + "/" + UPDATE_BACNETINFO;
           createNode(nodePath1);
           String nodePath2 = "/master/" + zkConfig.datcentId + "/" + UPDATE_GATHERSPOT;
           createNode(nodePath2);

            try {
                watched(nodePath1,nodePath2);
            } catch (Exception e) {
                closeZnode();
                e.printStackTrace();
            }

        }


    private void   watched(String nodePath1,String nodePath2) throws Exception {

        // 监听父节点以下所有的子节点, 当子节点发生变化的时候(增删改)都会监听到
        // 为子节点添加watcher事件
        // PathChildrenCache监听数据节点的增删改
        final PathChildrenCache childrenCache = new PathChildrenCache(client, "/master/BJ2", true);
        // NORMAL:异步初始化, BUILD_INITIAL_CACHE:同步初始化, POST_INITIALIZED_EVENT:异步初始化,初始化之后会触发事件
        childrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        List<ChildData> childDataList = childrenCache.getCurrentData(); // 当前数据节点的子节点数据列表
        childrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                if (event.getType().equals(PathChildrenCacheEvent.Type.INITIALIZED)) {
                    logger.info("子节点初始化ok..");
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_ADDED)) {
                    logger.info("添加子节点, path:{}, data:{}", event.getData().getPath(), event.getData().getData());
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                    logger.info("删除子节点, path:{}", event.getData().getPath());
                } else if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_UPDATED)) {
                    logger.info("修改子节点, path:{}, data:{}", event.getData().getPath(), event.getData().getData());
                    updateCollectInfos(event.getData().getPath());
                }
            }
        });

    }

    private void updateCollectInfos(String updatepath) {
        String nodePath1 = "/master/" + zkConfig.datcentId + "/" + UPDATE_BACNETINFO;
        String nodePath2 = "/master/" + zkConfig.datcentId + "/" + UPDATE_GATHERSPOT;

        if (nodePath1.equals(updatepath)){
            logger.info("更新bacnet设备信息 ");
            bacnetProducer.updateBacnetinfosBydatcentId(zkConfig.datcentId);
        }
        if (nodePath2.equals(updatepath)){
            logger.info("更新共济bms测点信息  ");
            BmsEntity bmsEntity = new BmsEntity();
            bmsEntity.setDatcentId(zkConfig.datcentId);
            String s = protocolService.updateGatherSpotBydatcetnId(bmsEntity);
            System.err.println(s);
        }

    }


    public CuratorFramework initCurator(){

        client = CuratorFrameworkFactory.builder().connectString(zkConfig.ipaddress+  ":" + zkConfig.port).sessionTimeoutMs(15000).retryPolicy(retryPolicy).build();
        client.start();
        return client;
    }


    private void createNode( String  nodePath){
        // 判断是否存在，Stat就是对znode所有属性的一个映射，stat=null表示节点不存在
        try {
            Stat stat = client.checkExists().forPath(nodePath);
            if (stat == null){
                // 创建节点
                client.create().creatingParentsIfNeeded() // 若创建节点的父节点不存在则先创建父节点再创建子节点
                        .withMode(CreateMode.PERSISTENT) // 创建的是持久节点
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE) // 默认匿名权限,权限scheme id:'world,'anyone,:cdrwa
                        .forPath(nodePath,"1".getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("create curator client error .....");
        }

    }

    // 关闭连接
    private  void closeZnode() {
        if (client != null) {
            client.close();
        }
    }

    public  boolean isIsStartListener() {
        return this.isStartListener;
    }

    public  void setIsStartListener(boolean isStartListener) {
        this.isStartListener = isStartListener;
    }

}
