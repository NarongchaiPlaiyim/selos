package com.clevel.selos.controller;

import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.master.OpenAccountProductDAO;
import com.clevel.selos.dao.master.OpenAccountPurposeDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BankAccountType;
import com.clevel.selos.model.db.master.OpenAccountProduct;
import com.clevel.selos.model.db.master.OpenAccountPurpose;
import com.clevel.selos.model.view.*;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "accountInfo")
public class AccountInfo implements Serializable {
    @Inject
    @SELOS
    Logger log;

    @Inject
    @NormalMessage
    Message msg;

    @Inject
    private OpenAccountProductDAO productTypeDAO;
    @Inject
    private BankAccountTypeDAO accountTypeDAO;
    @Inject
    private OpenAccountPurposeDAO purposeDAO;

    private List<OpenAccountPurpose> purposeList;

    enum ModeForButton{ ADD, EDIT }
    private ModeForButton modeForButton;
    private int rowIndex;

    //*** View ***//
    private AccountInfoView accountInfoView;
    private List<AccountInfoDetailView> accountInfoDetailViewList;
    private AccountInfoDetailView accountInfoDetailView;

    //*** Drop down List ***//
    private List<BankAccountType> accountTypeList;
    private List<OpenAccountProduct> productTypeList;
    private List<AccountInfoBranchView> branchList;

    //*** Check box ***//
    private List<AccountInfoPurposeView> purposeViewList;

    private List<String> accountName;

    //*** Account Name (Table) ***//
    private List<AccountNameView> accountNameViewList;
    private AccountNameView accountNameView;

    //*** Credit Type (Table) ***//
    private List<AccountInfoCreditTypeView> creditTypeViewList;
    private AccountInfoCreditTypeView creditTypeView;

    @Inject
    public AccountInfo() {

    }

    @PostConstruct
    public void onCreation(){
        init();
        accountInfoDetailViewList = new ArrayList<AccountInfoDetailView>();
    }

    public void onAddAccountDetail(){
        modeForButton = ModeForButton.ADD;
        init();
    }

    public void onChangeAccountType(){
        int id = (int) accountInfoView.getAccountInfoDetailViewSelected().getAccountTypeView().getId();
        productTypeList = productTypeDAO.findByBankAccountTypeId(id);
    }

    public void addAccountName(){
        accountNameView = new AccountNameView();
        accountNameView.setName(accountInfoView.getAccountInfoDetailViewSelected().getAccountNameViewSelected().getName());
        accountInfoView.getAccountInfoDetailViewSelected().getAccountNameViewList().add(accountNameView);
    }

    public void addAccountDetail(){
        int id = 0;
        long idLong = 0L;
        String value = null;
        StringBuilder stringBuilder = null;

        //Request Account Type
        id = accountInfoView.getAccountInfoDetailViewSelected().getReqAccountType();
        if(1 == id){
            accountInfoView.getAccountInfoDetailViewSelected().setReqAccountTypeForShow("Existing");
        } else {
            accountInfoView.getAccountInfoDetailViewSelected().setReqAccountTypeForShow("New");
        }

        //Account Number
        value = accountInfoView.getAccountInfoDetailViewSelected().getAccountNumber();
        if(value == null || "".equals(value)){
            accountInfoView.getAccountInfoDetailViewSelected().setAccountNumberForShow(" - ");
        } else {
            accountInfoView.getAccountInfoDetailViewSelected().setAccountNumberForShow(value);
        }

        //Branch
        idLong = accountInfoView.getAccountInfoDetailViewSelected().getBranchView().getId();
        if(0 != id){
            for (AccountInfoBranchView branchView : branchList){
                if (branchView.getId() == idLong){
                    accountInfoView.getAccountInfoDetailViewSelected().getBranchView().setName(branchView.getName());
                    break;
                }
            }
        }

        //Account Name
//        accountNameViewList = accountInfoView.getAccountInfoDetailViewSelected().getAccountNameViewList();
//        if(accountNameViewList.size() > 0){
//            stringBuilder = new StringBuilder();
//            for (AccountNameView accountNameView : accountNameViewList){
//                stringBuilder.append(", ");
//                stringBuilder.append(accountNameView.getName());
//            }
//            String show = stringBuilder.toString();
//            if(show.length() > 1){
//                accountInfoView.getAccountInfoDetailViewSelected().setAccountNameViewListForShow(show.substring(2, show.length()));
//            } else {
//                accountInfoView.getAccountInfoDetailViewSelected().setAccountNameViewListForShow(" - ");
//            }
//        } else {
//            accountInfoView.getAccountInfoDetailViewSelected().setAccountNameViewListForShow(" - ");
//        }
//
//        id = (int) accountInfoView.getAccountInfoDetailViewSelected().getAccountTypeView().getId();
//        if(0 != id){
//            for (BankAccountType accountType : accountTypeList){
//                if (accountType.getId() == id){
//                    accountInfoView.getAccountInfoDetailViewSelected().getAccountTypeView().setName(accountType.getName());
//                    break;
//                }
//            }
//        }

        //Product Type
        id = (int) accountInfoView.getAccountInfoDetailViewSelected().getProductTypeView().getId();
        if(0 != id){
            for (OpenAccountProduct productType : productTypeList){
                if (productType.getId() == id){
                    accountInfoView.getAccountInfoDetailViewSelected().getProductTypeView().setName(productType.getName());
                    break;
                }
            }
        }

        //Term
        value = accountInfoView.getAccountInfoDetailViewSelected().getTerm();
        if(value == null || "null".equalsIgnoreCase(value) || "".equals(value)){
            accountInfoView.getAccountInfoDetailViewSelected().setTermForShow(" - ");
        } else {
            accountInfoView.getAccountInfoDetailViewSelected().setTermForShow(value);
        }

        //Purpose
//        purposeViewList = accountInfoView.getAccountInfoDetailViewSelected().getAccountInfoPurposeViewList();
//        if(purposeViewList.size() > 0){
//            stringBuilder = new StringBuilder();
//            for (AccountInfoPurposeView purposeView : purposeViewList){
//                if(purposeView.isSelected()){
//                    stringBuilder.append(", ");
//                    stringBuilder.append(purposeView.getName());
//                }
//            }
//            String show = stringBuilder.toString();
//            if(show.length() > 1){
//                accountInfoView.getAccountInfoDetailViewSelected().setAccountInfoPurposeViewListForShow(show.substring(2, show.length()));
//            } else {
//                accountInfoView.getAccountInfoDetailViewSelected().setAccountInfoPurposeViewListForShow(" - ");
//            }
//        } else {
//            accountInfoView.getAccountInfoDetailViewSelected().setAccountInfoPurposeViewListForShow(" - ");
//        }

        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){
            accountInfoDetailView = accountInfoView.getAccountInfoDetailViewSelected();
            accountInfoDetailViewList.add(accountInfoDetailView);
            accountInfoView.setAccountInfoDetailViewList(accountInfoDetailViewList);
        } else {
            accountInfoDetailView = accountInfoView.getAccountInfoDetailViewSelected();
            accountInfoDetailViewList.set(rowIndex, accountInfoDetailView);
        }

        boolean complete = true;
        RequestContext context = RequestContext.getCurrentInstance();
        context.addCallbackParam("functionComplete", complete);

    }

    public void removeAccountName() {
        AccountNameView accountNameViewSelected = accountInfoView.getAccountInfoDetailViewSelected().getAccountNameViewSelected();
        accountInfoView.getAccountInfoDetailViewSelected().getAccountNameViewList().remove(accountNameViewSelected);
    }

    public void removeAccountDetail() {
        accountInfoDetailView = accountInfoView.getAccountInfoDetailViewSelected();
        accountInfoDetailViewList.remove(accountInfoDetailView);
    }

    public void editAccountDetail(){
        modeForButton = ModeForButton.EDIT;
        accountInfoDetailView = accountInfoView.getAccountInfoDetailViewSelected();
        accountInfoView.setAccountInfoDetailViewSelected(accountInfoDetailView);
    }


    private void init(){
        accountInfoView = new AccountInfoView();
        accountInfoDetailView = new AccountInfoDetailView();

        //branchModelList
        branchList = new ArrayList<AccountInfoBranchView>();
        AccountInfoBranchView branchView = null;

        branchView = new AccountInfoBranchView();
        branchView.setId(01);
        branchView.setName("Branch");
        branchList.add(branchView);
        branchView = new AccountInfoBranchView();
        branchView.setId(02);
        branchView.setName("Branch 2");
        branchList.add(branchView);

        //Account Type
        accountTypeList = accountTypeDAO.findOpenAccountType();

        //Purpose
        purposeList = purposeDAO.findAll();

        purposeViewList = new ArrayList<AccountInfoPurposeView>();
        AccountInfoPurposeView purposeView = null;
        for(OpenAccountPurpose purpose : purposeList){
            purposeView = new AccountInfoPurposeView();
            purposeView.setName(purpose.getName());
            purposeView.setId(purpose.getId());
            purposeViewList.add(purposeView);
        }
        accountInfoDetailView.setAccountInfoPurposeViewList(purposeViewList);

        //Account Name for test
        accountName = new ArrayList<String>();
        accountName.add("Mr. Ki mu ji");
        accountName.add("Mr. Sbay D");
        accountName.add("Mr. Kim ji");

        //Account Name (Table) for test
        accountNameViewList = new ArrayList<AccountNameView>();

        //Credit Type (Table) for test
        creditTypeViewList = new ArrayList<AccountInfoCreditTypeView>();
        creditTypeView = new AccountInfoCreditTypeView();
        creditTypeView.setProductProgram("ProductProgram");
        creditTypeView.setCreditFacility("Loan");
        creditTypeView.setLimit(new BigDecimal("99999"));
        creditTypeViewList.add(creditTypeView);
        creditTypeView = new AccountInfoCreditTypeView();
        creditTypeView.setProductProgram("ProductProgram2");
        creditTypeView.setCreditFacility("OD");
        creditTypeView.setLimit(new BigDecimal("99999999999999"));
        creditTypeViewList.add(creditTypeView);

        accountInfoDetailView.setAccountInfoCreditTypeViewList(creditTypeViewList);

        accountInfoView.setAccountInfoDetailViewSelected(accountInfoDetailView);

    }



    public OpenAccountProductDAO getProductTypeDAO() {
        return productTypeDAO;
    }

    public void setProductTypeDAO(OpenAccountProductDAO productTypeDAO) {
        this.productTypeDAO = productTypeDAO;
    }

    public BankAccountTypeDAO getAccountTypeDAO() {
        return accountTypeDAO;
    }

    public void setAccountTypeDAO(BankAccountTypeDAO accountTypeDAO) {
        this.accountTypeDAO = accountTypeDAO;
    }

    public OpenAccountPurposeDAO getPurposeDAO() {
        return purposeDAO;
    }

    public void setPurposeDAO(OpenAccountPurposeDAO purposeDAO) {
        this.purposeDAO = purposeDAO;
    }

    public List<OpenAccountPurpose> getPurposeList() {
        return purposeList;
    }

    public void setPurposeList(List<OpenAccountPurpose> purposeList) {
        this.purposeList = purposeList;
    }

    public ModeForButton getModeForButton() {
        return modeForButton;
    }

    public void setModeForButton(ModeForButton modeForButton) {
        this.modeForButton = modeForButton;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public AccountInfoView getAccountInfoView() {
        return accountInfoView;
    }

    public void setAccountInfoView(AccountInfoView accountInfoView) {
        this.accountInfoView = accountInfoView;
    }

    public List<AccountInfoDetailView> getAccountInfoDetailViewList() {
        return accountInfoDetailViewList;
    }

    public void setAccountInfoDetailViewList(List<AccountInfoDetailView> accountInfoDetailViewList) {
        this.accountInfoDetailViewList = accountInfoDetailViewList;
    }

    public AccountInfoDetailView getAccountInfoDetailView() {
        return accountInfoDetailView;
    }

    public void setAccountInfoDetailView(AccountInfoDetailView accountInfoDetailView) {
        this.accountInfoDetailView = accountInfoDetailView;
    }

    public List<BankAccountType> getAccountTypeList() {
        return accountTypeList;
    }

    public void setAccountTypeList(List<BankAccountType> accountTypeList) {
        this.accountTypeList = accountTypeList;
    }

    public List<OpenAccountProduct> getProductTypeList() {
        return productTypeList;
    }

    public void setProductTypeList(List<OpenAccountProduct> productTypeList) {
        this.productTypeList = productTypeList;
    }

    public List<AccountInfoBranchView> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<AccountInfoBranchView> branchList) {
        this.branchList = branchList;
    }

    public List<AccountInfoPurposeView> getPurposeViewList() {
        return purposeViewList;
    }

    public void setPurposeViewList(List<AccountInfoPurposeView> purposeViewList) {
        this.purposeViewList = purposeViewList;
    }

    public List<String> getAccountName() {
        return accountName;
    }

    public void setAccountName(List<String> accountName) {
        this.accountName = accountName;
    }

    public List<AccountNameView> getAccountNameViewList() {
        return accountNameViewList;
    }

    public void setAccountNameViewList(List<AccountNameView> accountNameViewList) {
        this.accountNameViewList = accountNameViewList;
    }

    public AccountNameView getAccountNameView() {
        return accountNameView;
    }

    public void setAccountNameView(AccountNameView accountNameView) {
        this.accountNameView = accountNameView;
    }

    public List<AccountInfoCreditTypeView> getCreditTypeViewList() {
        return creditTypeViewList;
    }

    public void setCreditTypeViewList(List<AccountInfoCreditTypeView> creditTypeViewList) {
        this.creditTypeViewList = creditTypeViewList;
    }

    public AccountInfoCreditTypeView getCreditTypeView() {
        return creditTypeView;
    }

    public void setCreditTypeView(AccountInfoCreditTypeView creditTypeView) {
        this.creditTypeView = creditTypeView;
    }
}
