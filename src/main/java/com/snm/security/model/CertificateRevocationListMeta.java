package com.snm.security.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "revocation_list_meta")
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CertificateRevocationListMeta {

    @Id
    @SequenceGenerator(name = "revocation_list_meta_sequence", sequenceName = "revocation_list_meta_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "revocation_list_meta_sequence")
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String algorithm;

    @Column(nullable = false)
    private Timestamp updateDate;

    @ManyToOne
    @JoinColumn(name = "certifying_id")
    private CertifyingCenter certifyingCenter;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "revocationListMeta", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<CertificateRevocationList> revocationListSet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public CertifyingCenter getCertifyingCenter() {
        return certifyingCenter;
    }

    public void setCertifyingCenter(CertifyingCenter certifyingCenter) {
        this.certifyingCenter = certifyingCenter;
    }

    public Set<CertificateRevocationList> getRevocationListSet() {
        return revocationListSet;
    }

    public void setRevocationListSet(Set<CertificateRevocationList> revocationListSet) {
        this.revocationListSet = revocationListSet;
    }
}
