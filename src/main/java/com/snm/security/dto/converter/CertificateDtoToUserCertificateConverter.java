package com.snm.security.dto.converter;

import com.snm.security.dto.CertificateDto;
import com.snm.security.model.CertifyingCenter;
import com.snm.security.model.UserCertificate;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class CertificateDtoToUserCertificateConverter implements Converter<CertificateDto, UserCertificate> {

    @Override
    public UserCertificate convert(CertificateDto dto) {

        UserCertificate certificate = new UserCertificate();
        certificate.setSerialNumber(dto.getSerialNumber());
        certificate.setValidityDateFrom(Timestamp.from(dto.getValidityDateFrom()));
        certificate.setValidityDateTo(Timestamp.from(dto.getValidityDateUpTo()));
        certificate.setData(dto.getData());
        certificate.setOrganisationName(dto.getOrganization());
        certificate.setFullName(dto.getFio());
        certificate.setRevoked(false);

        CertifyingCenter certifyingCenter = new CertifyingCenter();
        certifyingCenter.setId(dto.getCertificateAuthorityId());
        certificate.setCertifyingCenter(certifyingCenter);

        return certificate;
    }
}
