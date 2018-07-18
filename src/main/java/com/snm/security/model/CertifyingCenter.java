package com.snm.security.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "certifying_center")
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CertifyingCenter {

    @Id
    @SequenceGenerator(name = "certifying_center_sequence", sequenceName = "certifying_center_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certifying_center_sequence")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String url;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "certifyingCenter", cascade = { CascadeType.ALL }, orphanRemoval = true)
    private Set<CertificateAuthority> certificateAuthorities;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "certifyingCenter", cascade = { CascadeType.ALL }, orphanRemoval = true)
    private Set<CertificateRevocationListMeta> crlMetaEntitySet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<CertificateAuthority> getCertificateAuthorities() {
        return certificateAuthorities;
    }

    public void setCertificateAuthorities(Set<CertificateAuthority> certificateAuthorities) {
        this.certificateAuthorities = certificateAuthorities;
    }

    public Set<CertificateRevocationListMeta> getCrlMetaEntitySet() {
        return crlMetaEntitySet;
    }

    public void setCrlMetaEntitySet(Set<CertificateRevocationListMeta> crlMetaEntitySet) {
        this.crlMetaEntitySet = crlMetaEntitySet;
    }
}
