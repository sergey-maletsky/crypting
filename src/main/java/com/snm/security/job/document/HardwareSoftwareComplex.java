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
public class HardwareSoftwareComplex implements Serializable {

    @XmlElement(name="Псевдоним")
    public String alias;

    @XmlElement(name="КлассСредствЭП")
    public String epClass;

    @XmlElement(name="Адрес")
    public Address address;

    @XmlElement(name="СредстваУЦ")
    public String certifyingCenterTools;

    @XmlElement(name="КлючиУполномоченныхЛиц")
    public AuthorizedPersonsKeys authorizedPersonsKeys;
}
