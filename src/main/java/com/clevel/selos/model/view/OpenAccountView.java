package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.BankAccountProduct;
import com.clevel.selos.model.view.master.BankAccountProductView;
import com.clevel.selos.model.view.master.BankAccountPurposeView;
import com.clevel.selos.model.view.master.BankAccountTypeView;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OpenAccountView implements Serializable {
    private long id;
    private String accountName;
    private List<CustomerInfoView> accountNameList;
    private BankAccountTypeView bankAccountTypeView;
    private BankAccountProductView bankAccountProductView;
    private List<BankAccountPurposeView> bankAccountPurposeView;
    private String purposeForShow;

    public OpenAccountView() {
        reset();
    }

    public void reset() {
        this.accountNameList = new ArrayList<CustomerInfoView>();
        this.bankAccountTypeView = new BankAccountTypeView();
        this.bankAccountProductView = new BankAccountProductView();
        this.bankAccountPurposeView = new ArrayList<BankAccountPurposeView>();
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

    public BankAccountProductView getBankAccountProductView() {
        return bankAccountProductView;
    }

    public void setBankAccountProductView(BankAccountProductView bankAccountProductView) {
        this.bankAccountProductView = bankAccountProductView;
    }

    public List<BankAccountPurposeView> getBankAccountPurposeView() {
        return bankAccountPurposeView;
    }

    public void setBankAccountPurposeView(List<BankAccountPurposeView> bankAccountPurposeView) {
        this.bankAccountPurposeView = bankAccountPurposeView;
    }

    public String getPurposeForShow() {
        return purposeForShow;
    }

    public void setPurposeForShow(String purposeForShow) {
        this.purposeForShow = purposeForShow;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("accountName", accountName)
                .append("accountNameList", accountNameList)
                .append("bankAccountTypeView", bankAccountTypeView)
                .append("bankAccountProductView", bankAccountProductView)
                .append("bankAccountPurposeView", bankAccountPurposeView)
                .append("purposeForShow", purposeForShow)
                .toString();
    }
}
