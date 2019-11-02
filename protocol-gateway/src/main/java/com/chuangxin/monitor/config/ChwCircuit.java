package com.chuangxin.monitor.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class ChwCircuit<T> {



        @JsonProperty("firstChwCircuit")
        private List<NameValues<T>> firstChwCircuit = new ArrayList();

        @JsonProperty("secondChwCircuit")
        private  List<NameValues<T>> secondChwCircuit = new ArrayList();

        public ChwCircuit() {
        }

        public List<NameValues<T>> getFirstChwCircuit() {
            return firstChwCircuit;
        }

        public void setFirstChwCircuit(List<NameValues<T>> firstChwCircuit) {
            this.firstChwCircuit = firstChwCircuit;
        }

        public List<NameValues<T>> getSecondChwCircuit() {
            return secondChwCircuit;
        }

        public void setSecondChwCircuit(List<NameValues<T>> secondChwCircuit) {
            this.secondChwCircuit = secondChwCircuit;
        }

}
