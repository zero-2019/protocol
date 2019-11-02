package com.chuangxin.monitor.component;


import com.alibaba.fastjson.JSON;
import com.chuangxin.monitor.entity.BacnetEntity;
import com.chuangxin.monitor.entity.BmsEntity;
import com.chuangxin.monitor.entity.ModbusEntity;
import com.chuangxin.monitor.service.ProtocolService;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HystrixClientFallbackFactory  implements FallbackFactory<ProtocolService> {


    @Override
    public ProtocolService create(Throwable cause) {
        return new ProtocolService() {
            Map<String,Object> map = new HashMap<>();
            @Override
            public String writeByBacnet(BacnetEntity bacnetEntity) {
                map.put("status",500);
                map.put("message","feign调用失败");
                return JSON.toJSONString(map);
            }

            @Override
            public String readByBacnet(BacnetEntity bacnetEntity) {
                map.put("status",500);
                map.put("message","feign调用失败");
                return JSON.toJSONString(map);
            }

            @Override
            public String writeBymodbus(ModbusEntity modbusEntity) {
                return null;
            }

            @Override
            public String readBymodbus(ModbusEntity modbusEntity) {
                return null;
            }

            @Override
            public String writeBybms(BmsEntity bmsEntity) {
                map.put("status",500);
                map.put("message","feign调用失败");
                return JSON.toJSONString(map);
            }

            @Override
            public String readBybms(BmsEntity bmsEntity) {
                map.put("status",500);
                map.put("message","feign调用失败");
                return JSON.toJSONString(map);
            }

            @Override
            public String updateGatherSpotBydatcetnId(BmsEntity bmsEntity) {
                map.put("status",500);
                map.put("message","feign调用失败");
                return JSON.toJSONString(map);
            }

        };
    }
}
