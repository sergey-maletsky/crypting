package com.snm.security.dto;

import java.time.Instant;

public class CertificateRevocationListDto {

    private Long id;
    private String serialNumber;
    private Instant revocationDate;
    private Long revocationListMetaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Instant getRevocationDate() {
        return revocationDate;
    }

    public void setRevocationDate(Instant revocationDate) {
        this.revocationDate = revocationDate;
    }

    public Long getRevocationListMetaId() {
        return revocationListMetaId;
    }

    public void setRevocationListMetaId(Long revocationListMetaId) {
        this.revocationListMetaId = revocationListMetaId;
    }
}
