package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class AddressView implements Serializable {
    private long id;
    private AddressType addressType;
    private String addressNo;
    private String moo;
    private String building;
    private String road;
    private Province province;
    private District district;
    private SubDistrict subDistrict;
    private String postalCode;
    private Country country;
    private String phoneNumber;
    private String extension;
    private String contactName;
    private String contactPhone;
    private String address;

    public AddressView() {
        reset();
    }

    public void reset() {
        this.province = new Province();
        this.district = new District();
        this.subDistrict = new SubDistrict();
        this.country = new Country();
        this.addressType = new AddressType();
    }

    public AddressView(AddressView inputAddressView,long id){
        this.id = id;
        this.addressType = inputAddressView.getAddressType();
        this.addressNo = inputAddressView.getAddressNo();
        this.moo = inputAddressView.getMoo();
        this.building = inputAddressView.getBuilding();
        this.road = inputAddressView.getRoad();
        this.province = inputAddressView.getProvince();
        this.district = inputAddressView.getDistrict();
        this.subDistrict = inputAddressView.getSubDistrict();
        this.postalCode = inputAddressView.getPostalCode();
        this.country = inputAddressView.getCountry();
        this.phoneNumber = inputAddressView.getPhoneNumber();
        this.extension = inputAddressView.getExtension();
        this.contactName = inputAddressView.getContactName();
        this.contactPhone = inputAddressView.getContactPhone();
        this.address = inputAddressView.getAddress();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public String getAddressNo() {
        return addressNo;
    }

    public void setAddressNo(String addressNo) {
        this.addressNo = addressNo;
    }

    public String getMoo() {
        return moo;
    }

    public void setMoo(String moo) {
        this.moo = moo;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoad() {
        return road;
    }

    public void setRoad(String road) {
        this.road = road;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public District getDistrict() {
        return district;
    }

    public void setDistrict(District district) {
        this.district = district;
    }

    public SubDistrict getSubDistrict() {
        return subDistrict;
    }

    public void setSubDistrict(SubDistrict subDistrict) {

        this.subDistrict = subDistrict;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("addressType", addressType).
                append("addressNo", addressNo).
                append("moo", moo).
                append("building", building).
                append("road", road).
                append("province", province).
                append("district", district).
                append("subDistrict", subDistrict).
                append("postalCode", postalCode).
                append("country", country).
                append("phoneNumber", phoneNumber).
                append("extension", extension).
                append("contactName", contactName).
                append("contactPhone", contactPhone).
                append("address", address).
                toString();
    }
}
