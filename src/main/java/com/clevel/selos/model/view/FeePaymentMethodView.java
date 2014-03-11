package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class FeePaymentMethodView implements Serializable {
    private int id;
    private String description;
    private boolean includeInAgreementSign;
    private boolean debitFromCustomer;
    private int active;
    private String brmsCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isIncludeInAgreementSign() {
        return includeInAgreementSign;
    }

    public void setIncludeInAgreementSign(boolean includeInAgreementSign) {
        this.includeInAgreementSign = includeInAgreementSign;
    }

    public boolean isDebitFromCustomer() {
        return debitFromCustomer;
    }

    public void setDebitFromCustomer(boolean debitFromCustomer) {
        this.debitFromCustomer = debitFromCustomer;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getBrmsCode() {
        return brmsCode;
    }

    public void setBrmsCode(String brmsCode) {
        this.brmsCode = brmsCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("description", description)
                .append("includeInAgreementSign", includeInAgreementSign)
                .append("debitFromCustomer", debitFromCustomer)
                .append("active", active)
                .append("brmsCode", brmsCode)
                .toString();
    }
}
