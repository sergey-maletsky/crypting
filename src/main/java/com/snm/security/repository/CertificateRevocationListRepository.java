package com.snm.security.repository;

import com.snm.security.model.CertificateRevocationList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateRevocationListRepository extends JpaRepository<CertificateRevocationList, Long> {

    @Nullable
    CertificateRevocationList findBySerialNumber(@NotNull String serial);
}
