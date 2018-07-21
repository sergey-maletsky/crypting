package com.snm.security.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_certificate")
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserCertificate {

    @Id
    @SequenceGenerator(name = "user_certificate_sequence", sequenceName = "user_certificate_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_certificate_sequence")
    private Long id;

    @Column(nullable = false)
    private String serialNumber;

    @Column(nullable = false)
    private byte[] data;

    @Column(nullable = false)
    private Timestamp validityDateFrom;

    @Column(nullable = false)
    private Timestamp validityDateTo;

    @Column
    private String organisationName;

    @Column
    private String fullName;

    @Column
    private boolean revoked;

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

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public CertifyingCenter getCertifyingCenter() {
        return certifyingCenter;
    }

    public void setCertifyingCenter(CertifyingCenter certifyingCenter) {
        this.certifyingCenter = certifyingCenter;
    }
}
