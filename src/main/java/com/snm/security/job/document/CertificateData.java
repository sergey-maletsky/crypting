package com.snm.security.job.document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CertificateData implements Serializable {

    @XmlElement(name="Отпечаток")
    public String imprint;

    @XmlElement(name="КемВыдан")
    public String issuedBy;

    @XmlElement(name="КомуВыдан")
    public String issuedTo;

    @XmlElement(name="СерийныйНомер")
    public String serialNumber;

    @XmlElement(name="ПериодДействияС")
    public String validOf;

    @XmlElement(name="ПериодДействияДо")
    public String validTo;

    @XmlElement(name="Данные")
    public String data;
}
