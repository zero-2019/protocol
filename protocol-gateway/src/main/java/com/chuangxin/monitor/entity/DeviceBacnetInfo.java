package com.chuangxin.monitor.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DeviceBacnetInfo implements RowMapper<DeviceBacnetInfo> {

    private int id;

    private String datcentId;

    private String broadcastIp;

    private String broeadcastPort;

    private String deviceIp;

    private String deviceId;

    private String propertyName;

    private String status;

    private String deviceNo;

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatcentId() {
        return datcentId;
    }

    public void setDatcentId(String datcentId) {
        this.datcentId = datcentId;
    }

    public String getBroadcastIp() {
        return broadcastIp;
    }

    public void setBroadcastIp(String broadcastIp) {
        this.broadcastIp = broadcastIp;
    }

    public String getBroeadcastPort() {
        return broeadcastPort;
    }

    public void setBroeadcastPort(String broeadcastPort) {
        this.broeadcastPort = broeadcastPort;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DeviceBacnetInfo() {
    }

    public DeviceBacnetInfo(int id, String datcentId, String broadcastIp, String broeadcastPort, String deviceIp, String deviceId, String propertyName, String status, String deviceNo) {
        this.id = id;
        this.datcentId = datcentId;
        this.broadcastIp = broadcastIp;
        this.broeadcastPort = broeadcastPort;
        this.deviceIp = deviceIp;
        this.deviceId = deviceId;
        this.propertyName = propertyName;
        this.status = status;
        this.deviceNo = deviceNo;
    }

    @Override
    public DeviceBacnetInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        DeviceBacnetInfo deviceBacnetInfo = new DeviceBacnetInfo();
        deviceBacnetInfo.setId(rs.getInt("id"));
        deviceBacnetInfo.setDatcentId(rs.getString("datcent_id"));
        deviceBacnetInfo.setBroadcastIp(rs.getString("broadcast_ip"));
        deviceBacnetInfo.setBroeadcastPort(rs.getString("broeadcast_port"));
        deviceBacnetInfo.setDeviceIp(rs.getString("device_ip"));
        deviceBacnetInfo.setDeviceId(rs.getString("device_id"));
        deviceBacnetInfo.setPropertyName(rs.getString("property_name"));
        deviceBacnetInfo.setStatus(rs.getString("status"));
        deviceBacnetInfo.setDeviceNo(rs.getString("device_no"));
        return deviceBacnetInfo;
    }

    @Override
    public String toString() {
        return "DeviceBacnetInfo{" +
                "id=" + id +
                ", datcentId='" + datcentId + '\'' +
                ", broadcastIp='" + broadcastIp + '\'' +
                ", broeadcastPort='" + broeadcastPort + '\'' +
                ", deviceIp='" + deviceIp + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", status='" + status + '\'' +
                ", deviceNo='" + deviceNo + '\'' +
                '}';
    }
}
