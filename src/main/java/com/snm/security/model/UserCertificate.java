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

    @Column(name = "validity_date_from", nullable = false)
    private Timestamp validityDateFrom;

    @Column(name = "validity_date_to", nullable = false)
    private Timestamp validityDateUpTo;

    @Column
    private String organisationName;

    @Column
    private String fullName;

    @Column
    private boolean revoked;

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

    public Timestamp getValidityDateUpTo() {
        return validityDateUpTo;
    }

    public void setValidityDateUpTo(Timestamp validityDateUpTo) {
        this.validityDateUpTo = validityDateUpTo;
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
}
