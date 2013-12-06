package com.clevel.selos.model.view.openaccount;

import com.clevel.selos.model.view.openaccount.model.AccountNameModel;
import com.clevel.selos.model.view.openaccount.model.CreditTypeModel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OpenAccountView implements Serializable {
    private AccountDetailInformationView accountDetailInformationView;
    private String accountName;
    private AccountNameModel accountNameModel;
    private List<CreditTypeModel> selectedCreditTypeList;

    public OpenAccountView() {
        init();
    }

    private void init(){
        accountDetailInformationView = new AccountDetailInformationView();
    }

    public AccountDetailInformationView getAccountDetailInformationView() {
        return accountDetailInformationView;
    }

    public void setAccountDetailInformationView(AccountDetailInformationView accountDetailInformationView) {
        this.accountDetailInformationView = accountDetailInformationView;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public AccountNameModel getAccountNameModel() {
        return accountNameModel;
    }

    public void setAccountNameModel(AccountNameModel accountNameModel) {
        this.accountNameModel = accountNameModel;
    }

    public List<CreditTypeModel> getSelectedCreditTypeList() {
        return selectedCreditTypeList;
    }

    public void setSelectedCreditTypeList(List<CreditTypeModel> selectedCreditTypeList) {
        this.selectedCreditTypeList = selectedCreditTypeList;
    }
}
