package com.snm.security.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "certificate_authority")
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CertificateAuthority {

    @Id
    @SequenceGenerator(name = "certificate_authority_sequence", sequenceName = "certificate_authority_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certificate_authority_sequence")
    private Long id;

    @Column(nullable = false)
    private String serialNumber;

    @Column(nullable = false)
    private byte[] data;

    @Column(nullable = false)
    private Timestamp validityDateFrom;

    @Column(nullable = false)
    private Timestamp validityDateTo;

    @ManyToOne
    @JoinColumn(name = "certifying_id")
    private CertifyingCenter certifyingCenter;

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

    public Timestamp getValidityDateFrom() {
        return validityDateFrom;
    }

    public void setValidityDateFrom(Timestamp validityDateFrom) {
        this.validityDateFrom = validityDateFrom;
    }

    public Timestamp getValidityDateTo() {
        return validityDateTo;
    }

    public void setValidityDateTo(Timestamp validityDateTo) {
        this.validityDateTo = validityDateTo;
    }

    public CertifyingCenter getCertifyingCenter() {
        return certifyingCenter;
    }

    public void setCertifyingCenter(CertifyingCenter certifyingCenter) {
        this.certifyingCenter = certifyingCenter;
    }
}
