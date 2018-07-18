package com.snm.security.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "revocation_list")
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRevocationList {

    @Id
    @SequenceGenerator(name = "revocation_list_sequence", sequenceName = "revocation_list_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "revocation_list_sequence")
    private Long id;

    @Column(nullable = false)
    private String serialNumber;

    @Column(nullable = false)
    private Timestamp revocationDate;

    @ManyToOne
    @JoinColumn(name = "crl_meta_id")
    private CertificateRevocationListMeta revocationListMeta;

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

    public Timestamp getRevocationDate() {
        return revocationDate;
    }

    public void setRevocationDate(Timestamp revocationDate) {
        this.revocationDate = revocationDate;
    }

    public CertificateRevocationListMeta getRevocationListMeta() {
        return revocationListMeta;
    }

    public void setRevocationListMeta(CertificateRevocationListMeta revocationListMeta) {
        this.revocationListMeta = revocationListMeta;
    }
}
