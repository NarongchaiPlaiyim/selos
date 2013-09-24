package com.clevel.selos.model.RMmodel.corporateInfo;

import java.io.Serializable;



public class CorporateModel implements Serializable{

    private String reqId;
    private String resCode;
    private String resDesc;

    private String searchResult;

    //Personal Detail Section
    private String tmbCusID;
    private String titleTH;
    private String companyNameTH;
    private String companyNameEN;
    private String registrationID;
    private String registrationDate;
    private String registrationCountry;
    private String subdistrict;
    private String district;
    private String province;
    private String postcode;
    private String country;
    private String countryCode;

    private RegistrationAddress registrationAddress;


    public String getTmbCusID() {
        return tmbCusID;
    }

    public void setTmbCusID(String tmbCusID) {
        this.tmbCusID = tmbCusID;
    }

    public String getTitleTH() {
        return titleTH;
    }

    public void setTitleTH(String titleTH) {
        this.titleTH = titleTH;
    }

    public String getCompanyNameTH() {
        return companyNameTH;
    }

    public void setCompanyNameTH(String companyNameTH) {
        this.companyNameTH = companyNameTH;
    }


    public String getCompanyNameEN() {
        return companyNameEN;
    }

    public void setCompanyNameEN(String companyNameEN) {
        this.companyNameEN = companyNameEN;
    }


    public String getRegistrationID() {
        return registrationID;
    }

    public void setRegistrationID(String registrationID) {
        this.registrationID = registrationID;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getRegistrationCountry() {
        return registrationCountry;
    }

    public void setRegistrationCountry(String registrationCountry) {
        this.registrationCountry = registrationCountry;
    }

    public String getSubdistrict() {
        return subdistrict;
    }

    public void setSubdistrict(String subdistrict) {
        this.subdistrict = subdistrict;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public RegistrationAddress getRegistrationAddress() {
        return registrationAddress;
    }

    public void setRegistrationAddress(RegistrationAddress registrationAddress) {
        this.registrationAddress = registrationAddress;
    }
}
