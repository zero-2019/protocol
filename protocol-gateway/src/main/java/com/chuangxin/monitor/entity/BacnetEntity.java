package com.chuangxin.monitor.entity;

public class BacnetEntity<T>{

    private String ip;

    private Integer deviceId;

    private String propertyName;

    private String broadcastIp;

    private  T  changeValue;

    public BacnetEntity() {
    }

    public BacnetEntity(String ip, Integer deviceId, String propertyName, String broadcastIp, T changeValue) {
        this.ip = ip;
        this.deviceId = deviceId;
        this.propertyName = propertyName;
        this.broadcastIp = broadcastIp;
        this.changeValue = changeValue;
    }

    public T getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(T changeValue) {
        this.changeValue = changeValue;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getBroadcastIp() {
        return broadcastIp;
    }

    public void setBroadcastIp(String broadcastIp) {
        this.broadcastIp = broadcastIp;
    }

    @Override
    public String toString() {
        return "BacnetEntity{" +
                "ip='" + ip + '\'' +
                ", deviceId=" + deviceId +
                ", propertyName='" + propertyName + '\'' +
                ", broadcastIp='" + broadcastIp + '\'' +
                ", changeValue=" + changeValue +
                '}';
    }
}
