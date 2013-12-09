package com.clevel.selos.model.view;

import com.clevel.selos.model.view.openaccount.AccountNameView;
//import com.clevel.selos.model.view.openaccount.OpenAccountCreditTypeView;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

public class OpenAccountView {
    private int idOfAccountType;
    private String productType;
    private int requestAccountType;
    private String accountName;
    private String accountNumber;
    private String term;

    private List<AccountNameView> accountNameViewList;
//    private List<OpenAccountCreditTypeView> openAccountCreditTypeViewList; //todo : change this , AS ( Comment OpenAccountCreditTypeView )
    private List<OpenAccountPurposeView> openAccountPurposeViewList;

    public OpenAccountView(){
        reset();
    }

    public void reset(){
        this.accountName = "";
        this.accountNumber = "";
        this.idOfAccountType = 0;
        this.productType = "";
        this.term = "";
        this.accountNameViewList = new ArrayList<AccountNameView>();
//        this.openAccountCreditTypeViewList = new ArrayList<OpenAccountCreditTypeView>();
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

    public int getRequestAccountType() {
        return requestAccountType;
    }

    public void setRequestAccountType(int requestAccountType) {
        this.requestAccountType = requestAccountType;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    public List<AccountNameView> getAccountNameViewList() {
        return accountNameViewList;
    }

    public void setAccountNameViewList(List<AccountNameView> accountNameViewList) {
        this.accountNameViewList = accountNameViewList;
    }

//    public List<OpenAccountCreditTypeView> getOpenAccountCreditTypeViewList() {
//        return openAccountCreditTypeViewList;
//    }
//
//    public void setOpenAccountCreditTypeViewList(List<OpenAccountCreditTypeView> openAccountCreditTypeViewList) {
//        this.openAccountCreditTypeViewList = openAccountCreditTypeViewList;
//    }

    public List<OpenAccountPurposeView> getOpenAccountPurposeViewList() {
        return openAccountPurposeViewList;
    }

    public void setOpenAccountPurposeViewList(List<OpenAccountPurposeView> openAccountPurposeViewList) {
        this.openAccountPurposeViewList = openAccountPurposeViewList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("idOfAccountType", idOfAccountType)
                .append("productType", productType)
                .append("requestAccountType", requestAccountType)
                .append("accountName", accountName)
                .append("accountNumber", accountNumber)
                .append("term", term)
                .append("accountNameViewList", accountNameViewList)
//                .append("openAccountCreditTypeViewList", openAccountCreditTypeViewList)
                .toString();
    }
}
