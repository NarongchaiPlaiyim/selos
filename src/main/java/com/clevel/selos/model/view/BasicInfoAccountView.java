package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.OpenAccountProduct;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BasicInfoAccountView implements Serializable {
    private long id;
    private String accountName;
    private BankAccountTypeView bankAccountTypeView;
    private OpenAccountProduct product;
    private List<BasicInfoAccountPurposeView> basicInfoAccountPurposeView;
    private String purposeForShow;

    public BasicInfoAccountView() {
        reset();
    }

    public void reset() {
        this.bankAccountTypeView = new BankAccountTypeView();
        this.product = new OpenAccountProduct();
        this.basicInfoAccountPurposeView = new ArrayList<BasicInfoAccountPurposeView>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BankAccountTypeView getBankAccountTypeView() {
        return bankAccountTypeView;
    }

    public void setBankAccountTypeView(BankAccountTypeView bankAccountTypeView) {
        this.bankAccountTypeView = bankAccountTypeView;
    }

    public OpenAccountProduct getProduct() {
        return product;
    }

    public void setProduct(OpenAccountProduct product) {
        this.product = product;
    }

    public List<BasicInfoAccountPurposeView> getBasicInfoAccountPurposeView() {
        return basicInfoAccountPurposeView;
    }

    public void setBasicInfoAccountPurposeView(List<BasicInfoAccountPurposeView> basicInfoAccountPurposeView) {
        this.basicInfoAccountPurposeView = basicInfoAccountPurposeView;
    }

    public String getPurposeForShow() {
        return purposeForShow;
    }

    public void setPurposeForShow(String purposeForShow) {
        this.purposeForShow = purposeForShow;
    }
}
