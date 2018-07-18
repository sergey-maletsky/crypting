package com.snm.security.dto;

import java.io.Serializable;

public class AuthDto implements Serializable {

    private String uuid;
    private String data;
    private Boolean isReg;

    public AuthDto() {
    }

    public AuthDto(String uuid, String data) {
        this.uuid = uuid;
        this.data = data;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Boolean getIsReg() {
        return isReg;
    }

    public void setIsReg(Boolean registration) {
        isReg = registration;
    }
}
