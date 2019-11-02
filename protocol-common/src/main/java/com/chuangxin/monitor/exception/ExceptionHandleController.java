package com.chuangxin.monitor.exception;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import com.chuangxin.monitor.web.BaseController;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.AuthenticationException;
import javax.naming.NoPermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class ExceptionHandleController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandleController.class);
    private static final String NOT_FOUND = "not_found";
    private static final String HANDLER_METHOD_NOT_FOUND = "handler_method_not_found";
    private static final String HTTP_METHOD_NOT_SUPPORTED = "http_method_not_supported";
    private static final String MEDIA_TYPE_NOT_SUPPORTED = "media_type_not_supported";
    private static final String MEDIA_TYPE_NOT_ACCEPTABLE = "media_type_not_acceptable";
    private static final String REQUEST_PARAM_REQUIRED = "request_param_required";
    private static final String REQUEST_PART_REQUIRED = "request_part_required";
    private static final String REQUEST_HEADER_REQUIRED = "request_header_required";
    private static final String HTTP_MESSAGE_NOT_READABLE = "http_message_not_readable";
    private static final String CONVERSION_NOT_SUPPORTED = "conversion_not_supported";
    private static final String METHOD_ARGUMENT_NOT_VALID = "method_argument_not_valid";
    private static final String INTERNAL_SERVER_ERROR = "internal_server_error";
    private static final String FORBIDDEN = "permission_invalid";
    private static final String UNAUTHORIZED = "auth_error";

    public ExceptionHandleController() {
    }

    @ExceptionHandler({NoHandlerFoundException.class})
    @ResponseBody
    public ResponseEntity<ExceptionRespEntity> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        ExceptionRespEntity entity = new ExceptionRespEntity("not_found", "没有找到 [" + ex.getHttpMethod().toUpperCase() + " " + ex.getRequestURL() + "] 相应的处理器.");
        return this.getJSONResp(entity, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({NoObjectFoundException.class})
    @ResponseBody
    public ResponseEntity<ExceptionRespEntity> handleNoObjectFoundException(NoObjectFoundException ex) {
        ExceptionRespEntity entity = new ExceptionRespEntity("not_found", "没有找到:" + ex.getMessage());
        return this.getJSONResp(entity, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public ResponseEntity<ExceptionRespEntity> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        StringBuffer supportedMethods = new StringBuffer();
        if (ex.getSupportedMethods() != null && ex.getSupportedMethods().length > 0) {
            for(int i = 0; i < ex.getSupportedMethods().length; ++i) {
                if (i != 0) {
                    supportedMethods.append(" | ");
                }

                supportedMethods.append(ex.getSupportedMethods()[i]);
            }
        }

        ExceptionRespEntity entity = new ExceptionRespEntity("http_method_not_supported", "不支持的Http方法 [" + ex.getMethod().toUpperCase() + "], 请尝试 [" + supportedMethods.toString() + "].");
        return this.getJSONResp(entity, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler({HttpMediaTypeNotSupportedException.class})
    @ResponseBody
    public ResponseEntity<ExceptionRespEntity> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        ExceptionRespEntity entity = new ExceptionRespEntity("media_type_not_supported", "不支持的Http媒体类型 [" + ex.getContentType().toString() + "].");
        return this.getJSONResp(entity, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    @ResponseBody
    public ResponseEntity<ExceptionRespEntity> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        StringBuffer supportsList = new StringBuffer();
        if (ex.getSupportedMediaTypes() != null && ex.getSupportedMediaTypes().size() > 0) {
            for(int i = 0; i < ex.getSupportedMediaTypes().size(); ++i) {
                if (i != 0) {
                    supportsList.append(" | ");
                }

                supportsList.append(((MediaType)ex.getSupportedMediaTypes().get(i)).toString());
            }
        }

        ExceptionRespEntity entity = new ExceptionRespEntity("media_type_not_acceptable", "不可接受的Http媒体类型, 仅支持 [" + supportsList.toString() + "].");
        return this.getJSONResp(entity, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public ResponseEntity<ExceptionRespEntity> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        ExceptionRespEntity entity = new ExceptionRespEntity("request_param_required", "URL参数 [" + ex.getParameterName() + "] 不能为空.");
        return this.getJSONResp(entity, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ServletRequestBindingException.class})
    @ResponseBody
    public ResponseEntity<ExceptionRespEntity> handleServletRequestBindingException(ServletRequestBindingException ex) {
        ExceptionRespEntity entity = new ExceptionRespEntity("request_header_required", "HEADER参数不能为空.");
        return this.getJSONResp(entity, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MissingServletRequestPartException.class})
    @ResponseBody
    public ResponseEntity<ExceptionRespEntity> handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        ExceptionRespEntity entity = new ExceptionRespEntity("request_part_required", "Request part [" + ex.getRequestPartName() + "] 不能为空.");
        return this.getJSONResp(entity, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseBody
    public ResponseEntity<ExceptionRespEntity> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ExceptionRespEntity entity = new ExceptionRespEntity("http_message_not_readable", ex.getMessage());
        return this.getJSONResp(entity, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConversionNotSupportedException.class})
    @ResponseBody
    public ResponseEntity<ExceptionRespEntity> handleConversionNotSupportedException(ConversionNotSupportedException ex) {
        ExceptionRespEntity entity = new ExceptionRespEntity("conversion_not_supported", "不支持的类型转换, 属性 [" + ex.getPropertyName() + "].");
        return this.getJSONResp(entity, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseEntity<ExceptionRespEntity> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, Object> results = new HashMap();
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if (fieldErrors != null) {
            for(int i = 0; i < fieldErrors.size(); ++i) {
                FieldError fieldError = (FieldError)fieldErrors.get(i);
                String value = fieldError.getRejectedValue() == null ? "null" : fieldError.getRejectedValue().toString();
                String reason = fieldError.getDefaultMessage() == null ? "" : fieldError.getDefaultMessage();
                String errorFieldMessage = "被拒绝的值 [" + value + "], 原因 [" + reason + "].";
                results.put(fieldError.getField(), errorFieldMessage);
            }
        }

        ExceptionRespEntity entity = new ExceptionRespEntity("method_argument_not_valid", "请求数据格式有误.");
        entity.setExt(results);
        return this.getJSONResp(entity, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoPermissionException.class})
    @ResponseBody
    public ResponseEntity<ExceptionRespEntity> handleNoPermissionException(NoPermissionException ex) {
        ExceptionRespEntity entity = new ExceptionRespEntity("permission_invalid", ex.getMessage());
        return this.getJSONResp(entity, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({AuthenticationException.class})
    @ResponseBody
    public ResponseEntity<ExceptionRespEntity> handleAuthenticationException(AuthenticationException ex) {
        ExceptionRespEntity entity = new ExceptionRespEntity("auth_error", ex.getMessage());
        return this.getJSONResp(entity, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({RuntimeException.class, Exception.class, Throwable.class})
    @ResponseBody
    public ResponseEntity<ExceptionRespEntity> handleException(Throwable th) {
        logger.error("internal server error", th);
        ExceptionRespEntity entity = new ExceptionRespEntity("internal_server_error", th.getMessage());
        return this.getJSONResp(entity, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
