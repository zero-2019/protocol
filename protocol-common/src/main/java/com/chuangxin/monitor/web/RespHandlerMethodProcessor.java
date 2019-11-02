package com.chuangxin.monitor.web;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.alibaba.fastjson.JSON;
import java.net.URI;
import java.util.List;
import com.chuangxin.monitor.exception.ExceptionRespEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class RespHandlerMethodProcessor implements ResponseBodyAdvice {
    private static final Logger logger = LoggerFactory.getLogger(RespHandlerMethodProcessor.class);

    public RespHandlerMethodProcessor() {
    }

    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (!request.getURI().getPath().startsWith("/v2") && !request.getURI().getPath().startsWith("/swagger")) {
            List<String> feginHead = request.getHeaders().get("feign-req");
            if (feginHead != null && "1".equals(feginHead.get(0))) {
                return body;
            } else {
                if (body == null) {
                }

                URI host = request.getURI();
                String url = host.getPath();
                String address = WebUtils.getRealRemoteHostIp(((ServletServerHttpRequest)request).getServletRequest());
                String userID = UserContextHolder.getUserID();
                int status = ((ServletServerHttpResponse)response).getServletResponse().getStatus();
                logger.info(address + " " + userID + " " + host + " " + status);
                if (returnType.getParameterType().isAssignableFrom(String.class)) {
                    return JSON.toJSONString(ResponseMessage.ok(body));
                } else {
                    return !(body instanceof ExceptionRespEntity) && !returnType.getParameterType().isAssignableFrom(ExceptionRespEntity.class) ? ResponseMessage.ok(body) : ResponseMessage.error(((ExceptionRespEntity)body).getMsg()).status(((ServletServerHttpResponse)response).getServletResponse().getStatus());
                }
            }
        } else {
            return body;
        }
    }

    public static void main(String[] args) {
        logger.info("192.168.1.18 admin www.baidu.com 500");
        System.out.println("执行");
    }
}
