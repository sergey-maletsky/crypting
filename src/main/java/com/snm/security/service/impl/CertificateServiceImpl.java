package com.snm.security.service.impl;

import com.snm.security.dto.CertificateAuthorityDto;
import com.snm.security.dto.CertificateDto;
import com.snm.security.dto.CertificateRevocationListDto;
import com.snm.security.dto.converter.BaseConverter;
import com.snm.security.dto.validation.CertificateValidationResult;
import com.snm.security.exception.NotUniqueException;
import com.snm.security.model.CertificateAuthority;
import com.snm.security.model.CertificateRevocationList;
import com.snm.security.model.UserCertificate;
import com.snm.security.repository.CertificateAuthorityRepository;
import com.snm.security.repository.CertificateRevocationListRepository;
import com.snm.security.repository.UserCertificateRepository;
import com.snm.security.service.CertificateService;
import com.snm.security.util.CryptoProUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class CertificateServiceImpl implements CertificateService {

    private static final Logger log = LoggerFactory.getLogger(CertificateServiceImpl.class);

    @Autowired
    private UserCertificateRepository userCertificateRepository;
    @Autowired
    private CertificateAuthorityRepository certificateAuthorityRepository;
    @Autowired
    private CertificateRevocationListRepository revocationListRepository;
    @Autowired
    private BaseConverter converter;

    @Nullable
    @Override
    @Transactional(readOnly = true)
    public CertificateDto findUserCertificateBySerial(@NotNull String serial) {

        UserCertificate userCertificate = userCertificateRepository.findBySerialNumber(serial);
        return converter.convert(userCertificate, CertificateDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public CertificateAuthorityDto findCertificateAuthorityBySerial(@NotNull String serial) {

        CertificateAuthority certificateAuthority = certificateAuthorityRepository.findBySerialNumber(serial);
        return converter.convert(certificateAuthority, CertificateAuthorityDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public CertificateRevocationListDto findRevocationListBySerial(@NotNull String serial) {

        CertificateRevocationList certificateRevocationList = revocationListRepository.findBySerialNumber(serial);
        return converter.convert(certificateRevocationList, CertificateRevocationListDto.class);
    }

    @Override
    public CertificateValidationResult validateCertificate(String data, String randomValue, boolean validateUserCertsByCaStrictly) throws Exception {
        CertificateValidationResult result = CryptoProUtil.validateCertificate(data, randomValue);
        CertificateDto certificateDto = result.getCertificate();
        if (Objects.isNull(certificateDto)) {
            return result;
        }

        CertificateAuthorityDto certificateAuthorityDto = Objects.isNull(certificateDto.getIssuedSerialNumber()) ? null : findCertificateAuthorityBySerial(certificateDto.getIssuedSerialNumber());
        if (Objects.isNull(certificateAuthorityDto)) {
            if (validateUserCertsByCaStrictly) {
                result.getErrors().add("Корневой сертификат не находится в списке доверенных сертификатов");
            }
            certificateDto.setCertificateAuthorityId(-1L);
        } else {
            certificateDto.setCertificateAuthorityId(certificateAuthorityDto.getCertifyingCenterId());
        }

        CertificateRevocationListDto revocationListDto = findRevocationListBySerial(certificateDto.getSerialNumber());
        if (Objects.nonNull(revocationListDto)) {
            result.getErrors().add("Сертификат находится в списке отозванных сертификатов");
        }

        return result;
    }

    /**
     * Return result of the checking that certificates are different
     *
     * @param currentCertificate the current user certificate
     * @return true if the new certificate and the certificate from the db are different
     */
    @Override
    public boolean checkCertificatesAreDifferent(CertificateDto currentCertificate) {

        CertificateDto certificate = findUserCertificateBySerial(currentCertificate.getSerialNumber());
        if (Objects.isNull(certificate)) {
            return true;
        }
        return false;
    }

    @NotNull
    @Override
    public CertificateDto saveUserCertificate(@NotNull CertificateDto certificateDto) {

        UserCertificate existingUserCertificate = userCertificateRepository
                .findBySerialNumber(certificateDto.getSerialNumber());
        if(Objects.nonNull(existingUserCertificate)) {
            log.error("Certificate with {} serial number already exists", certificateDto.getSerialNumber());
            throw new NotUniqueException("Certificate with name " + certificateDto.getSerialNumber() + " serial number already exists");
        }

        UserCertificate userCertificate = converter.convert(certificateDto, UserCertificate.class);
        return converter.convert(userCertificateRepository.save(userCertificate), CertificateDto.class);
    }
}
