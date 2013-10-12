package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.OpenAccountProduct;
import com.clevel.selos.model.db.master.OpenAccountPurpose;
import com.clevel.selos.model.db.master.OpenAccountType;

import java.io.Serializable;

public class BasicInfoAccountView implements Serializable {
    private String accountName;
    private OpenAccountType accountType;
    private OpenAccountProduct product;
    private OpenAccountPurpose purpose;

    public BasicInfoAccountView(){
        reset();
    }

    public void reset(){
        this.accountType = new OpenAccountType();
        this.product = new OpenAccountProduct();
        this.purpose = new OpenAccountPurpose();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public OpenAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(OpenAccountType accountType) {
        this.accountType = accountType;
    }

    public OpenAccountProduct getProduct() {
        return product;
    }

    public void setProduct(OpenAccountProduct product) {
        this.product = product;
    }

    public OpenAccountPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(OpenAccountPurpose purpose) {
        this.purpose = purpose;
    }
}
