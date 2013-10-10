package com.clevel.selos.model.view;

import java.io.Serializable;

public class BasicInfoAccountView implements Serializable {
    private String accountName;
    private String accountType;
    private String product;
    private String purpose;

    public BasicInfoAccountView(){
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }
}
