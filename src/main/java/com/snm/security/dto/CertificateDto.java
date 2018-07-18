package com.snm.security.dto;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.time.Instant;

public class CertificateDto implements Serializable {

    @NotNull
    private String serialNumber;
    private String country; //C
    private String state; //ST
    private String region; //ST
    private String location; //L
    private String street; //STREET
    private String organization; //O
    private String organizationUnit; //OU
    private String position; //T
    @NotNull
    private String subjectName; //CN
    private String givenname; //G or GIVENNAME
    private String surname; //SN or SURNAME
    private String email; //E or EMAILADDRESS
    private String inn;
    private String ogrn;
    private String kpp;
    private String ogrnip;
    private String snils;
    @NotNull
    private Instant validityDateFrom;
    @NotNull
    private Instant validityDateUpTo;
    private Instant updatedDate;
    private byte[] data;
    private String issuedBy;
    private String issuedSerialNumber;

    public CertificateDto() {
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getGivenname() {
        return givenname;
    }

    public void setGivenname(String givenname) {
        this.givenname = givenname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getOgrnip() {
        return ogrnip;
    }

    public void setOgrnip(String ogrnip) {
        this.ogrnip = ogrnip;
    }

    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
    }

    public Instant getValidityDateFrom() {
        return validityDateFrom;
    }

    public void setValidityDateFrom(Instant validityDateFrom) {
        this.validityDateFrom = validityDateFrom;
    }

    public Instant getValidityDateUpTo() {
        return validityDateUpTo;
    }

    public void setValidityDateUpTo(Instant validityDateUpTo) {
        this.validityDateUpTo = validityDateUpTo;
    }

    public Instant getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Instant updatedDate) {
        this.updatedDate = updatedDate;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getIssuedSerialNumber() {
        return issuedSerialNumber;
    }

    public void setIssuedSerialNumber(String issuedSerialNumber) {
        this.issuedSerialNumber = issuedSerialNumber;
    }

    @NotNull
    public String getFio() {
        String surname = getSurname(); //Certificate has SN and probable G fields
        String givenName = getGivenname();
        String subjectName = getSubjectName();
        if (!StringUtils.isEmpty(surname)) {
            return !StringUtils.isEmpty(givenName) ? surname + " " + givenName : surname;
        } else {
            return !StringUtils.isEmpty(subjectName) ? subjectName : "";
        }
    }
}