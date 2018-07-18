package com.snm.security.repository;

import com.snm.security.model.CertificateRevocationListMeta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRevocationListMetaRepository extends JpaRepository<CertificateRevocationListMeta, Long> {
}
