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
    private AccountInfoView accountInfoViewSelected;
    private List<AccountInfoView> accountInfoViewList;
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
//        accountDetailInformationViewList = new ArrayList<AccountDetailInformationView>();
    }

    public void onAddAccountDetail(){
        modeForButton = ModeForButton.ADD;
        log.debug("Mode : {}", modeForButton);
        init();
    }

    public void onChangeAccountType(){
//        int id = openAccountView.getAccountDetailInformationView().getAccountType().getId();
        //Product
//        openAccountProductList = openAccountProductDAO.findByBankAccountTypeId(id);
    }

    public void addAccountName(){
        //Add The Account Name from Account Name List
//        AccountNameModel accountNameModel = new AccountNameModel();
//        accountNameModel.setName(openAccountView.getAccountName());
//
//        openAccountView.getAccountDetailInformationView().getAccountNameModelList().add(accountNameModel);
    }

//    public void addAccountDetail(){
//        int id = 0;
//        String value = null;
//        StringBuilder stringBuilder = null;
//        List<AccountNameModel> accountNameModelList;
//
//        //Request Account Type
//        id = openAccountView.getAccountDetailInformationView().getRequestAccountType().getId();
//        if(1 == id){
//            openAccountView.getAccountDetailInformationView().getRequestAccountType().setName("Existing");
//        } else {
//            openAccountView.getAccountDetailInformationView().getRequestAccountType().setName("New");
//        }
//
//        //Account Number
//        value = openAccountView.getAccountDetailInformationView().getAccountNumber();
//        if(value == null || "null".equalsIgnoreCase(value) || "".equals(value)){
//            openAccountView.getAccountDetailInformationView().setAccountNumber(" - ");
//        }
//
//        //Branch
//        id = openAccountView.getAccountDetailInformationView().getBranch().getId();
//        if(0 != id){
//            for (BranchModel branch : branchModelList){
//                if (branch.getId() == id){
//                    openAccountView.getAccountDetailInformationView().getBranch().setName(branch.getName());
//                }
//            }
//        }
//
//        //Account Name
//        accountNameModelList = openAccountView.getAccountDetailInformationView().getAccountNameModelList();
//        if(accountNameModelList.size() > 0){
//            stringBuilder = new StringBuilder();
//            for (AccountNameModel accountName : accountNameModelList){
//                stringBuilder.append(", ");
//                stringBuilder.append(accountName.getName());
//            }
//            String show = stringBuilder.toString();
//            if(show.length() > 1){
//                openAccountView.getAccountDetailInformationView().setAccountNameForShow(show.substring(2, show.length()));
//            } else {
//                openAccountView.getAccountDetailInformationView().setAccountNameForShow(" - ");
//            }
//        } else {
//            openAccountView.getAccountDetailInformationView().setAccountNameForShow(" - ");
//        }
//
//
//        id = openAccountView.getAccountDetailInformationView().getAccountType().getId();
//        if(0 != id){
//            for (BankAccountType accountType : bankAccountTypeList){
//                if (accountType.getId() == id){
//                    openAccountView.getAccountDetailInformationView().getAccountType().setName(accountType.getName());
//                }
//            }
//        }

//        //Product Type
//        id = openAccountView.getAccountDetailInformationView().getProductType().getId();
//        if(0 != id){
//            for (OpenAccountProduct productType : openAccountProductList){
//                if (productType.getId() == id){
//                    openAccountView.getAccountDetailInformationView().getProductType().setName(productType.getName());
//                }
//            }
//        }

        //Term
//        value = openAccountView.getAccountDetailInformationView().getTerm();
//        if(value == null || "null".equalsIgnoreCase(value) || "".equals(value)){
//            openAccountView.getAccountDetailInformationView().setTerm(" - ");
//        }

        //Purpose
//        purposeModelList = openAccountView.getAccountDetailInformationView().getPurposeModelList();
//        if(purposeModelList.size() > 0){
//            stringBuilder = new StringBuilder();
//            for (PurposeModel purpose : purposeModelList){
//                if(purpose.isSelected()){
//                    stringBuilder.append(", ");
//                    stringBuilder.append(purpose.getName());
//                }
//            }
//            String show = stringBuilder.toString();
//            if(show.length() > 1){
//                openAccountView.getAccountDetailInformationView().setPurposeForShow(show.substring(2, show.length()));
//            } else {
//                openAccountView.getAccountDetailInformationView().setPurposeForShow(" - ");
//            }
//        } else {
//            openAccountView.getAccountDetailInformationView().setPurposeForShow(" - ");
//        }
//
//        if(modeForButton != null && modeForButton.equals(ModeForButton.ADD)){
//            accountDetailInformationViewSelected = openAccountView.getAccountDetailInformationView();
//            accountDetailInformationViewList.add(accountDetailInformationViewSelected);
//        } else {
//            accountDetailInformationViewSelected = openAccountView.getAccountDetailInformationView();
//            accountDetailInformationViewList.set(rowIndex, accountDetailInformationViewSelected);
//        }

        boolean complete = true;
        RequestContext context = RequestContext.getCurrentInstance();
//        context.addCallbackParam("functionComplete", complete);

//    }

//    public void removeAccountName() {
//        //Remove The Account Name from Account Name List
//        openAccountView.getAccountDetailInformationView().getAccountNameModelList().remove(openAccountView.getAccountNameModel());
//    }
//
//    public void removeAccountDetail() {
//        //Remove The Account Detail from Account Detail List
//        accountDetailInformationViewList.remove(accountDetailInformationViewSelected);
//    }
//
//    public void editAccountDetail(){
//        modeForButton = ModeForButton.EDIT;
//        log.debug("Mode : {}", modeForButton);
//        //Edit Account Detail
//        openAccountView.setAccountDetailInformationView(accountDetailInformationViewSelected);
//    }
//

    private void init(){
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

//        //Account Name (Table) for test
//        accountNameViewList = new ArrayList<AccountNameModel>();
//
//        //Credit Type (Table) for test
//        openAccountCreditTypeViewList = new ArrayList<AccountInfoCreditTypeView>();
//
//        openAccountCreditTypeView = new CreditTypeModel();
//        openAccountCreditTypeView.setProductProgram("ProductProgram");
//        openAccountCreditTypeView.setCreditFacility("Loan");
//        openAccountCreditTypeView.setLimit(new BigDecimal("99999"));
//        openAccountCreditTypeViewList.add(openAccountCreditTypeView);
//
//        openAccountCreditTypeView = new CreditTypeModel();
//        openAccountCreditTypeView.setProductProgram("ProductProgram2");
//        openAccountCreditTypeView.setCreditFacility("OD");
//        openAccountCreditTypeView.setLimit(new BigDecimal("99999999999999"));
//        openAccountCreditTypeViewList.add(openAccountCreditTypeView);
//
//        openAccountView.getAccountDetailInformationView().setCreditTypeModelList(openAccountCreditTypeViewList);
    }
}
