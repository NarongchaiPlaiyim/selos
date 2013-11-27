package com.clevel.selos.controller;

import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.master.OpenAccountProductDAO;
import com.clevel.selos.dao.master.OpenAccountPurposeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BankAccountType;
import com.clevel.selos.model.db.master.OpenAccountProduct;
import com.clevel.selos.model.db.master.OpenAccountPurpose;
import com.clevel.selos.model.view.openaccount.AccountNameView;
import com.clevel.selos.model.view.openaccount.OpenAccountCreditTypeView;
import com.clevel.selos.model.view.openaccount.OpenAccountPurposeView;
import com.clevel.selos.model.view.openaccount.OpenAccountView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "openAccount")
public class OpenAccount {
    @Inject
    @SELOS
    Logger log;

    @Inject
    @NormalMessage
    Message msg;
    @Inject
    private OpenAccountProductDAO openAccountProductDAO;
    @Inject
    private BankAccountTypeDAO bankAccountTypeDAO;
    @Inject
    private OpenAccountPurposeDAO openAccountPurposeDAO;

    private List<OpenAccountPurpose> openAccountPurposeList;


    //*** View ***//
    private OpenAccountView openAccountView;

    //*** Drop down List ***//
    private List<BankAccountType> bankAccountTypeList;
    private List<OpenAccountProduct> openAccountProductList;
    private List<OpenAccountPurposeView> openAccountPurposeViewList;

    private List<String> accountName;

    //*** Account Name (Table) ***//
    private List<AccountNameView> accountNameViewList;
    private AccountNameView accountNameView;

    //*** Credit Type (Table) ***//
    private List<OpenAccountCreditTypeView> openAccountCreditTypeViewList;
    private OpenAccountCreditTypeView openAccountCreditTypeView;

    @Inject
    public OpenAccount() {

    }

    @PostConstruct
    public void onCreation(){
        openAccountView = new OpenAccountView();

        //Account Type
        bankAccountTypeList = bankAccountTypeDAO.findOpenAccountType();

        //Purpose
        openAccountPurposeList = openAccountPurposeDAO.findAll();
        openAccountPurposeViewList = new ArrayList<OpenAccountPurposeView>();
        for(OpenAccountPurpose openAccountPurpose : openAccountPurposeList) {
            OpenAccountPurposeView openAccountPurposeView = new OpenAccountPurposeView();
            openAccountPurposeView.setPurpose(openAccountPurpose);
            openAccountPurposeViewList.add(openAccountPurposeView);
        }

        //Account Name for test
        accountName = new ArrayList<String>();
        accountName.add("Name");
        accountName.add("Name2");
        accountName.add("Name3");
        accountName.add("Name4");
        accountName.add("Name5");

        //Account Name (Table) for test
        accountNameViewList = new ArrayList<AccountNameView>();
        openAccountView.setAccountNameViewList(accountNameViewList);



        //Credit Type (Table) for test
        openAccountCreditTypeViewList = new ArrayList<OpenAccountCreditTypeView>();

        openAccountCreditTypeView = new OpenAccountCreditTypeView();
        openAccountCreditTypeView.setProductProgram("ProductProgram");
        openAccountCreditTypeView.setCreditFacility("Loan");
        openAccountCreditTypeView.setLimit(new BigDecimal("99999"));
        openAccountCreditTypeViewList.add(openAccountCreditTypeView);

        openAccountCreditTypeView = new OpenAccountCreditTypeView();
        openAccountCreditTypeView.setProductProgram("ProductProgram2");
        openAccountCreditTypeView.setCreditFacility("OD");
        openAccountCreditTypeView.setLimit(new BigDecimal("99999999999999"));
        openAccountCreditTypeViewList.add(openAccountCreditTypeView);

        openAccountView.setOpenAccountCreditTypeViewList(openAccountCreditTypeViewList);
    }

    public void onChangeAccountType(){
        //Product
        openAccountProductList = openAccountProductDAO.findByBankAccountTypeId(openAccountView.getIdOfAccountType());
    }

    public void addAccountName(){
        //Add The Account Name from Account Name List
        accountNameViewList.add(new AccountNameView(openAccountView.getAccountName()));
    }

    public void removeAccountName() {
        //Remove The Account Name from Account Name List
        accountNameViewList.remove(accountNameView);
    }

    public List<OpenAccountPurpose> getOpenAccountPurposeList() {
        return openAccountPurposeList;
    }

    public List<OpenAccountPurposeView> getOpenAccountPurposeViewList() {
        return openAccountPurposeViewList;
    }

    public void setOpenAccountPurposeViewList(List<OpenAccountPurposeView> openAccountPurposeViewList) {
        this.openAccountPurposeViewList = openAccountPurposeViewList;
    }

    public void setOpenAccountPurposeList(List<OpenAccountPurpose> openAccountPurposeList) {
        this.openAccountPurposeList = openAccountPurposeList;
    }

    public OpenAccountView getOpenAccountView() {
        return openAccountView;
    }

    public void setOpenAccountView(OpenAccountView openAccountView) {
        this.openAccountView = openAccountView;
    }

    public List<BankAccountType> getBankAccountTypeList() {
        return bankAccountTypeList;
    }

    public void setBankAccountTypeList(List<BankAccountType> bankAccountTypeList) {
        this.bankAccountTypeList = bankAccountTypeList;
    }

    public List<OpenAccountProduct> getOpenAccountProductList() {
        return openAccountProductList;
    }

    public void setOpenAccountProductList(List<OpenAccountProduct> openAccountProductList) {
        this.openAccountProductList = openAccountProductList;
    }

    public List<String> getAccountName() {
        return accountName;
    }

    public void setAccountName(List<String> accountName) {
        this.accountName = accountName;
    }

    public AccountNameView getAccountNameView() {
        return accountNameView;
    }

    public void setAccountNameView(AccountNameView accountNameView) {
        this.accountNameView = accountNameView;
    }
}
