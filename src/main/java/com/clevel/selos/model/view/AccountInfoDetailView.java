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

    //Branch
    private AccountInfoBranchView branchView;

    //Account Type
    private AccountInfoAccountTypeView accountTypeView;

    //Product Type
    private AccountInfoProductTypeView productTypeView;

    //term
    private String term;

    //Account Name
    private List<AccountNameView> accountNameViewList;
    private String accountNameViewListForShow;

    //Purpose
    private List<AccountInfoPurposeView> accountInfoPurposeViewList;
    private String accountInfoPurposeViewListForShow;

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

    public String getAccountNameViewListForShow() {
        return accountNameViewListForShow;
    }

    public void setAccountNameViewListForShow(String accountNameViewListForShow) {
        this.accountNameViewListForShow = accountNameViewListForShow;
    }

    public List<AccountInfoPurposeView> getAccountInfoPurposeViewList() {
        return accountInfoPurposeViewList;
    }

    public void setAccountInfoPurposeViewList(List<AccountInfoPurposeView> accountInfoPurposeViewList) {
        this.accountInfoPurposeViewList = accountInfoPurposeViewList;
    }

    public String getAccountInfoPurposeViewListForShow() {
        return accountInfoPurposeViewListForShow;
    }

    public void setAccountInfoPurposeViewListForShow(String accountInfoPurposeViewListForShow) {
        this.accountInfoPurposeViewListForShow = accountInfoPurposeViewListForShow;
    }

    public List<AccountInfoCreditTypeView> getAccountInfoCreditTypeViewList() {
        return accountInfoCreditTypeViewList;
    }

    public void setAccountInfoCreditTypeViewList(List<AccountInfoCreditTypeView> accountInfoCreditTypeViewList) {
        this.accountInfoCreditTypeViewList = accountInfoCreditTypeViewList;
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
                .append("accountNameViewListForShow", accountNameViewListForShow)
                .append("accountInfoPurposeViewList", accountInfoPurposeViewList)
                .append("accountInfoPurposeViewListForShow", accountInfoPurposeViewListForShow)
                .toString();
    }
}
