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
public class Address implements Serializable {

    @XmlElement(name="Страна")
    public String country;

    @XmlElement(name="Регион")
    public Region region;

    @XmlElement(name="Индекс")
    public String index;

    @XmlElement(name="УлицаДом")
    public String fullHouse;

    @XmlElement(name="Город")
    public String city;
}
