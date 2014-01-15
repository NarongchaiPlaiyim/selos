package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AccountInfoDetailView implements Serializable {
    //Request Account Type
    private int reqAccountType;
    private String reqAccountTypeForShow;

    //Account Number
    private String accountNumber;
    private String accountNumberForShow;

    //Branch
    private AccountInfoBranchView branchView;

    //Account Type
    private AccountInfoAccountTypeView accountTypeView;

    //Product Type
    private AccountInfoProductTypeView productTypeView;

    //term
    private String term;
    private String termForShow;

    //Account Name
    private List<AccountNameView> accountNameViewList;
    private AccountNameView accountNameViewSelected;

    //Purpose
    private List<AccountInfoPurposeView> accountInfoPurposeViewList;

    //Open Account
    private int openAccount;

    //Credit Type
    private List<AccountInfoCreditTypeView> accountInfoCreditTypeViewList;
    public AccountInfoDetailView(){
        init();
    }

    public void reset(){
        init();
    }

    private void init(){
        branchView = new AccountInfoBranchView();
        accountTypeView = new AccountInfoAccountTypeView();
        productTypeView = new AccountInfoProductTypeView();
        accountNameViewList = new ArrayList<AccountNameView>();
        accountInfoCreditTypeViewList = new ArrayList<AccountInfoCreditTypeView>();
        accountNameViewSelected = new AccountNameView();
    }

    public int getReqAccountType() {
        return reqAccountType;
    }

    public void setReqAccountType(int reqAccountType) {
        this.reqAccountType = reqAccountType;
    }

    public String getReqAccountTypeForShow() {
        return reqAccountTypeForShow;
    }

    public void setReqAccountTypeForShow(String reqAccountTypeForShow) {
        this.reqAccountTypeForShow = reqAccountTypeForShow;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountInfoBranchView getBranchView() {
        return branchView;
    }

    public void setBranchView(AccountInfoBranchView branchView) {
        this.branchView = branchView;
    }

    public AccountInfoAccountTypeView getAccountTypeView() {
        return accountTypeView;
    }

    public void setAccountTypeView(AccountInfoAccountTypeView accountTypeView) {
        this.accountTypeView = accountTypeView;
    }

    public AccountInfoProductTypeView getProductTypeView() {
        return productTypeView;
    }

    public void setProductTypeView(AccountInfoProductTypeView productTypeView) {
        this.productTypeView = productTypeView;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public List<AccountNameView> getAccountNameViewList() {
        return accountNameViewList;
    }

    public void setAccountNameViewList(List<AccountNameView> accountNameViewList) {
        this.accountNameViewList = accountNameViewList;
    }

    public List<AccountInfoPurposeView> getAccountInfoPurposeViewList() {
        return accountInfoPurposeViewList;
    }

    public void setAccountInfoPurposeViewList(List<AccountInfoPurposeView> accountInfoPurposeViewList) {
        this.accountInfoPurposeViewList = accountInfoPurposeViewList;
    }

    public List<AccountInfoCreditTypeView> getAccountInfoCreditTypeViewList() {
        return accountInfoCreditTypeViewList;
    }

    public void setAccountInfoCreditTypeViewList(List<AccountInfoCreditTypeView> accountInfoCreditTypeViewList) {
        this.accountInfoCreditTypeViewList = accountInfoCreditTypeViewList;
    }

    public AccountNameView getAccountNameViewSelected() {
        return accountNameViewSelected;
    }

    public void setAccountNameViewSelected(AccountNameView accountNameViewSelected) {
        this.accountNameViewSelected = accountNameViewSelected;
    }

    public String getAccountNumberForShow() {
        return accountNumberForShow;
    }

    public void setAccountNumberForShow(String accountNumberForShow) {
        this.accountNumberForShow = accountNumberForShow;
    }

    public String getTermForShow() {
        return termForShow;
    }

    public void setTermForShow(String termForShow) {
        this.termForShow = termForShow;
    }

    public int getOpenAccount() {
        return openAccount;
    }

    public void setOpenAccount(int openAccount) {
        this.openAccount = openAccount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("accountInfoCreditTypeViewList", accountInfoCreditTypeViewList)
                .append("reqAccountType", reqAccountType)
                .append("reqAccountTypeForShow", reqAccountTypeForShow)
                .append("accountNumber", accountNumber)
                .append("branchView", branchView)
                .append("accountTypeView", accountTypeView)
                .append("productTypeView", productTypeView)
                .append("term", term)
                .append("accountNameViewList", accountNameViewList)
                .append("accountInfoPurposeViewList", accountInfoPurposeViewList)
                .toString();
    }
}
