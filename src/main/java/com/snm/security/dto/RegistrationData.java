package com.snm.security.dto;

import java.io.Serializable;

public class RegistrationData implements Serializable {

    private String randomValue;
    private String signature; // base64

    public RegistrationData() {
    }

    public RegistrationData(String randomValue, String signature) {
        this.randomValue = randomValue;
        this.signature = signature;
    }

    public String getRandomValue() {
        return randomValue;
    }

    public void setRandomValue(String randomValue) {
        this.randomValue = randomValue;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
