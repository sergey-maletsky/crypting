package com.snm.security.dto.converter;

import com.snm.security.dto.CertificateDto;
import com.snm.security.model.UserCertificate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserCertificateToCertificateDtoConverter implements Converter<UserCertificate, CertificateDto> {

    @Override
    public CertificateDto convert(UserCertificate certificate) {

        CertificateDto dto = new CertificateDto();
        dto.setSerialNumber(certificate.getSerialNumber());
        dto.setValidityDateFrom(certificate.getValidityDateFrom().toInstant());
        dto.setValidityDateUpTo(certificate.getValidityDateUpTo().toInstant());

        return dto;
    }
}
