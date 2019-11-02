package com.chuangxin.monitor.web;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ResponseMessage<T> implements Serializable {
    private static final long serialVersionUID = 8992436576262574064L;
    protected String message;
    protected T result;
    protected int status;
    private Long timestamp;
    private transient Map<Class<?>, Set<String>> includes;
    private transient Map<Class<?>, Set<String>> excludes;

    public String getMessage() {
        return this.message;
    }

    public int getStatus() {
        return this.status;
    }

    public T getResult() {
        return this.result;
    }

    public Long getTimestamp() {
        return this.timestamp;
    }

    public static <T> ResponseMessage<T> error(String message) {
        return error(500, message);
    }

    public static <T> ResponseMessage<T> error(int status, String message) {
        ResponseMessage<T> msg = new ResponseMessage();
        msg.message = message;
        msg.status(status);
        return msg.putTimeStamp();
    }

    public static <T> ResponseMessage<T> error(T result) {
        return (new ResponseMessage()).result(result).putTimeStamp().status(500);
    }

    public static <T> ResponseMessage<T> ok() {
        return (ResponseMessage<T>) ok((Object)null);
    }

    public ResponseMessage<T> putTimeStamp() {
        this.timestamp = System.currentTimeMillis();
        return this;
    }

    public static <T> ResponseMessage<T> ok(T result) {
        return (new ResponseMessage()).result(result).putTimeStamp().status(200);
    }

    public ResponseMessage<T> result(T result) {
        this.result = result;
        return this;
    }

    public ResponseMessage() {
    }

    public ResponseMessage<T> include(Class<?> type, String... fields) {
        return this.include(type, (Collection)Arrays.asList(fields));
    }

    public ResponseMessage<T> include(Class<?> type, Collection<String> fields) {
        if (this.includes == null) {
            this.includes = new HashMap();
        }

        if (fields != null && !fields.isEmpty()) {
            Iterator var3 = fields.iterator();

            while(var3.hasNext()) {
                String field = (String)var3.next();
                if (field.contains(".")) {
                    String[] tmp = field.split("[.]", 2);

                    try {
                        Field field1 = type.getDeclaredField(tmp[0]);
                        if (field1 != null) {
                            this.include(field1.getType(), tmp[1]);
                        }
                    } catch (Throwable var7) {
                    }
                } else {
                    this.getStringListFromMap(this.includes, type).add(field);
                }
            }

            return this;
        } else {
            return this;
        }
    }

    public ResponseMessage<T> exclude(Class type, Collection<String> fields) {
        if (this.excludes == null) {
            this.excludes = new HashMap();
        }

        if (fields != null && !fields.isEmpty()) {
            Iterator var3 = fields.iterator();

            while(var3.hasNext()) {
                String field = (String)var3.next();
                if (field.contains(".")) {
                    String[] tmp = field.split("[.]", 2);

                    try {
                        Field field1 = type.getDeclaredField(tmp[0]);
                        if (field1 != null) {
                            this.exclude(field1.getType(), tmp[1]);
                        }
                    } catch (Throwable var7) {
                    }
                } else {
                    this.getStringListFromMap(this.excludes, type).add(field);
                }
            }

            return this;
        } else {
            return this;
        }
    }

    public ResponseMessage<T> exclude(Collection<String> fields) {
        if (this.excludes == null) {
            this.excludes = new HashMap();
        }

        if (fields != null && !fields.isEmpty()) {
            if (this.getResult() != null) {
                Class type = this.getResult().getClass();
                this.exclude(type, fields);
                return this;
            } else {
                return this;
            }
        } else {
            return this;
        }
    }

    public ResponseMessage<T> include(Collection<String> fields) {
        if (this.includes == null) {
            this.includes = new HashMap();
        }

        if (fields != null && !fields.isEmpty()) {
            if (this.getResult() != null) {
                Class type = this.getResult().getClass();
                this.include(type, fields);
                return this;
            } else {
                return this;
            }
        } else {
            return this;
        }
    }

    public ResponseMessage<T> exclude(Class type, String... fields) {
        return this.exclude(type, (Collection)Arrays.asList(fields));
    }

    public ResponseMessage<T> exclude(String... fields) {
        return this.exclude((Collection)Arrays.asList(fields));
    }

    public ResponseMessage<T> include(String... fields) {
        return this.include((Collection)Arrays.asList(fields));
    }

    protected Set<String> getStringListFromMap(Map<Class<?>, Set<String>> map, Class type) {
        return null;
    }

    public String toString() {
        return JSON.toJSONStringWithDateFormat(this, "yyyy-MM-dd HH:mm:ss", new SerializerFeature[0]);
    }

    public ResponseMessage<T> status(int status) {
        this.status = status;
        return this;
    }

    public Map<Class<?>, Set<String>> getExcludes() {
        return this.excludes;
    }

    public Map<Class<?>, Set<String>> getIncludes() {
        return this.includes;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}

