package com.snm.security.service.impl;

import com.snm.security.dto.AuthDto;
import com.snm.security.dto.CertificateDto;
import com.snm.security.dto.JsonResult;
import com.snm.security.dto.validation.CertificateValidationResult;
import com.snm.security.exception.IncorrectCertificateException;
import com.snm.security.exception.NotUniqueException;
import com.snm.security.service.AuthService;
import com.snm.security.service.CertificateService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Objects;

import static com.snm.security.dto.JsonResult.ErrorCode.NO_ERROR;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Value("${app.validate.user.certs.by.ca.strictly}")
    private boolean validateUserCertsByCaStrictly;

    @Autowired
    private CertificateService certificateService;

    @Override
    public JsonResult checkUserCertificate(AuthDto authDto, String randomValue) {

        CertificateValidationResult certificateValidationResult =
                getCertificateValidationResult(authDto.getData(), randomValue);
        checkErrors(certificateValidationResult);

        CertificateDto certificateDto = certificateValidationResult.getCertificate();
        CertificateDto existingCertificateDto = certificateService.
                findUserCertificateBySerial(certificateDto.getSerialNumber());

        return getJsonResult(authDto.getIsReg(), certificateDto, existingCertificateDto);
    }

    private CertificateValidationResult getCertificateValidationResult(String data, String randomValue) {

        CertificateValidationResult certificateValidationResult;
        try {
            certificateValidationResult = certificateService.validateCertificate(data,
                    randomValue, validateUserCertsByCaStrictly);
        } catch (Exception e) {
            log.error("Error while process the certificate", e);
            throw new IncorrectCertificateException("Error while process the certificate. The certificate is incorrect");
        }

        return certificateValidationResult;
    }

    private CertificateDto checkErrors(CertificateValidationResult certificateValidationResult) {
        CertificateDto certificateDto = certificateValidationResult.getCertificate();
        if (certificateDto == null || !certificateValidationResult.getErrors().isEmpty()) {
            log.error("The certificate validation is failed");
            String joinedErrors = String.join("; ", certificateValidationResult.getErrors());

            throw new IncorrectCertificateException(joinedErrors);
        }

        return certificateDto;
    }

    @NotNull
    private JsonResult getJsonResult(Boolean isReg, CertificateDto certificateDto, CertificateDto existingCertificateDto) {

        JsonResult result = new JsonResult<>(NO_ERROR);
        if (isReg) {
            if(Objects.nonNull(existingCertificateDto)) {
                log.error("Certificate with {} serial number already exists", existingCertificateDto.getSerialNumber());
                throw new NotUniqueException("Сертификат уже существует в системе");
            }

            result.setMessage("Вы зарегистрировались в системе");
            result.setResult(certificateService.saveUserCertificate(certificateDto));
        } else {
            if (Objects.isNull(existingCertificateDto)) {
                log.error("Certificate with {} serial number not found in the system", certificateDto.getSerialNumber());
                throw new EntityNotFoundException("Сертификат не найден в системе");
            }

            result.setMessage("Вы вошли в систему");
            result.setResult(existingCertificateDto);
        }
        return result;
    }
}
