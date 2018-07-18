package com.snm.security.service;

import com.snm.security.dto.CertificateAuthorityDto;
import com.snm.security.dto.CertificateDto;
import com.snm.security.dto.CertificateRevocationListDto;
import com.snm.security.dto.validation.CertificateValidationResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CertificateService {

    @Nullable
    CertificateDto findUserCertificateBySerial(@NotNull String serial);

    CertificateAuthorityDto findCertificateAuthorityBySerial(@NotNull String serial);

    CertificateRevocationListDto findRevocationListBySerial(@NotNull String serial);

    CertificateValidationResult validateCertificate(String data, String randomValue, boolean validateUserCertsByCaStrictly) throws Exception;

    boolean checkCertificatesAreDifferent(CertificateDto currentCertificate);

    @NotNull CertificateDto saveUserCertificate(@NotNull CertificateDto certificateDto);
}
