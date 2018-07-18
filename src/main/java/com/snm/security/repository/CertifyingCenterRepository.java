package com.snm.security.repository;

import com.snm.security.model.CertifyingCenter;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertifyingCenterRepository extends JpaRepository<CertifyingCenter, Long> {

    @NotNull
    List<CertifyingCenter> findByName(@NotNull String name);
}
