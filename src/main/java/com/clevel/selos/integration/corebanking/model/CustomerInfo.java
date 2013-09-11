package com.clevel.selos.integration.corebanking.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

public class CustomerInfo {
    private String citizenId;
    private String tmbCustomerId;
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private int age;
    private String addressNumber;

    public CustomerInfo() {
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getTmbCustomerId() {
        return tmbCustomerId;
    }

    public void setTmbCustomerId(String tmbCustomerId) {
        this.tmbCustomerId = tmbCustomerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAddressNumber() {
        return addressNumber;
    }

    public void setAddressNumber(String addressNumber) {
        this.addressNumber = addressNumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("citizenId", citizenId).
                append("tmbCustomerId", tmbCustomerId).
                append("firstName", firstName).
                append("lastName", lastName).
                append("dateOfBirth", dateOfBirth).
                append("age", age).
                append("addressNumber", addressNumber).
                toString();
    }
}
