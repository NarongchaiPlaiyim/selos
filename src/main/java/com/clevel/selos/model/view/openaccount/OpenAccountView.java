package com.clevel.selos.model.view.openaccount;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OpenAccountView implements Serializable {
    private int idOfAccountType;
    private String productType;
    private int requestAccountType;
    private String accountName;
    private String accountNumber;
    private String term;

    private List<AccountNameView> accountNameViewList;
    private List<OpenAccountCreditTypeView> openAccountCreditTypeViewList;

    public OpenAccountView() {
        onCreation();
    }

    private void onCreation() {
//        accountNameViewList = new ArrayList<AccountNameView>();
    }

    public int getRequestAccountType() {
        return requestAccountType;
    }

    public void setRequestAccountType(int requestAccountType) {
        this.requestAccountType = requestAccountType;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getIdOfAccountType() {
        return idOfAccountType;
    }

    public void setIdOfAccountType(int idOfAccountType) {
        this.idOfAccountType = idOfAccountType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public List<AccountNameView> getAccountNameViewList() {
        return accountNameViewList;
    }

    public void setAccountNameViewList(List<AccountNameView> accountNameViewList) {
        this.accountNameViewList = accountNameViewList;
    }

    public List<OpenAccountCreditTypeView> getOpenAccountCreditTypeViewList() {
        return openAccountCreditTypeViewList;
    }

    public void setOpenAccountCreditTypeViewList(List<OpenAccountCreditTypeView> openAccountCreditTypeViewList) {
        this.openAccountCreditTypeViewList = openAccountCreditTypeViewList;
    }
}
