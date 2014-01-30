package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "wrk_address")
public class Address implements Serializable {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_ADDRESS_ID", sequenceName = "SEQ_WRK_ADDRESS_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_ADDRESS_ID")
    private long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    Customer customer;

    @OneToOne
    @JoinColumn(name = "address_type_id")
    AddressType addressType;

    @Column(name = "address_no", length = 50)
    private String addressNo;

    @Column(name = "moo", length = 50)
    private String moo;

    @Column(name = "building", length = 100)
    private String building;

    @Column(name = "road", length = 200)
    private String road;

    @OneToOne
    @JoinColumn(name = "province_id")
    private Province province;

    @OneToOne
    @JoinColumn(name = "district_id")
    private District district;

    @OneToOne
    @JoinColumn(name = "subdistrict_id")
    private SubDistrict subDistrict;

    @Column(name = "postal_code", length = 5)
    private String postalCode;

    @OneToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column(name = "phone_number", length = 40)
    private String phoneNumber;

    @Column(name = "phone_extension", length = 10)
    private String extension;

    @Column(name = "contact_name")
    private String contactName;

    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "address")
    private String address;

    @Column(name="address_type_flag", nullable=false, columnDefinition = "int default 0")
    private int addressTypeFlag;

    public Address() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public int getAddressTypeFlag() {
        return addressTypeFlag;
    }

    public void setAddressTypeFlag(int addressTypeFlag) {
        this.addressTypeFlag = addressTypeFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("customer", customer).
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
                append("addressTypeFlag", addressTypeFlag).
                toString();
    }
}
