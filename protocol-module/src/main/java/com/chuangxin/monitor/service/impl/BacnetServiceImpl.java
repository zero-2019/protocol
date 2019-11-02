package com.chuangxin.monitor.service.impl;

import com.alibaba.fastjson.JSON;
import com.chuangxin.monitor.constants.BacnetConstants;
import com.chuangxin.monitor.entity.BacnetEntity;
import com.chuangxin.monitor.service.BacnetService;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.RemoteDevice;
import com.serotonin.bacnet4j.enums.MaxApduLength;
import com.serotonin.bacnet4j.exception.BACnetException;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkUtils;
import com.serotonin.bacnet4j.service.acknowledgement.ReadPropertyAck;
import com.serotonin.bacnet4j.service.confirmed.ReadPropertyRequest;
import com.serotonin.bacnet4j.service.confirmed.WritePropertyRequest;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.type.constructed.PriorityArray;
import com.serotonin.bacnet4j.type.constructed.SequenceOf;
import com.serotonin.bacnet4j.type.enumerated.PropertyIdentifier;
import com.serotonin.bacnet4j.type.enumerated.Segmentation;
import com.serotonin.bacnet4j.type.primitive.ObjectIdentifier;
import com.serotonin.bacnet4j.type.primitive.Real;
import com.serotonin.bacnet4j.util.PropertyReferences;
import com.serotonin.bacnet4j.util.PropertyValues;
import com.serotonin.bacnet4j.util.RequestUtils;
import org.apache.commons.lang3.mutable.Mutable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class BacnetServiceImpl implements BacnetService {

    private static final Logger logger = LoggerFactory.getLogger(BacnetServiceImpl.class);
    private static Map<Integer, Map<String, ObjectIdentifier>> deviceMap = new HashMap<>();





    private ObjectIdentifier  getObjectIdentifier( LocalDevice localDevice,  RemoteDevice remoteDevice,  String propertyName){
        logger.info("获取ObjectIdentifier 如果为空就去查询所有");
        int deviceId = remoteDevice.getInstanceNumber();
        Map<String, ObjectIdentifier> stringObjectIdentifierHashMap = deviceMap.get(deviceId);

        if(stringObjectIdentifierHashMap == null || null == deviceMap.get(deviceId).get(propertyName)) {
            logger.info("异步去获取所有的获取ObjectIdentifier保存到deviceMap里面,,deviceId <====>"+deviceId);
            syncObjectIdentifier(localDevice, remoteDevice);
        }
        logger.info("propertyName================"+propertyName);
        logger.info("最后获得得ObjectIdentifier==========="+deviceMap.get(deviceId).get(propertyName));
        return deviceMap.get(deviceId).get(propertyName);
    }

    private void  syncObjectIdentifier(LocalDevice localDevice,RemoteDevice  remoteDevice) {
        int remoteDeviceId = remoteDevice.getInstanceNumber();
        logger.info("try to read property id of "+remoteDeviceId);

        try {
            List<ObjectIdentifier> oids = ((SequenceOf<ObjectIdentifier>) RequestUtils.sendReadPropertyAllowNull(localDevice, remoteDevice, remoteDevice.getObjectIdentifier(), PropertyIdentifier.objectList)).getValues();



            PropertyReferences references = new PropertyReferences();
            for (ObjectIdentifier objectIdentifier : oids) {
                references.add(objectIdentifier, PropertyIdentifier.presentValue, PropertyIdentifier.objectName);
            }

            logger.info("try to read properties name and present value of  "+remoteDeviceId);
            PropertyValues values =  RequestUtils.readProperties(localDevice, remoteDevice, references, null);

            for (ObjectIdentifier tmpOid : oids) {
                String objectName = values.getString(tmpOid, PropertyIdentifier.objectName);
                logger.info("objectIdentifier:  "+tmpOid +"  <==> property:  "+objectName);
                String pres = values.getString(tmpOid, PropertyIdentifier.presentValue);
                logger.info("presentValue : "+ pres);


                setObjectIdentifier(remoteDeviceId, objectName, tmpOid);
            }
        } catch (BACnetException e) {
            e.printStackTrace();
        }

    }

    private void setObjectIdentifier(Integer deviceId, String propertyName,ObjectIdentifier objectIdentifier) {
        if (!deviceMap.containsKey(deviceId)) {
            HashMap<String, ObjectIdentifier> map = new  HashMap<>();
            deviceMap.put(deviceId,map);
        }
        deviceMap.get(deviceId).put(propertyName,objectIdentifier) ;
    }


    /**
     * 通过bacnet 写 控制器
     * @param bacnetEntity
     * @return
     */
    @Override
    public String writeproperty(BacnetEntity bacnetEntity)
    {
        Map<String,Object> res = new HashMap<>();
         res.put("code","1");
        res.put("result","success");
        LocalDevice localDevice = null;

        try {
            logger.info("初始化 localDevice...............");
            localDevice = getLocalDevice(bacnetEntity.getBroadcastIp(),bacnetEntity.getDeviceId());
            logger.info("1.   localDevice-------------------------" + localDevice);

            RemoteDevice remoteDevice = localDevice.findRemoteDevice(IpNetworkUtils.toAddress(bacnetEntity.getIp(), BacnetConstants.DEFAULT_PORT), bacnetEntity.getDeviceId());
            logger.info("2.  remoteDevice-------------------------"+remoteDevice);

            ObjectIdentifier objectIdentifier = getObjectIdentifier(localDevice,remoteDevice,bacnetEntity.getPropertyName());
            logger.info("3.objectIdentifier-------------------------"+objectIdentifier);
            try {
                //需要变成浮点数 ，要不会报错
                float value =  Float.parseFloat((String) bacnetEntity.getChangeValue()) ;
                WritePropertyRequest writePropertyRequest = new WritePropertyRequest(objectIdentifier, PropertyIdentifier.presentValue,
                        null,new Real(value), null);

                localDevice.send(remoteDevice, writePropertyRequest);

                logger.info("Succeed to writeAsync property "+bacnetEntity.getDeviceId()+":"+bacnetEntity.getPropertyName()+" to "+bacnetEntity.getChangeValue());

            } catch (Exception e) {

                logger.warn("Fail to writeAsync property inner "+bacnetEntity.getDeviceId()+":"+bacnetEntity.getPropertyName()+" to "+bacnetEntity.getChangeValue());
                e.printStackTrace();
                res.put("code","0");
                res.put("result","err");
            } finally {
                Thread.sleep(100);
                localDevice.terminate();
            }
        }catch (Exception e){
            logger.warn("Fail to writeAsync property out  "+bacnetEntity.getDeviceId()+":"+bacnetEntity.getPropertyName()+" to "+bacnetEntity.getChangeValue());
            e.printStackTrace();
            res.put("code","0");
            res.put("result","err");
        } finally {
            if (localDevice != null && localDevice.isInitialized()) {
                localDevice.terminate();
            }
        }

        return JSON.toJSONString(res);
    }


    /**
     * 根据 网关 获取到所有的 设备
     * @param broadcastIp
     * @return
     */

  private LocalDevice getLocalDevice(String broadcastIp,int localDeviceID){
      String  subnetMask = "255.255.255.0";
      try {
          IpNetworkBuilder networkBuilder = new IpNetworkBuilder().subnetMask(subnetMask).broadcastIp(broadcastIp).port(BacnetConstants.DEFAULT_PORT);

          DefaultTransport transport = new DefaultTransport(networkBuilder.build());

       /*   int localDeviceID = 10000 + (int)(Math.random() * 10000);*/
          LocalDevice localDevice = new LocalDevice(localDeviceID, transport);

          localDevice.initialize();

          return localDevice;
      } catch (Exception e) {
          logger.warn("fail to get localDevice..........");
          e.printStackTrace();
      }
        return null;
  }





}
