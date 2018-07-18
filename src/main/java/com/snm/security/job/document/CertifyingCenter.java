package com.snm.security.job.document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CertifyingCenter implements Serializable {

    @XmlElement(name="Название")
    public String name;

    @XmlElement(name="ЭлектроннаяПочта")
    public String email;

    @XmlElement(name="КраткоеНазвание")
    public String shortName;

    @XmlElement(name="АдресСИнформациейПоУЦ")
    public String urlToInf;

    @XmlElement(name="Адрес")
    public Address address;

    @XmlElement(name="ПрограммноАппаратныеКомплексы")
    public HardwareSoftwareComplexes hardwareSoftwareComplexes;

    @XmlElement(name="ИНН")
    public String inn;

    @XmlElement(name="ОГРН")
    public String ogrn;

    @XmlElement(name="РеестровыйНомер")
    public String registerNumber;

    @XmlElement(name="СтатусАккредитации")
    public AccreditationStatus accreditationStatus;

    @XmlElement(name="ИсторияСтатусовАккредитации")
    public AccreditationStatusHistory accreditationStatusHistory;

    public List<CertificateData> getCertificateDataList() {

        List<CertificateData> resultList = new ArrayList<>();

        if(hardwareSoftwareComplexes != null) {
            if(hardwareSoftwareComplexes.complexes != null) {
                hardwareSoftwareComplexes.complexes.forEach(complex -> {
                    if (complex != null && complex.authorizedPersonsKeys != null) {
                        if(complex.authorizedPersonsKeys.keys != null) {
                            complex.authorizedPersonsKeys.keys.forEach(key ->  {
                                if(key.certificates != null && key.certificates.certificateDataList != null ) {
                                    resultList.addAll(key.certificates.certificateDataList);
                                }
                            });
                        }
                    }
                });
            }
        }

        return resultList;
    }

    public List<String> getRevocationURLList() {

        List<String> resultList = new ArrayList<>();

        if(hardwareSoftwareComplexes != null) {
            if(hardwareSoftwareComplexes.complexes != null) {
                hardwareSoftwareComplexes.complexes.forEach(complex -> {
                    if(complex.authorizedPersonsKeys != null) {
                        if(complex.authorizedPersonsKeys.keys != null) {
                            complex.authorizedPersonsKeys.keys.forEach(key -> {
                                if(key.addressesListsRevocation != null) {
                                    if(key.addressesListsRevocation.revocationLists != null) {
                                        resultList.addAll(key.addressesListsRevocation.revocationLists);
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }

        return resultList;
    }

    public List<AddressesListsRevocation> getAddressListRevocation() {

        List<AddressesListsRevocation> result = new ArrayList<>();

        if(hardwareSoftwareComplexes != null) {
            if(hardwareSoftwareComplexes.complexes != null) {
                hardwareSoftwareComplexes.complexes.forEach(complex -> {
                    if(complex.authorizedPersonsKeys != null) {
                        if(complex.authorizedPersonsKeys.keys != null) {
                            complex.authorizedPersonsKeys.keys.forEach(key -> {
                                if(key.addressesListsRevocation != null) {
                                    result.add(key.addressesListsRevocation);
                                }
                            });
                        }
                    }
                });
            }
        }

        return  result;
    }
}
