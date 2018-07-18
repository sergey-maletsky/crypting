package com.snm.security.job.document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name="АккредитованныеУдостоверяющиеЦентры")
public class AccreditedCertificationCenters implements Serializable {

    @XmlElement(name="Версия")
    public String version;

    @XmlElement(name="Дата")
    public String date;

    @XmlElement(name="УдостоверяющийЦентр")
    public List<CertifyingCenter> centers;
}
