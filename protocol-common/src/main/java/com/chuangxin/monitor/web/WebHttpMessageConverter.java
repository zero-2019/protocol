package com.chuangxin.monitor.web;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.lang.reflect.Type;
import java.util.concurrent.atomic.AtomicReference;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class WebHttpMessageConverter extends MappingJackson2HttpMessageConverter {
    public WebHttpMessageConverter() {
    }

    protected boolean supports(Class<?> clazz) {
        return super.supports(clazz);
    }

    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        if (!this.canWrite(mediaType)) {
            return false;
        } else {
            AtomicReference<Throwable> causeRef = new AtomicReference();
            if (this.objectMapper.canSerialize(clazz, causeRef)) {
                return true;
            } else {
                this.logWarningIfNecessary(clazz, (Throwable)causeRef.get());
                return false;
            }
        }
    }
}
