package com.chuangxin.monitor.web;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import java.util.HashMap;
import java.util.Map;

public class UserContextHolder {
    private static final ThreadLocal<Map> localThreadHolder = new ThreadLocal();

    public UserContextHolder() {
    }

    public static Map getRequestParam() {
        return (Map)localThreadHolder.get();
    }

    public static void setRequestParam(Map params) {
        localThreadHolder.set(params);
    }

    public static Object get(String key) {
        Map contextParams = getRequestParam();
        Object result = null;
        if (contextParams != null && key != null) {
            result = contextParams.get(key);
        }

        return result;
    }

    public static void set(String key, Object value) {
        Map contextParams = getRequestParam();
        if (contextParams == null) {
            contextParams = new HashMap();
            setRequestParam((Map)contextParams);
        }

        if (key != null && value != null) {
            ((Map)contextParams).put(key, value);
        }

    }

    public static String getUserID() {
        Object value = get("currentUserId");
        return returnObjectValue(value);
    }

    public static String getTenantID() {
        Object value = get("currentTenantId");
        return returnObjectValue(value);
    }

    public static String getUsername() {
        Object value = get("currentUserName");
        return returnObjectValue(value);
    }

    public static String getName() {
        Object value = get("currentUser");
        return returnObjectValue(value);
    }

    public static String getToken() {
        Object value = get("currentUserToken");
        return returnObjectValue(value);
    }

    public static void setToken(String token) {
        set("currentUserToken", token);
    }

    public static void setName(String name) {
        set("currentUser", name);
    }

    public static void setUserID(String userID) {
        set("currentUserId", userID);
    }

    public static void setTenantID(String tenantID) {
        set("currentTenantId", tenantID);
    }

    public static void setUsername(String username) {
        set("currentUserName", username);
    }

    private static String returnObjectValue(Object value) {
        return value == null ? null : value.toString();
    }

    public static void remove() {
        localThreadHolder.remove();
    }
}
