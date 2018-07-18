package com.snm.security.util;

import com.snm.security.dto.CertificateDto;
import com.snm.security.dto.validation.CertificateValidationResult;
import org.apache.commons.lang3.ObjectUtils;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import java.math.BigInteger;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.snm.security.util.AppConstants.*;

public class CryptoProUtil {

    private static final Logger log = LoggerFactory.getLogger(CryptoProUtil.class);

    static {

        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Get a CertificateValidateResult from a data in base64 and random value
     * that was sent to check the connection handshake
     *
     * @param data data in base64 format
     * @param randomValue random value
     * @return certificate validate result
     * @throws Exception if certificate retrieving is failed
     */
    public static CertificateValidationResult validateCertificate(String data, String randomValue) throws Exception {

        CertificateValidationResult result =
                new CertificateValidationResult(ObjectUtils.defaultIfNull(data, EMPTY));
        X509Certificate certificate = getCertificate(data, randomValue);
        // Checking of a validity of the signature
        if (Objects.isNull(certificate)) {
            log.error("The signature is not valid");
            result.setCertificate(null);
            result.getErrors().add("The signature is not valid");
            return result;
        }

        CertificateDto certificateDto = null;
        try {
            certificateDto = extractCertificateDto(certificate);
        } catch (Exception e) {
            log.error("Error while extracting the certificate", e);
            result.getErrors().add("Internal error while retrieving data from the certificate");
        }

        result.setCertificate(certificateDto);

        // Check if it has expired
        try {
            certificate.checkValidity();
        } catch (CertificateException e) {
            log.error("Certificate not trusted. It has expired", e);
            result.getErrors().add("The certificate has expired");
        }

        return result;
    }

    /**
     * Get a certificate from a data in base64 and random value
     * that was sent to check the connection handshake
     *
     * @param data data in base64 format
     * @param randomValue random value
     * @return x509 certificate
     * @throws Exception if decodedData has no right content
     */
    private static X509Certificate getCertificate(String data, String randomValue) throws Exception {
        byte[] decodedData = null;
        if (org.apache.commons.codec.binary.Base64.isBase64(data)) {
            decodedData = org.apache.commons.codec.binary.Base64.decodeBase64(data);
        }

        CMSSignedData signedData = null;
        X509Certificate certificate = null;
        if (Objects.nonNull(decodedData)) {
            signedData = new CMSSignedData(decodedData);
            certificate = verifySignedMessage(signedData);
        }

        if (Objects.nonNull(certificate)) {
            byte[] clearData = (byte[]) signedData.getSignedContent().getContent();
            String signedContent = new String(clearData).replaceAll(UNICODE_NULL, EMPTY);
            if (!randomValue.equals(signedContent)) {
                return null;
            }
        }

        return certificate;
    }

    /**
     * Verify the signature using the cert embedded into the signed object
     *
     * @param data signed data
     * @return the signed contentList
     */
    private static X509Certificate verifySignedMessage(CMSSignedData data) {
        X509Certificate certificate = null;
        Security.addProvider(new BouncyCastleProvider());
        Store certs = data.getCertificates();
        SignerInformation signer = data.getSignerInfos().getSigners().iterator().next();
        X509CertificateHolder certificateHolder = (X509CertificateHolder) certs.getMatches(
                signer.getSID()).iterator().next();

        try {
            if (signer.verify(new JcaSimpleSignerInfoVerifierBuilder().
                    setProvider(BC).build(certificateHolder))) {
                log.info("Signature is verified");
                certificate = new JcaX509CertificateConverter()
                        .setProvider(BC).
                                getCertificate(certificateHolder);
            } else {
                log.error("Signature verification is failed");
            }
        } catch (OperatorCreationException | CertificateException | CMSException e) {
            log.error("Error while verifying the signed message", e.getMessage());
        }

        return certificate;
    }

    /**
     * Get a CertificateDto from x509 certificate
     *
     * @param certificate x509 certificate
     * @return data of the certificate
     * @throws Exception if there is no corresponding name in the principal
     * or there is error while certificate encoding
     */
    private static CertificateDto extractCertificateDto(X509Certificate certificate) throws Exception {
        CertificateDto certificateDto = new CertificateDto();

        if (Objects.nonNull(certificate)) {
            BigInteger serialNumber = certificate.getSerialNumber();
            Instant notBefore = certificate.getNotBefore().toInstant();
            Instant notAfter = certificate.getNotAfter().toInstant();
            certificateDto.setSerialNumber(serialNumber.toString());
            certificateDto.setValidityDateFrom(notBefore);
            certificateDto.setValidityDateUpTo(notAfter);
            certificateDto.setData(certificate.getEncoded());
            certificateDto.setUpdatedDate(Instant.now());
            certificateDto.setIssuedBy(getIssueCN(certificate));
            certificateDto.setIssuedSerialNumber(getIssueSerialNumber(certificate));

            String subjectPrincipalName = certificate.getSubjectDN().getName(); //getSubjectX500Principal().getName();
            LdapName ldapDN = new LdapName(subjectPrincipalName);
            Map<String, String> rdnTypeValue = new HashMap<>();
            for (Rdn rdn : ldapDN.getRdns()) {
                if (rdn.getValue() instanceof String) {
                    rdnTypeValue.put(rdn.getType(), (String) rdn.getValue());
                } else if (rdn.getValue() instanceof byte[]) { // INN
                    byte[] bytes = (byte[]) rdn.getValue();
                    char[] convertedChar = new char[bytes.length - DOUBLE_SHIFT];
                    for (int i = DOUBLE_SHIFT; i < bytes.length; i++) {
                        convertedChar[i - DOUBLE_SHIFT] = (char) bytes[i];
                    }

                    rdnTypeValue.put(rdn.getType(), String.valueOf(convertedChar));
                }
            }

            setCertificateDtoFieldsFromRdn(rdnTypeValue, certificateDto, subjectPrincipalName);
        }

        return certificateDto;
    }

    private static String getIssueCN(X509Certificate certificate) throws CertificateEncodingException {
        X500Name x500name = new JcaX509CertificateHolder(certificate).getIssuer();
        RDN issueCn = x500name.getRDNs(BCStyle.CN)[0];

        return IETFUtils.valueToString(issueCn.getFirst().getValue());
    }

    private static String getIssueSerialNumber(X509Certificate certificate) {
        String serialNumber = "";
        byte[] extensionValue = certificate.getExtensionValue(AUTHORITY_KEY_IDENTIFIER_OID);

        ASN1OctetString akiOc = ASN1OctetString.getInstance(extensionValue);
        if (Objects.nonNull(akiOc)) {
            AuthorityKeyIdentifier aki = AuthorityKeyIdentifier.getInstance(akiOc.getOctets());
            if (Objects.nonNull(aki.getAuthorityCertSerialNumber())) {
                serialNumber = aki.getAuthorityCertSerialNumber().toString();
            }
        }

        return serialNumber;
    }

    private static void setCertificateDtoFieldsFromRdn(Map<String, String> rdnTypeValue, CertificateDto certificateDto, String subjectPrincipalName) {
        if (!rdnTypeValue.isEmpty()) {
            if (rdnTypeValue.containsKey(C_FIELD)) {
                certificateDto.setCountry(rdnTypeValue.get(C_FIELD));
            }
            if (rdnTypeValue.containsKey(ST_FIELD)) {
                certificateDto.setState(rdnTypeValue.get(ST_FIELD));
            }
            if (rdnTypeValue.containsKey(S_FIELD)) {
                certificateDto.setRegion(rdnTypeValue.get(S_FIELD));
            }
            if (rdnTypeValue.containsKey(L_FIELD)) {
                certificateDto.setLocation(rdnTypeValue.get(L_FIELD));
            }
            if (rdnTypeValue.containsKey(STREET_FIELD)) {
                certificateDto.setStreet(rdnTypeValue.get(STREET_FIELD));
            }
            if (rdnTypeValue.containsKey(O_FIELD)) {
                certificateDto.setOrganization(rdnTypeValue.get(O_FIELD));
            }
            if (rdnTypeValue.containsKey(OU_FIELD)) {
                certificateDto.setOrganizationUnit(rdnTypeValue.get(OU_FIELD));
            }
            if (rdnTypeValue.containsKey(T_FIELD)) {
                certificateDto.setPosition(rdnTypeValue.get(T_FIELD));
            }
            if (rdnTypeValue.containsKey(CN_FIELD)) {
                certificateDto.setSubjectName(rdnTypeValue.get(CN_FIELD));
            }
            if (rdnTypeValue.containsKey(GIVENNAME_FIELD)) {
                certificateDto.setGivenname(rdnTypeValue.get(GIVENNAME_FIELD));
            } else if (rdnTypeValue.containsKey(G_FIELD)) {
                certificateDto.setGivenname(rdnTypeValue.get(G_FIELD));
            }
            if (rdnTypeValue.containsKey(SURNAME_FIELD)) {
                certificateDto.setSurname(rdnTypeValue.get(SURNAME_FIELD));
            } else if (rdnTypeValue.containsKey(SN_FIELD)) {
                certificateDto.setSurname(rdnTypeValue.get(SN_FIELD));
            }
            if (rdnTypeValue.containsKey(OGRN_FIELD)) {
                certificateDto.setOgrn(rdnTypeValue.get(OGRN_FIELD));
            } else if (rdnTypeValue.containsKey(OGRN_NUM_FIELD)) {
                certificateDto.setOgrn(rdnTypeValue.get(OGRN_NUM_FIELD));
            } else if (rdnTypeValue.containsKey(OGRN_RUS_FIELD)) {
                certificateDto.setOgrn(rdnTypeValue.get(OGRN_RUS_FIELD));
            }
            if (rdnTypeValue.containsKey(PPC_FIELD)) {
                certificateDto.setKpp(rdnTypeValue.get(PPC_FIELD));
            } else if (rdnTypeValue.containsKey(PPC_NUM_FIELD)) {
                certificateDto.setKpp(rdnTypeValue.get(PPC_NUM_FIELD));
            } else if (rdnTypeValue.containsKey(PPC_RUS_FIELD)) {
                certificateDto.setKpp(rdnTypeValue.get(PPC_RUS_FIELD));
            }
            if (rdnTypeValue.containsKey(OGRNIP_FIELD)) {
                certificateDto.setOgrnip(rdnTypeValue.get(OGRNIP_FIELD));
            } else if (rdnTypeValue.containsKey(OGRNIP_NUM_FIELD)) {
                certificateDto.setOgrnip(rdnTypeValue.get(OGRNIP_NUM_FIELD));
            } else if (rdnTypeValue.containsKey(OGRNIP_RUS_FIELD)) {
                certificateDto.setOgrnip(rdnTypeValue.get(OGRNIP_RUS_FIELD));
            }
            if (rdnTypeValue.containsKey(SNILS_FIELD)) {
                certificateDto.setSnils(rdnTypeValue.get(SNILS_FIELD));
            } else if (rdnTypeValue.containsKey(SNILS_NUM_FIELD)) {
                certificateDto.setSnils(rdnTypeValue.get(SNILS_NUM_FIELD));
            } else if (rdnTypeValue.containsKey(SNILS_RUS_FIELD)) {
                certificateDto.setSnils(rdnTypeValue.get(SNILS_RUS_FIELD));
            }

            if (rdnTypeValue.containsKey(E_FIELD)) {
                certificateDto.setEmail(rdnTypeValue.get(E_FIELD));
            } else if (rdnTypeValue.containsKey(EMAIL_ADDRESS_FIELD)) {
                certificateDto.setEmail(rdnTypeValue.get(EMAIL_ADDRESS_FIELD));
            } else if (rdnTypeValue.containsKey(EMAIL_ADDRESS_UPPERCASE_FIELD)) {
                certificateDto.setEmail(rdnTypeValue.get(EMAIL_ADDRESS_UPPERCASE_FIELD));
            }

            if (rdnTypeValue.containsKey(INN_OID_NUM_FIELD)) {
                certificateDto.setInn(rdnTypeValue.get(INN_OID_NUM_FIELD));
            } else if (rdnTypeValue.containsKey(INN_ORG_ID_FIELD)) {
                certificateDto.setInn(rdnTypeValue.get(INN_ORG_ID_FIELD));
            } else if (rdnTypeValue.containsKey("1.2.643.3.131.1.1")) {
                certificateDto.setInn(rdnTypeValue.get("1.2.643.3.131.1.1"));
            } else if (subjectPrincipalName.contains(INN_FIELD)) {
                String temp = subjectPrincipalName.substring(subjectPrincipalName.indexOf(INN_FIELD + EQUAL_SIGN), subjectPrincipalName.length());
                String inn = temp.substring(temp.indexOf(INN_FIELD + EQUAL_SIGN) + QUADRUPLE_SHIFT, temp.indexOf(COMMA));
                certificateDto.setInn(inn);
            } else if (subjectPrincipalName.contains(INN_RUS_FIELD)) {
                String temp = subjectPrincipalName.substring(subjectPrincipalName.indexOf(INN_RUS_FIELD + EQUAL_SIGN), subjectPrincipalName.length());
                String inn = temp.substring(temp.indexOf(INN_RUS_FIELD + EQUAL_SIGN) + QUADRUPLE_SHIFT, temp.indexOf(COMMA));
                certificateDto.setInn(inn);
            }
        }
    }
}
