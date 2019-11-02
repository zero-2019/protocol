package com.chuangxin.monitor.service.impl;

import com.chuangxin.monitor.entity.ModbusEntity;
import com.chuangxin.monitor.service.ModbusService;
import com.chuangxin.monitor.utils.Modbus4jReadTcpUtils;
import com.chuangxin.monitor.utils.Modbus4jWriteTcpUtils;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ModbusServiceImpl implements ModbusService {


    private static final Logger logger = LoggerFactory.getLogger(BacnetServiceImpl.class);
    @Override
    public String writeproperty(ModbusEntity modbusEntity) {
        String  res = "1";
        try {
            if (StringUtils.isNotBlank(modbusEntity.getHost())){
                Modbus4jWriteTcpUtils.LOCALHOST = modbusEntity.getHost();
            }
            if (modbusEntity.getPort() != 0){
                Modbus4jWriteTcpUtils.PORT = modbusEntity.getPort();
            }


            Modbus4jWriteTcpUtils.writeHoldingRegister(modbusEntity.getSlaveId(), modbusEntity.getOffset(), modbusEntity.getWriteValue(),DataType.EIGHT_BYTE_FLOAT);
            logger.info("success wirte by modbus set slaveID:"+modbusEntity.getSlaveId()+"<==========>offset:"+ modbusEntity.getOffset()+"<========>writeValue:"+modbusEntity.getWriteValue());
        } catch (ModbusTransportException e) {
            e.printStackTrace();
            res = e.getMessage();
            logger.warn("ERROR write "+e.getMessage());
        } catch (ModbusInitException e) {
            e.printStackTrace();
            res = e.getLocalizedMessage();
            logger.warn("ERROR write "+e.getMessage());
        } catch (ErrorResponseException e) {
            e.printStackTrace();
            res = e.getMessage();
            logger.warn("ERROR write "+e.getMessage());
        }

        return res;
    }

    @Override
    public int readproperty(ModbusEntity modbusEntity) {
        int res = -1;


        try {
            if (StringUtils.isNotBlank(modbusEntity.getHost())){
                Modbus4jWriteTcpUtils.LOCALHOST = modbusEntity.getHost();
            }
            if (modbusEntity.getPort() != 0){
                Modbus4jWriteTcpUtils.PORT = modbusEntity.getPort();
            }
            Number number = Modbus4jReadTcpUtils.readHoldingRegister(modbusEntity.getSlaveId(), modbusEntity.getOffset(), DataType.EIGHT_BYTE_FLOAT);
            res =  number.intValue();
            logger.info("success read by modbus set slaveID:"+modbusEntity.getSlaveId()+"<==========>offset:"+ modbusEntity.getOffset()+"<========>value:" + res);
        } catch (ModbusTransportException e) {
            e.printStackTrace();
            logger.warn("ERROR write "+e.getMessage());
        } catch (ErrorResponseException e) {
            e.printStackTrace();
            logger.warn("ERROR write "+e.getMessage());
        } catch (ModbusInitException e) {
            e.printStackTrace();
            logger.warn("ERROR write "+e.getMessage());
        }


        return res;
    }


}
