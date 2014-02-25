package com.clevel.selos.transform;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.clevel.selos.dao.master.BankAccountProductDAO;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.OpenAccountDAO;
import com.clevel.selos.model.BorrowerType;
import com.clevel.selos.model.db.master.BankAccountProduct;
import com.clevel.selos.model.db.master.BankAccountType;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.OpenAccount;
import com.clevel.selos.model.db.working.OpenAccountName;
import com.clevel.selos.model.db.working.OpenAccountPurpose;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.BankAccountPurposeView;
import com.clevel.selos.model.view.BankAccountTypeView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.model.view.OpenAccountFullView;
import com.clevel.selos.model.view.OpenAccountView;

public class OpenAccountTransform extends Transform {
    private static final long serialVersionUID = -709756394277635355L;
	@Inject
    BankAccountTypeDAO bankAccountTypeDAO;
    @Inject
    BankAccountProductDAO bankAccountProductDAO;
    @Inject
    CustomerDAO customerDAO;
    @Inject
    OpenAccountDAO openAccountDAO;

    @Inject
    BankAccountTypeTransform bankAccountTypeTransform;
    @Inject
    CustomerTransform customerTransform;

    public OpenAccountTransform() {
    }

    public OpenAccount transformToModel(OpenAccountView openAccountView, WorkCase workCase) {
        OpenAccount openAccount = new OpenAccount();

        if(openAccountView.getId() != 0){
            openAccount = openAccountDAO.findById(openAccountView.getId());
        }

        openAccount.setWorkCase(workCase);

        BankAccountType bankaccountType = null;
        if(openAccountView.getBankAccountTypeView() != null && openAccountView.getBankAccountTypeView().getId() != 0){
            bankaccountType = bankAccountTypeDAO.findById(openAccountView.getBankAccountTypeView().getId());
        }
        openAccount.setBankAccountType(bankaccountType);

        BankAccountProduct bankAccountProduct = null;
        if(openAccountView.getBankAccountProduct() != null && openAccountView.getBankAccountProduct().getId() != 0){
            bankAccountProduct = bankAccountProductDAO.findById(openAccountView.getBankAccountProduct().getId());
        }
        openAccount.setBankAccountProduct(bankAccountProduct);

        if(openAccountView.getAccountNameList() != null && openAccountView.getAccountNameList().size() > 0){
            List<OpenAccountName> openAccountNameList = new ArrayList<OpenAccountName>();
            for (CustomerInfoView civ : openAccountView.getAccountNameList()){
                OpenAccountName openAccountName = new OpenAccountName();
                Customer customer = customerDAO.findById(civ.getId());
                openAccountName.setCustomer(customer);
                openAccountNameList.add(openAccountName);
            }
            openAccount.setOpenAccountNameList(openAccountNameList);
        } else {
            openAccount.setOpenAccountNameList(null);
        }

        if(openAccountView.getBankAccountPurposeView() != null && openAccountView.getBankAccountPurposeView().size() > 0){
            List<OpenAccountPurpose> openAccountPurposeList = new ArrayList<OpenAccountPurpose>();
            for (BankAccountPurposeView bpv : openAccountView.getBankAccountPurposeView()){
                OpenAccountPurpose openAccountPurpose = new OpenAccountPurpose();
                openAccountPurpose.setAccountPurpose(bpv.getPurpose());
                openAccountPurposeList.add(openAccountPurpose);
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

    public OpenAccountView transformToView(OpenAccount openAccount) {
        OpenAccountView openAccountView = new OpenAccountView();

        openAccountView.setId(openAccount.getId());

        //for show view
        if(openAccount.getOpenAccountNameList() != null && openAccount.getOpenAccountNameList().size() > 0){
            StringBuilder accName = new StringBuilder();
            for (int i=0; i<openAccount.getOpenAccountNameList().size(); i++){
                if(openAccount.getOpenAccountNameList().size()-1 == i){
                    if(openAccount.getOpenAccountNameList().get(i).getCustomer().getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                        accName.append(openAccount.getOpenAccountNameList().get(i).getCustomer().getNameTh() + " " + openAccount.getOpenAccountNameList().get(i).getCustomer().getLastNameTh());
                    } else {
                        accName.append(openAccount.getOpenAccountNameList().get(i).getCustomer().getNameTh());
                    }
                } else {
                    if(openAccount.getOpenAccountNameList().get(i).getCustomer().getCustomerEntity().getId() == BorrowerType.INDIVIDUAL.value()){
                        accName.append(openAccount.getOpenAccountNameList().get(i).getCustomer().getNameTh() + " " + openAccount.getOpenAccountNameList().get(i).getCustomer().getLastNameTh() + ", ");
                    } else {
                        accName.append(openAccount.getOpenAccountNameList().get(i).getCustomer().getNameTh() + ", ");
                    }
                }
                openAccountView.getAccountNameList().add(customerTransform.transformToView(openAccount.getOpenAccountNameList().get(i).getCustomer()));
            }
            openAccountView.setAccountName(accName.toString());
        }

        openAccountView.setBankAccountProduct(openAccount.getBankAccountProduct());
        if (openAccountView.getBankAccountProduct() == null) {
            openAccountView.setBankAccountProduct(new BankAccountProduct());
            openAccountView.getBankAccountProduct().setName("-"); // for view
        }

        openAccountView.setBankAccountTypeView(bankAccountTypeTransform.getBankAccountTypeView(openAccount.getBankAccountType()));
        if (openAccountView.getBankAccountTypeView() == null) {
            openAccountView.setBankAccountTypeView(new BankAccountTypeView());
            openAccountView.getBankAccountTypeView().setName("-"); // for view
        }

        List<BankAccountPurposeView> bankAccountPurposeViewList = new ArrayList<BankAccountPurposeView>();
        if(openAccount.getOpenAccountPurposeList() != null && openAccount.getOpenAccountPurposeList().size() > 0){
            for (OpenAccountPurpose oap : openAccount.getOpenAccountPurposeList()){
                BankAccountPurposeView bankAccountPurposeView = new BankAccountPurposeView();
                bankAccountPurposeView.setPurpose(oap.getAccountPurpose());
                bankAccountPurposeView.setSelected(true);
                bankAccountPurposeViewList.add(bankAccountPurposeView);
            }
        }
        openAccountView.setBankAccountPurposeView(bankAccountPurposeViewList);

        //for show view
        if(openAccountView.getBankAccountPurposeView().size() > 0){
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < openAccountView.getBankAccountPurposeView().size(); i++) {
                if (i == 0) {
                    stringBuilder.append(openAccountView.getBankAccountPurposeView().get(i).getPurpose().getName());
                } else {
                    stringBuilder.append(", " + openAccountView.getBankAccountPurposeView().get(i).getPurpose().getName());
                }
            }
            openAccountView.setPurposeForShow(stringBuilder.toString());
        } else {
            openAccountView.setPurposeForShow("-"); // for view
        }

        return openAccountView;
    }

    public List<OpenAccountView> transformToViewList(List<OpenAccount> openAccountList) {
        List<OpenAccountView> openAccountViews = new ArrayList<OpenAccountView>();
        if (openAccountList != null) {
            for (OpenAccount op : openAccountList) {
                OpenAccountView openAccountView = transformToView(op);
                openAccountViews.add(openAccountView);
            }
        }
        return openAccountViews;
    }

    public List<OpenAccount> transformToModelList(List<OpenAccountView> openAccountViewList, WorkCase workCase) {
        List<OpenAccount> openAccounts = new ArrayList<OpenAccount>();
        if (openAccountViewList != null) {
            for (OpenAccountView opv : openAccountViewList) {
                OpenAccount openAccount = transformToModel(opv,workCase);
                openAccounts.add(openAccount);
            }
        }
        return openAccounts;
    }
    
    public OpenAccountFullView transformToFullView(OpenAccount model) {
    	OpenAccountFullView view = new OpenAccountFullView();
    	if (model == null)
    		return view;
    	view.setId(model.getId());
    	view.setRequestAccountType(model.getRequestType());
    	view.setAccountNo(model.getAccountNumber());
    	if (model.getBankBranch() != null) {
    		view.setBranchId(model.getBankBranch().getId());
    		view.setDisplayBranch(model.getBankBranch().getName());
    	}
    	if (model.getBankAccountType() != null) {
    		view.setAccountTypeId(model.getBankAccountType().getId());
    		view.setDisplayAccountType(model.getBankAccountType().getName());
    	}
    	if (model.getBankAccountProduct() != null) {
    		view.setProductTypeId(model.getBankAccountProduct().getId());
    		view.setDisplayProductType(model.getBankAccountProduct().getName());
    	}
    	
    	view.setTerm(model.getTerm());
    	view.setFromPledge(model.isPledgeAccount());
    	return view;
    }
}
