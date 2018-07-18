package com.snm.security.dto.converter;

import com.snm.security.dto.CertificateRevocationListDto;
import com.snm.security.model.CertificateRevocationList;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RevocationListToRevocationListDtoConverter implements Converter<CertificateRevocationList, CertificateRevocationListDto> {

    @Override
    public CertificateRevocationListDto convert(CertificateRevocationList revocationList) {

        CertificateRevocationListDto dto = new CertificateRevocationListDto();
        dto.setId(revocationList.getId());
        dto.setSerialNumber(revocationList.getSerialNumber());
        dto.setRevocationDate(revocationList.getRevocationDate().toInstant());
        dto.setRevocationListMetaId(revocationList.getRevocationListMeta().getId());

        return dto;
    }
}
