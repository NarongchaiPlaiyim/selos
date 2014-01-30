package com.clevel.selos.transform;

import com.clevel.selos.dao.master.AccountProductDAO;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.model.db.master.AccountProduct;
import com.clevel.selos.model.db.master.AccountPurpose;
import com.clevel.selos.model.db.master.BankAccountType;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.BankAccountTypeView;
import com.clevel.selos.model.view.BasicInfoAccountPurposeView;
import com.clevel.selos.model.view.BasicInfoAccountView;
import com.clevel.selos.model.view.CustomerInfoView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class BasicInfoAccountTransform extends Transform {
    @Inject
    BankAccountTypeDAO bankAccountTypeDAO;
    @Inject
    AccountProductDAO accountProductDAO;
    @Inject
    CustomerDAO customerDAO;

    @Inject
    BankAccountTypeTransform bankAccountTypeTransform;

    @Inject
    public BasicInfoAccountTransform() {
    }

    public OpenAccount transformToModel(BasicInfoAccountView basicInfoAccountView, WorkCase workCase) {
        OpenAccount openAccount = new OpenAccount();

        openAccount.setWorkCase(workCase);
        BankAccountType bankaccountType = bankAccountTypeDAO.findById(basicInfoAccountView.getBankAccountTypeView().getId());
        if(bankaccountType != null){
            openAccount.setBankAccountType(bankaccountType);
        } else {
            openAccount.setBankAccountType(null);
        }

        AccountProduct accountProduct = accountProductDAO.findById(basicInfoAccountView.getAccountProduct().getId());
        if(accountProduct != null){
            openAccount.setAccountProduct(accountProduct);
        } else {
            openAccount.setAccountProduct(null);
        }

        if(basicInfoAccountView.getAccountNameList() != null && basicInfoAccountView.getAccountNameList().size() > 0){
            List<OpenAccountName> openAccountNameList = new ArrayList<OpenAccountName>();
            for (CustomerInfoView civ : basicInfoAccountView.getAccountNameList()){
                OpenAccountName openAccountName = new OpenAccountName();
                Customer customer = customerDAO.findById(civ.getId());
                openAccountName.setCustomer(customer);
                openAccountNameList.add(openAccountName);
            }
            openAccount.setOpenAccountNameList(openAccountNameList);
        } else {
            openAccount.setOpenAccountNameList(null);
        }

        if(basicInfoAccountView.getBasicInfoAccountPurposeView() != null && basicInfoAccountView.getBasicInfoAccountPurposeView().size() > 0){
            List<OpenAccountPurpose> openAccountPurposeList = new ArrayList<OpenAccountPurpose>();
            for (BasicInfoAccountPurposeView bpv : basicInfoAccountView.getBasicInfoAccountPurposeView()){
            }
            openAccount.setOpenAccountPurposeList(openAccountPurposeList);
        } else {
            openAccount.setOpenAccountPurposeList(null);
        }

//        private int requestType;
//        private String accountNumber;
//        private BankBranch bankBranch;
//        private String term;
//        private int numberOfDep;
//        private List<OpenAccountDeposit> openAccountDepositList;
//        private int confirmOpenAccount;
//        private List<OpenAccountCredit> openAccountCreditList;


        return openAccount;
    }

    public BasicInfoAccountView transformToView(OpenAccount openAccount) {
        BasicInfoAccountView basicInfoAccountView = new BasicInfoAccountView();

//        basicInfoAccountView.setId(openAccount.getId());
//
//        basicInfoAccountView.setAccountName(openAccount.getAccountName());
//
//        basicInfoAccountView.setProduct(openAccount.getAccountProduct());
        /*if (basicInfoAccountView.getProduct() == null) {
            basicInfoAccountView.setProduct(new AccountProduct());
            basicInfoAccountView.getProduct().setName("-"); // for view
        }

//        basicInfoAccountView.setBankAccountTypeView(bankAccountTypeTransform.getBankAccountTypeView(openAccount.getBankAccountType()));
        if (basicInfoAccountView.getBankAccountTypeView() == null) {
            basicInfoAccountView.setBankAccountTypeView(new BankAccountTypeView());
            basicInfoAccountView.getBankAccountTypeView().setName("-"); // for view
        }*/

        BasicInfoAccPurposeTransform basicInfoAccPurposeTransform = new BasicInfoAccPurposeTransform();
//        List<BasicInfoAccountPurposeView> basicInfoAccountPurposeViews = basicInfoAccPurposeTransform.transformToViewList(openAccount.getOpenAccPurposeList());

//        basicInfoAccountView.setBasicInfoAccountPurposeView(basicInfoAccountPurposeViews);

        //for show view
        if(basicInfoAccountView.getBasicInfoAccountPurposeView().size() > 0){
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < basicInfoAccountView.getBasicInfoAccountPurposeView().size(); i++) {
                if (i == 0) {
                    stringBuilder.append(basicInfoAccountView.getBasicInfoAccountPurposeView().get(i).getPurpose().getName());
                } else {
                    stringBuilder.append(", " + basicInfoAccountView.getBasicInfoAccountPurposeView().get(i).getPurpose().getName());
                }
            }
            basicInfoAccountView.setPurposeForShow(stringBuilder.toString());
        } else {
            basicInfoAccountView.setPurposeForShow("-"); // for view
        }

        return basicInfoAccountView;
    }

    public List<BasicInfoAccountView> transformToViewList(List<OpenAccount> openAccountList) {
        List<BasicInfoAccountView> basicInfoAccountViews = new ArrayList<BasicInfoAccountView>();
        if (openAccountList != null) {
            for (OpenAccount op : openAccountList) {
                BasicInfoAccountView basicInfoAccountView = transformToView(op);
                basicInfoAccountViews.add(basicInfoAccountView);
            }
        }
        return basicInfoAccountViews;
    }
}
