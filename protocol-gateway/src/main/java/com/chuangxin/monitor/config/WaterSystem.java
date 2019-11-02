package com.chuangxin.monitor.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class WaterSystem<T> {

    @JsonProperty("chiller")
    private List<NameValues<T>> chiller = new ArrayList();

    @JsonProperty("chwCircuit")
    private  ChwCircuit<T>  chwCircuit;


    @JsonProperty("cowCircuit")
    private List<NameValues<T>> cowCircuit = new ArrayList();

    public WaterSystem() {
    }

    public List<NameValues<T>> getChiller() {
        return chiller;
    }

    public void setChiller(List<NameValues<T>> chiller) {
        this.chiller = chiller;
    }

    public ChwCircuit<T> getChwCircuit() {
        return chwCircuit;
    }

    public void setChwCircuit(ChwCircuit<T> chwCircuit) {
        this.chwCircuit = chwCircuit;
    }

    public List<NameValues<T>> getCowCircuit() {
        return cowCircuit;
    }

    public void setCowCircuit(List<NameValues<T>> cowCircuit) {
        this.cowCircuit = cowCircuit;
    }
}
