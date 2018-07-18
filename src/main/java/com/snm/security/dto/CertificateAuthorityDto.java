package com.snm.security.dto;

import java.io.Serializable;
import java.time.Instant;

public class CertificateAuthorityDto implements Serializable {

    private Long id;
    private String serialNumber;
    private byte[] data;
    private Instant validityDateFrom;
    private Instant validityDateUpTo;
    private Long certifyingCenterId;

    public CertificateAuthorityDto() {
    }

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

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Instant getValidityDateFrom() {
        return validityDateFrom;
    }

    public void setValidityDateFrom(Instant validityDateFrom) {
        this.validityDateFrom = validityDateFrom;
    }

    public Instant getValidityDateUpTo() {
        return validityDateUpTo;
    }

    public void setValidityDateUpTo(Instant validityDateUpTo) {
        this.validityDateUpTo = validityDateUpTo;
    }

    public Long getCertifyingCenterId() {
        return certifyingCenterId;
    }

    public void setCertifyingCenterId(Long certifyingCenterId) {
        this.certifyingCenterId = certifyingCenterId;
    }
}
