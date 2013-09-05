package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;

public class CustomerView {
    private BigDecimal id;
    private String customerName;
    private String citizenID;
    private String inputCitizenID;
    private int validCitizenID;

    public CustomerView(){

    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(String citizenID) {
        this.citizenID = citizenID;
    }

    public String getInputCitizenID() {
        return inputCitizenID;
    }

    public void setInputCitizenID(String inputCitizenID) {
        this.inputCitizenID = inputCitizenID;
    }

    public int getValidCitizenID() {
        return validCitizenID;
    }

    public void setValidCitizenID(int validCitizenID) {
        this.validCitizenID = validCitizenID;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("customerName", customerName)
                .append("citizenID", citizenID)
                .append("inputCitizenID", inputCitizenID)
                .append("validCitizenID", validCitizenID)
                .toString();
    }
}
