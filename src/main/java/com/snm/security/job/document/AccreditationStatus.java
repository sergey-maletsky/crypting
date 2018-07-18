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
public class AccreditationStatus implements Serializable {

    @XmlElement(name="Статус")
    public String status;

    @XmlElement(name="ДействуетС")
    public String validFrom;
}
