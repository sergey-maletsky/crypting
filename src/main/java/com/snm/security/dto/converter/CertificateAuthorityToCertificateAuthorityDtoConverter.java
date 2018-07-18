package com.snm.security.dto.converter;

import com.snm.security.dto.CertificateAuthorityDto;
import com.snm.security.model.CertificateAuthority;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CertificateAuthorityToCertificateAuthorityDtoConverter implements Converter<CertificateAuthority, CertificateAuthorityDto> {

    @Override
    public CertificateAuthorityDto convert(CertificateAuthority certificateAuthority) {

        CertificateAuthorityDto dto = new CertificateAuthorityDto();
        dto.setId(certificateAuthority.getId());
        dto.setSerialNumber(certificateAuthority.getSerialNumber());
        dto.setValidityDateFrom(certificateAuthority.getValidityDateFrom().toInstant());
        dto.setValidityDateUpTo(certificateAuthority.getValidityDateTo().toInstant());
        dto.setCertifyingCenterId(certificateAuthority.getCertifyingCenter().getId());

        return null;
    }
}
