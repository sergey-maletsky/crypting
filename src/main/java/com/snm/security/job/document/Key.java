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
public class Key implements Serializable {

    @XmlElement(name="ИдентификаторКлюча")
    public String id;

    @XmlElement(name="АдресаСписковОтзыва")
    public AddressesListsRevocation addressesListsRevocation;

    @XmlElement(name="Сертификаты")
    public Certificates certificates;
}
