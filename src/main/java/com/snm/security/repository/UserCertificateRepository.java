package com.snm.security.repository;

import com.snm.security.model.UserCertificate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCertificateRepository extends JpaRepository<UserCertificate, Long> {

    @Nullable
    UserCertificate findBySerialNumber(@NotNull String serial);
}
