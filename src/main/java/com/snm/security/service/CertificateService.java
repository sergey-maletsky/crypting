package com.snm.security.service;

import com.snm.security.dto.CertificateDto;
import com.snm.security.dto.validation.CertificateValidationResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.transaction.annotation.Transactional;

public interface CertificateService {

    @Nullable
    @Transactional(readOnly = true)
    CertificateDto findUserCertificateBySerial(@NotNull String serial);

    CertificateValidationResult validateCertificate(String data, String randomValue) throws Exception;

    boolean checkCertificatesAreDifferent(CertificateDto currentCertificate);

    @NotNull CertificateDto saveUserCertificate(@NotNull CertificateDto certificateDto);
}
