package com.snm.security.job.document;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.util.List;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class HardwareSoftwareComplexes implements Serializable {

    @XmlElement(name="ПрограммноАппаратныйКомплекс")
    public List<HardwareSoftwareComplex> complexes;
}
