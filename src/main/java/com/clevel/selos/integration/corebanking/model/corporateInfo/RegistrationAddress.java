package com.clevel.selos.integration.corebanking.model.corporateInfo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;


public class RegistrationAddress implements Serializable {

    private String subdistrict;
    private String district;
    private String province;
    private String postcode;
    private String country;
    private String countryCode;
    private String phoneNo;
    private String extension;
    private String contactName;
    private String contactPhoneNo;
    private String addressNo;
    private String addressMoo;
    private String addressBuilding;
    private String addressStreet;

    public String getContactPhoneNo() {
        return contactPhoneNo;
    }

    public void setContactPhoneNo(String contactPhoneNo) {
        this.contactPhoneNo = contactPhoneNo;
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

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
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
                .append("subdistrict", subdistrict)
                .append("district", district)
                .append("province", province)
                .append("postcode", postcode)
                .append("country", country)
                .append("countryCode", countryCode)
                .append("phoneNo", phoneNo)
                .append("extension", extension)
                .append("contactName", contactName)
                .append("contactPhoneNo", contactPhoneNo)
                .append("addressNo", addressNo)
                .append("addressMoo", addressMoo)
                .append("addressBuilding", addressBuilding)
                .append("addressStreet", addressStreet)
                .toString();
    }
}
