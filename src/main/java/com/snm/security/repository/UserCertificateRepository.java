package com.snm.security.repository;

import com.snm.security.model.UserCertificate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCertificateRepository extends JpaRepository<UserCertificate, Long> {

    @Nullable
    UserCertificate findBySerialNumber(@NotNull String serial);

    @Modifying
    @Query(nativeQuery = true, value = "update user_certificate set revoked=true where revoked=false and serial_number in (select serial_number from revocation_list)")
    int updateRevokedCertificates();
}
