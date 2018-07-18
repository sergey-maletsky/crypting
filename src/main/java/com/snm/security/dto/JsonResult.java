package com.snm.security.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class JsonResult<T> implements Serializable {

    private T result;
    private String message;
    private Integer errorCode;
    private Map<String, Object> validatorErrors = new HashMap<>();

    public JsonResult() {}

    public JsonResult(ErrorCode errorCode) {

        this.errorCode = errorCode.getCode();
    }

    public JsonResult(ErrorCode errorCode, String message) {

        this.message = message;
        this.errorCode = errorCode.getCode();
    }

    public JsonResult(ErrorCode errorCode, T result) {

        this.result = result;
        this.errorCode = errorCode.getCode();
    }

    public T getResult() {

        return result;
    }

    public void setResult(T result) {

        this.result = result;
    }

    public String getMessage() {

        return message;
    }

    public void setMessage(String message) {

        this.message = message;
    }

    public Integer getErrorCode() {

        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {

        this.errorCode = errorCode;
    }

    public Map<String, Object> getValidatorErrors() {

        return validatorErrors;
    }

    public void setValidatorErrors(Map<String, Object> validatorErrors) {

        this.validatorErrors = validatorErrors;
    }

    public enum ErrorCode {
        NO_ERROR(0),
        VALIDATION_ERROR(3),
        SYSTEM_ERROR(4),
        ALREADY_EXIST(5),
        ENTITY_NOT_FOUND(6),
        AUTH_ERROR(7);
        Integer code;

        ErrorCode(Integer code) {

            this.code = code;
        }

        @Override
        public String toString() {

            return code.toString();
        }

        public Integer getCode() {

            return code;
        }

        public void setCode(Integer code) {

            this.code = code;
        }
    }
}
