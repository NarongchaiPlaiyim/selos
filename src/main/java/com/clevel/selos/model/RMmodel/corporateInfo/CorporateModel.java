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
    private String companyNameTH1;
    private String companyNameTH2;
    private String companyNameTH3;
    private String companyNameEN1;
    private String companyNameEN2;
    private String companyNameEN3;
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

    public String getCompanyNameTH1() {
        return companyNameTH1;
    }

    public void setCompanyNameTH1(String companyNameTH1) {
        this.companyNameTH1 = companyNameTH1;
    }

    public String getCompanyNameTH2() {
        return companyNameTH2;
    }

    public void setCompanyNameTH2(String companyNameTH2) {
        this.companyNameTH2 = companyNameTH2;
    }

    public String getCompanyNameTH3() {
        return companyNameTH3;
    }

    public void setCompanyNameTH3(String companyNameTH3) {
        this.companyNameTH3 = companyNameTH3;
    }

    public String getCompanyNameEN1() {
        return companyNameEN1;
    }

    public void setCompanyNameEN1(String companyNameEN1) {
        this.companyNameEN1 = companyNameEN1;
    }

    public String getCompanyNameEN2() {
        return companyNameEN2;
    }

    public void setCompanyNameEN2(String companyNameEN2) {
        this.companyNameEN2 = companyNameEN2;
    }

    public String getCompanyNameEN3() {
        return companyNameEN3;
    }

    public void setCompanyNameEN3(String companyNameEN3) {
        this.companyNameEN3 = companyNameEN3;
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
