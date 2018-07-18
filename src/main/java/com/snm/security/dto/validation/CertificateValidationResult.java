package com.snm.security.dto.validation;

import com.snm.security.dto.CertificateDto;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CertificateValidationResult implements Serializable {

    @NotNull
    private final String certificateData;
    private CertificateDto certificate;
    private List<String> errors = new ArrayList<>();

    public CertificateValidationResult(@NotNull String certificateData) {
        this.certificateData = certificateData;
    }

    public CertificateDto getCertificate() {
        return certificate;
    }

    public void setCertificate(CertificateDto certificate) {
        this.certificate = certificate;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    @NotNull
    public String getCertificateData() {
        return certificateData;
    }

}
