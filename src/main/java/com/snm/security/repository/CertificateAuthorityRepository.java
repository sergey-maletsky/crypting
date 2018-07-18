package com.snm.security.repository;

import com.snm.security.model.CertificateAuthority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificateAuthorityRepository extends JpaRepository<CertificateAuthority, Long> {

    @Nullable
    CertificateAuthority findBySerialNumber(@NotNull String serial);
}
