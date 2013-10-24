package com.clevel.selos.integration.corebanking.model.individualInfo;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class ContactDetails implements Serializable{
    private String addressNo;
    private String addressMoo;
    private String addressBuilding;
    private String addressStreet;
    private String subdistrict;
    private String district;
    private String province;
    private String postcode;
    private String country;
    private String countryCode;

    public ContactDetails(){

    }

    public ContactDetails(String addressNo, String addressMoo, String addressBuilding, String addressStreet, String subdistrict, String district, String province, String postcode, String country, String countryCode) {
        this.addressNo = addressNo;
        this.addressMoo = addressMoo;
        this.addressBuilding = addressBuilding;
        this.addressStreet = addressStreet;
        this.subdistrict = subdistrict;
        this.district = district;
        this.province = province;
        this.postcode = postcode;
        this.country = country;
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("addressNo", addressNo)
                .append("addressMoo", addressMoo)
                .append("addressBuilding", addressBuilding)
                .append("addressStreet", addressStreet)
                .append("subdistrict", subdistrict)
                .append("district", district)
                .append("province", province)
                .append("postcode", postcode)
                .append("country", country)
                .append("countryCode", countryCode)
                .toString();
    }
}
