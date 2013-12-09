package com.clevel.selos.integration.coms.db;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class HeadCollateral implements Serializable {
    private String colId; //1. COL_ID
    private String colNo; //2. COL_NO
    private String crmLocation; //3. CRM_LOCATION
    private String addDistrict; //4. ADD_DISTRICT
    private String addCity; //5. ADD_CITY
    private String cityExpand; //6. CITY_EXPAND
    private String provExpand; //7. PROV_EXPAND
    private String countryCode; //8. SET_COUNTRY.CODE
    private String countryNameThai; //9. SET_COUNTRY.NAME_THAI
    private String cityId; //10. CITY.CITY_ID
    private String city; //11. CITY.CITY
    private String cityProvinceId; //12. CITY.PROVINCE_ID
    private String provId; //13. PROVINCE.PROV_ID
    private String provName; //14. PROVINCE.PROV_NAME
    private BigDecimal cPrice; //15. C_PRICE
    private BigDecimal matiPrice; //16. MATI_PRICE
    private String colType; //17. COL_TYPE
    private String colSubType; //18. COL_SUB_TYPE

    public String getColId() {
        return colId;
    }

    public void setColId(String colId) {
        this.colId = colId;
    }

    public String getColNo() {
        return colNo;
    }

    public void setColNo(String colNo) {
        this.colNo = colNo;
    }

    public String getCrmLocation() {
        return crmLocation;
    }

    public void setCrmLocation(String crmLocation) {
        this.crmLocation = crmLocation;
    }

    public String getAddDistrict() {
        return addDistrict;
    }

    public void setAddDistrict(String addDistrict) {
        this.addDistrict = addDistrict;
    }

    public String getAddCity() {
        return addCity;
    }

    public void setAddCity(String addCity) {
        this.addCity = addCity;
    }

    public String getCityExpand() {
        return cityExpand;
    }

    public void setCityExpand(String cityExpand) {
        this.cityExpand = cityExpand;
    }

    public String getProvExpand() {
        return provExpand;
    }

    public void setProvExpand(String provExpand) {
        this.provExpand = provExpand;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryNameThai() {
        return countryNameThai;
    }

    public void setCountryNameThai(String countryNameThai) {
        this.countryNameThai = countryNameThai;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCityProvinceId() {
        return cityProvinceId;
    }

    public void setCityProvinceId(String cityProvinceId) {
        this.cityProvinceId = cityProvinceId;
    }

    public String getProvId() {
        return provId;
    }

    public void setProvId(String provId) {
        this.provId = provId;
    }

    public String getProvName() {
        return provName;
    }

    public void setProvName(String provName) {
        this.provName = provName;
    }

    public BigDecimal getcPrice() {
        return cPrice;
    }

    public void setcPrice(BigDecimal cPrice) {
        this.cPrice = cPrice;
    }

    public BigDecimal getMatiPrice() {
        return matiPrice;
    }

    public void setMatiPrice(BigDecimal matiPrice) {
        this.matiPrice = matiPrice;
    }

    public String getColType() {
        return colType;
    }

    public void setColType(String colType) {
        this.colType = colType;
    }

    public String getColSubType() {
        return colSubType;
    }

    public void setColSubType(String colSubType) {
        this.colSubType = colSubType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("colId", colId)
                .append("colNo", colNo)
                .append("crmLocation", crmLocation)
                .append("addDistrict", addDistrict)
                .append("addCity", addCity)
                .append("cityExpand", cityExpand)
                .append("provExpand", provExpand)
                .append("countryCode", countryCode)
                .append("countryNameThai", countryNameThai)
                .append("cityId", cityId)
                .append("city", city)
                .append("cityProvinceId", cityProvinceId)
                .append("provId", provId)
                .append("provName", provName)
                .append("cPrice", cPrice)
                .append("matiPrice", matiPrice)
                .append("colType", colType)
                .append("colSubType", colSubType)
                .toString();
    }
}
