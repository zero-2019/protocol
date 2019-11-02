package com.chuangxin.monitor.entity;

public class ModbusEntity {

    private String host ;   //主站master 地址

    private int port ;   //mastert 端口号

    private int  slaveId;   //从站 id

    private int offset ;  //偏移量

    private  short writeValue;   //修改值

    private int dataType;  //数字类型

    public ModbusEntity() {
    }

    public ModbusEntity(String host, int port, int slaveId, int offset, short writeValue, int dataType) {
        this.host = host;
        this.port = port;
        this.slaveId = slaveId;
        this.offset = offset;
        this.writeValue = writeValue;
        this.dataType = dataType;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSlaveId() {
        return slaveId;
    }

    public void setSlaveId(int slaveId) {
        this.slaveId = slaveId;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public short getWriteValue() {
        return writeValue;
    }

    public void setWriteValue(short writeValue) {
        this.writeValue = writeValue;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return "ModbusEntity{" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", slaveId='" + slaveId + '\'' +
                ", offset=" + offset +
                ", writeValue=" + writeValue +
                ", dataType=" + dataType +
                '}';
    }
}
