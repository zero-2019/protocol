package com.chuangxin.monitor.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class AlgoOutput {

    @JsonProperty("to_control")
    private  boolean toControl;

    @JsonProperty("datetime")
    private String datetime;

    @JsonProperty("send_time")
    private String sendTime;

    @JsonProperty("data_interval")
    private  DataInterval dataInterval;

    @JsonProperty("control")
    private WaterSystem<Integer>  control;

    public AlgoOutput() {
    }

    public boolean isToControl() {
        return toControl;
    }

    public void setToControl(boolean toControl) {
        this.toControl = toControl;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public DataInterval getDataInterval() {
        return dataInterval;
    }

    public void setDataInterval(DataInterval dataInterval) {
        this.dataInterval = dataInterval;
    }

    public WaterSystem<Integer> getControl() {
        return control;
    }

    public void setControl(WaterSystem<Integer> control) {
        this.control = control;
    }
}

enum  DataInterval{
    NONE,

    NORMAL,

    INTENSIVE;

}