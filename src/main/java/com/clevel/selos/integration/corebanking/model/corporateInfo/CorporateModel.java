package com.clevel.selos.integration.corebanking.model.corporateInfo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

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
    private String documentType;
    private String registrationID;
    private String registrationDate;
    private String registrationCountry;
    private String subdistrict;
    private String district;
    private String province;
    private String postcode;
    private String country;
    private String countryCode;
    private String addressNo;
    private String addressMoo;
    private String addressBuilding;
    private String addressStreet;

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

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getAddressNo() {
        return addressNo;
    }

    public void setAddressNo(String addressNo) {
        this.addressNo = addressNo;
    }

    public String getAddressMoo() {
        return addressMoo;
    }

    public void setAddressMoo(String addressMoo) {
        this.addressMoo = addressMoo;
    }

    public String getAddressBuilding() {
        return addressBuilding;
    }

    public void setAddressBuilding(String addressBuilding) {
        this.addressBuilding = addressBuilding;
    }

    public String getAddressStreet() {
        return addressStreet;
    }

    public void setAddressStreet(String addressStreet) {
        this.addressStreet = addressStreet;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("reqId", reqId)
                .append("resCode", resCode)
                .append("resDesc", resDesc)
                .append("searchResult", searchResult)
                .append("tmbCusID", tmbCusID)
                .append("titleTH", titleTH)
                .append("companyNameTH", companyNameTH)
                .append("companyNameEN", companyNameEN)
                .append("documentType", documentType)
                .append("registrationID", registrationID)
                .append("registrationDate", registrationDate)
                .append("registrationCountry", registrationCountry)
                .append("subdistrict", subdistrict)
                .append("district", district)
                .append("province", province)
                .append("postcode", postcode)
                .append("country", country)
                .append("countryCode", countryCode)
                .append("registrationAddress", registrationAddress)
                .append("addressNo", addressNo)
                .append("addressMoo", addressMoo)
                .append("addressBuilding", addressBuilding)
                .append("addressStreet", addressStreet)
                .toString();
    }
}
