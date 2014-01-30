package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.BankAccountProduct;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BasicInfoAccountView implements Serializable {
    private long id;
    private String accountName;
    private List<CustomerInfoView> accountNameList;
    private BankAccountTypeView bankAccountTypeView;
    private BankAccountProduct bankAccountProduct;
    private List<BasicInfoAccountPurposeView> basicInfoAccountPurposeView;
    private String purposeForShow;

    public BasicInfoAccountView() {
        reset();
    }

    public void reset() {
        this.accountNameList = new ArrayList<CustomerInfoView>();
        this.bankAccountTypeView = new BankAccountTypeView();
        this.bankAccountProduct = new BankAccountProduct();
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

    public List<CustomerInfoView> getAccountNameList() {
        return accountNameList;
    }

    public void setAccountNameList(List<CustomerInfoView> accountNameList) {
        this.accountNameList = accountNameList;
    }

    public BankAccountTypeView getBankAccountTypeView() {
        return bankAccountTypeView;
    }

    public void setBankAccountTypeView(BankAccountTypeView bankAccountTypeView) {
        this.bankAccountTypeView = bankAccountTypeView;
    }

    public BankAccountProduct getBankAccountProduct() {
        return bankAccountProduct;
    }

    public void setBankAccountProduct(BankAccountProduct bankAccountProduct) {
        this.bankAccountProduct = bankAccountProduct;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("accountName", accountName).
                append("accountNameList", accountNameList).
                append("bankAccountTypeView", bankAccountTypeView).
                append("bankAccountProduct", bankAccountProduct).
                append("basicInfoAccountPurposeView", basicInfoAccountPurposeView).
                append("purposeForShow", purposeForShow).
                toString();
    }
}
