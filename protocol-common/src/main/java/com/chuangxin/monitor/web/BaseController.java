package com.chuangxin.monitor.web;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public BaseController() {
    }

    public ResponseEntity<String> getSucceedStringResp() {
        return this.getStringResp("Successfully.", HttpStatus.OK);
    }

    public ResponseEntity<String> getStringResp(String body, HttpStatus statusCode) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "text/plain;charset=utf-8");
        return new ResponseEntity(body, responseHeaders, statusCode);
    }

    public <T> ResponseEntity<T> getJSONResp(T body, HttpStatus statusCode) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Content-Type", "application/json;charset=utf-8");
        return new ResponseEntity(body, responseHeaders, statusCode);
    }

    protected final boolean isPost(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod());
    }

    protected boolean isAjaxRequest(HttpServletRequest request) {
        String xmlRequest = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equalsIgnoreCase(xmlRequest);
    }

    protected void handleErr(Exception ex, HttpServletRequest request) {
        this.handleErr(ex.getMessage(), ex, request);
    }

    protected final void handleErr(String message, Exception ex, HttpServletRequest request) {
        try {
            try {
                request.setAttribute("javax.servlet.error.exception", ex);
                request.setAttribute("ERROR_MSG", message);
                this.logger.error(message, ex);
            } catch (Exception var8) {
            }

        } finally {
            ;
        }
    }
}
