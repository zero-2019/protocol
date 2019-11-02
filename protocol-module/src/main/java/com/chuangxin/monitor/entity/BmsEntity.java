package com.chuangxin.monitor.entity;

public class BmsEntity {

    private String host;   //socket 连接 bms的  ip

    private String port;    //socket 连接 bms的 端口号

    private String datcentId ;    //数据中心  BJ2

    private boolean sign = true ;    //


    public BmsEntity() {
    }

    public BmsEntity(String host, String port, String datcentId, boolean sign) {
        this.host = host;
        this.port = port;
        this.datcentId = datcentId;
        this.sign = sign;
    }

    public boolean isSign() {
        return sign;
    }

    public void setSign(boolean sign) {
        this.sign = sign;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatcentId() {
        return datcentId;
    }

    public void setDatcentId(String datcentId) {
        this.datcentId = datcentId;
    }

    @Override
    public String toString() {
        return "BmsEntity{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", datcentId='" + datcentId + '\'' +
                ", sign=" + sign +
                '}';
    }
}


