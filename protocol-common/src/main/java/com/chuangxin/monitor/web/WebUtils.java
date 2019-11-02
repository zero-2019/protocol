package com.chuangxin.monitor.web;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//


import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.zip.GZIPOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebUtils {
    private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);
    public static final long ONE_YEAR_SECONDS = 31536000L;
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public WebUtils() {
    }

    public static void setExpiresHeader(HttpServletResponse response, long expiresSeconds) {
        response.setDateHeader("Expires", System.currentTimeMillis() + expiresSeconds * 1000L);
        response.setHeader("Cache-Control", "max-age=" + expiresSeconds);
    }

    public static void setNoCacheHeader(HttpServletResponse response) {
        response.setDateHeader("Expires", 0L);
        response.setHeader("Cache-Control", "no-cache");
    }

    public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
        response.setDateHeader("Last-Modified", lastModifiedDate);
    }

    public static void setEtag(HttpServletResponse response, String etag) {
        response.setHeader("ETag", etag);
    }

    public static boolean checkIfModifiedSince(HttpServletRequest request, HttpServletResponse response, long lastModified) {
        long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        if (ifModifiedSince != -1L && lastModified < ifModifiedSince + 1000L) {
            response.setStatus(304);
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkIfNoneMatchEtag(HttpServletRequest request, HttpServletResponse response, String etag) {
        String headerValue = request.getHeader("If-None-Match");
        if (headerValue != null) {
            boolean conditionSatisfied = false;
            if (!"*".equals(headerValue)) {
                StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");

                while(!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
                    String currentToken = commaTokenizer.nextToken();
                    if (currentToken.trim().equals(etag)) {
                        conditionSatisfied = true;
                    }
                }
            } else {
                conditionSatisfied = true;
            }

            if (conditionSatisfied) {
                response.setStatus(304);
                response.setHeader("ETag", etag);
                return false;
            }
        }

        return true;
    }

    public static boolean checkAccetptGzip(HttpServletRequest request) {
        String acceptEncoding = request.getHeader("Accept-Encoding");
        return StringUtils.contains(acceptEncoding, "gzip");
    }

    public static OutputStream buildGzipOutputStream(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Encoding", "gzip");
        response.setHeader("Vary", "Accept-Encoding");
        return new GZIPOutputStream(response.getOutputStream());
    }

    public static void setDownloadableHeader(HttpServletResponse response, String fileName) {
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
    }

    public static Map<String, String> getParametersStartingWith(HttpServletRequest request, String prefix) {
        return null;
    }

    public static Map<String, String> getParameterMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap();
        Map<String, String[]> requestParams = request.getParameterMap();
        Iterator iterator = requestParams.keySet().iterator();

        while(iterator.hasNext()) {
            String key = (String)iterator.next();
            String[] values = (String[])requestParams.get(key);
            if (values != null && values.length > 0) {
                params.put(key, values[0]);
            }
        }

        return params;
    }

    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equalsIgnoreCase(request.getHeader("X-Requested-With"));
    }

    public static void restFailed(HttpServletResponse response, int code, String message) {
        respondJson(response, 400, code, message);
    }

    public static void ajaxSucceed(HttpServletResponse response, int code, String message) {
        respondJson(response, 200, code, message);
    }

    public static void ajaxFailed(HttpServletResponse response, int respondStatus, int code, String message) {
        respondJson(response, respondStatus, code, message);
    }

    public static void ajaxFailed(HttpServletResponse response, int respondStatus, String message) {
        respondJson(response, respondStatus, respondStatus, message);
    }

    private static void respondJson(HttpServletResponse response, int respondStatus, int code, String message) {
        ResponseMessage respmessage = new ResponseMessage();
        respmessage.putTimeStamp();
        respmessage.setMessage(message);
        respmessage.setStatus(code);
        response.setStatus(respondStatus);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;

        try {
            out = response.getWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(Include.NON_NULL);
            String json = objectMapper.writeValueAsString(respmessage);
            out.write(json);
        } catch (IOException var11) {
            logger.error(var11.getMessage(), var11);
        } finally {
            if (out != null) {
                out.close();
            }

        }

    }

    public static void respondJson(HttpServletResponse response, int respondStatus, Object object) {
        response.setStatus(respondStatus);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;

        try {
            out = response.getWriter();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.setSerializationInclusion(Include.NON_NULL);
            String json = objectMapper.writeValueAsString(object);
            out.write(json);
        } catch (IOException var9) {
            logger.error(var9.getMessage(), var9);
        } finally {
            if (out != null) {
                out.close();
            }

        }

    }

    public static String getRealRemoteHostIp(HttpServletRequest request) {
        String remoteIp = null;
        String forwardIp = request.getHeader("ZUUL_XFORWARD_REMOTEHOST");
        String ip = request.getRemoteAddr();
        remoteIp = forwardIp == null ? request.getRemoteAddr() : forwardIp;
        return remoteIp;
    }
}
