package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class CustomerBasicView implements Serializable {

    private long id;
    private int customerEntityId;
    private String titleTh;
    private String firstNameTh;
    private String lastNameEm;
    private String tmbCustomerId;
    private String citizenId;
    private String registrationId;

    public CustomerBasicView(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getCustomerEntityId() {
        return customerEntityId;
    }

    public void setCustomerEntityId(int customerEntityId) {
        this.customerEntityId = customerEntityId;
    }

    public String getTitleTh() {
        return titleTh;
    }

    public void setTitleTh(String titleTh) {
        this.titleTh = titleTh;
    }

    public String getFirstNameTh() {
        return firstNameTh;
    }

    public void setFirstNameTh(String firstNameTh) {
        this.firstNameTh = firstNameTh;
    }

    public String getLastNameEm() {
        return lastNameEm;
    }

    public void setLastNameEm(String lastNameEm) {
        this.lastNameEm = lastNameEm;
    }

    public String getTmbCustomerId() {
        return tmbCustomerId;
    }

    public void setTmbCustomerId(String tmbCustomerId) {
        this.tmbCustomerId = tmbCustomerId;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("customerEntityId", customerEntityId)
                .append("titleTh", titleTh)
                .append("firstNameTh", firstNameTh)
                .append("lastNameEm", lastNameEm)
                .append("tmbCustomerId", tmbCustomerId)
                .append("citizenId", citizenId)
                .append("registrationId", registrationId)
                .toString();
    }
}
