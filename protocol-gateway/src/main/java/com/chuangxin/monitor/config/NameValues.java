package com.chuangxin.monitor.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class NameValues<T> {

    @JsonProperty("name")
    private String name;


    @JsonProperty("values")
    private Map<String,T>  values = new HashMap<>();

    public NameValues() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, T> getValues() {
        return values;
    }

    public void setValues(Map<String, T> values) {
        this.values = values;
    }
}
