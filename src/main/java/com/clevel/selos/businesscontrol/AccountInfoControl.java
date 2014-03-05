package com.clevel.selos.businesscontrol;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.master.BankAccountProductDAO;
import com.clevel.selos.dao.master.BankAccountPurposeDAO;
import com.clevel.selos.dao.master.BankAccountTypeDAO;
import com.clevel.selos.dao.master.BankBranchDAO;
import com.clevel.selos.dao.working.AccountInfoSummaryDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.ExistingCreditDetailDAO;
import com.clevel.selos.dao.working.NewCreditDetailDAO;
import com.clevel.selos.dao.working.OpenAccountCreditDAO;
import com.clevel.selos.dao.working.OpenAccountDAO;
import com.clevel.selos.dao.working.OpenAccountDepositDAO;
import com.clevel.selos.dao.working.OpenAccountNameDAO;
import com.clevel.selos.dao.working.OpenAccountPurposeDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.BankAccountProduct;
import com.clevel.selos.model.db.master.BankAccountPurpose;
import com.clevel.selos.model.db.master.BankAccountType;
import com.clevel.selos.model.db.master.BankBranch;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.AccountInfoSummary;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.NewCreditDetail;
import com.clevel.selos.model.db.working.OpenAccount;
import com.clevel.selos.model.db.working.OpenAccountCredit;
import com.clevel.selos.model.db.working.OpenAccountDeposit;
import com.clevel.selos.model.db.working.OpenAccountName;
import com.clevel.selos.model.db.working.OpenAccountPurpose;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.AccountInfoSummaryView;
import com.clevel.selos.model.view.MortgageSummaryView;
import com.clevel.selos.model.view.OpenAccountCreditView;
import com.clevel.selos.model.view.OpenAccountFullView;
import com.clevel.selos.model.view.OpenAccountNameView;
import com.clevel.selos.model.view.OpenAccountPurposeView;
import com.clevel.selos.transform.AccountInfoSummaryTransform;
import com.clevel.selos.transform.CreditDetailSimpleTransform;
import com.clevel.selos.transform.OpenAccountTransform;

@Stateless
public class AccountInfoControl extends BusinessControl implements Serializable {
    private static final long serialVersionUID = 7094364991471157346L;

	@Inject @SELOS
    private Logger log;
    
    @Inject private WorkCaseDAO workCaseDAO;
   
    @Inject private MortgageSummaryControl mortgageSummaryControl;
    
    @Inject private OpenAccountDAO openAccountDAO;
    @Inject private OpenAccountCreditDAO openAccountCreditDAO;
	@Inject	private OpenAccountNameDAO openAccountNameDAO;
	@Inject	private OpenAccountPurposeDAO openAccountPurposeDAO;
	@Inject	private OpenAccountDepositDAO openAccountDepositDAO;

    @Inject private AccountInfoSummaryDAO accountInfoSummaryDAO;
    
    @Inject private OpenAccountTransform openAccountTransform;
    @Inject private AccountInfoSummaryTransform accountInfoSummaryTransform;
    
    @Inject private BankBranchDAO bankBranchDAO;
    @Inject private BankAccountTypeDAO bankAccountTypeDAO;
    @Inject private BankAccountProductDAO bankAccountProductDAO;
    @Inject private CustomerDAO customerDAO;
    @Inject private CreditDetailSimpleTransform creditDetailSimpleTransform;
    @Inject private NewCreditDetailDAO newCreditDetailDAO;
	@Inject	private ExistingCreditDetailDAO existingCreditDetailDAO;
    @Inject private BankAccountPurposeDAO bankAccountPurposeDAO;
    
    public void initialOpenAccount(long workCaseId) {
    	if (workCaseId <=0)
    		return;
    	//Checking that this workcase should calculate open account from pledge
    	MortgageSummaryView view = mortgageSummaryControl.getMortgageSummaryView(workCaseId);
    	if (view.getId() <= 0) {
    		//need to calculate
    		mortgageSummaryControl.calculateMortgageSummary(workCaseId);
    	}
    }
    public AccountInfoSummaryView getAccountInfoSummary(long workCaseId) {
    	AccountInfoSummary result = null;
    	try {
    		if (workCaseId > 0)
    			result = accountInfoSummaryDAO.findByWorkCase(workCaseId);
    	} catch (Throwable e) {}
    	return accountInfoSummaryTransform.transformToView(result);
    }
    public List<OpenAccountFullView> getAccountInfoList(long workCaseId) {
    	if (workCaseId <= 0)
    		return Collections.emptyList();
    	
    	List<OpenAccount> models = openAccountDAO.findByWorkCaseId(workCaseId);
    	
    	List<OpenAccountFullView> rtnDatas = new ArrayList<OpenAccountFullView>();
    	for (OpenAccount model : models) {
    		OpenAccountFullView view = openAccountTransform.transformToFullView(model);
    		
    		//Credits
        	List<OpenAccountCreditView> creditViews = new ArrayList<OpenAccountCreditView>();
        	HashSet<Long> newCreditSet = new HashSet<Long>();
        	List<OpenAccountCredit> creditModels = model.getOpenAccountCreditList();
        	if (creditModels != null && !creditModels.isEmpty()) {
        		for (OpenAccountCredit creditModel : creditModels) {
        			OpenAccountCreditView creditView = new OpenAccountCreditView();
        			if (creditModel.getExistingCreditDetail() != null) {
        				creditDetailSimpleTransform.updateSimpleView(creditView, creditModel.getExistingCreditDetail());
        			} else if (creditModel.getNewCreditDetail() != null) {
        				creditDetailSimpleTransform.updateSimpleView(creditView, creditModel.getNewCreditDetail());
        				newCreditSet.add(creditModel.getNewCreditDetail().getId());
        			} else {
        				continue;
        			}
        			creditView.setOpenAccountCreditId(creditModel.getId());
        			creditView.setFromPledge(creditModel.isFromPledge());
        			creditView.setChecked(true);
        			creditViews.add(creditView);
        		}
        	}
        	//List from new credit detail
        	List<NewCreditDetail> newCreditModels = newCreditDetailDAO.findApprovedNewCreditDetail(model.getWorkCase().getId());
        	for (NewCreditDetail newCreditModel : newCreditModels) {
        		if (newCreditSet.contains(newCreditModel.getId()))
        			continue;
        		OpenAccountCreditView creditView = new OpenAccountCreditView();
        		creditDetailSimpleTransform.updateSimpleView(creditView, newCreditModel);
        		creditView.setOpenAccountCreditId(0);
    			creditView.setFromPledge(false);
    			creditView.setChecked(false);
    			creditViews.add(creditView);
        	}
        	Collections.sort(creditViews);
        	view.setCredits(creditViews);
        	
        	//Purpose
        	List<OpenAccountPurposeView> purposeViews = new ArrayList<OpenAccountPurposeView>();
        	HashMap<Long,OpenAccountPurpose> purposeMap = new HashMap<Long, OpenAccountPurpose>();
        	List<OpenAccountPurpose> purposeModels = model.getOpenAccountPurposeList();
        	if (purposeModels != null && !purposeModels.isEmpty()) {
        		for (OpenAccountPurpose purposeModel : purposeModels) {
        			purposeMap.put(purposeModel.getAccountPurpose().getId(), purposeModel);
        		}
        	}
        	List<BankAccountPurpose> purposeDataModels = bankAccountPurposeDAO.findActiveAll();
        	for (BankAccountPurpose purposeDataModel : purposeDataModels) {
        		OpenAccountPurposeView purposeView = new OpenAccountPurposeView();
        		purposeView.setPurposeId(purposeDataModel.getId());
        		purposeView.setPurpose(purposeDataModel.getName());
        		
        		OpenAccountPurpose purposeModel = purposeMap.get(purposeDataModel.getId());
        		if (purposeModel != null) {
        			purposeView.setId(purposeModel.getId());
        			purposeView.setChecked(true);
        		} else {
        			purposeView.setChecked(false);
        		}
        		if (model.isPledgeAccount() && purposeDataModel.isPledgeDefault()) {
        			purposeView.setChecked(true);
        			purposeView.setEditable(false);
        		} else {
        			purposeView.setEditable(true);
        		}
        		purposeViews.add(purposeView);
        	}
        	view.setPurposes(purposeViews);
        	
        	//Account Name
        	List<OpenAccountNameView> nameViews = new ArrayList<OpenAccountNameView>();
        	List<OpenAccountName> nameModels = model.getOpenAccountNameList();
        	for (OpenAccountName nameModel : nameModels) {
        		OpenAccountNameView nameView = new OpenAccountNameView();
        		nameView.setId(nameModel.getId());
        		nameView.setCustomerId(nameModel.getCustomer().getId());
        		nameView.setName(nameModel.getCustomer().getDisplayName());
        		nameView.setFromPledge(nameModel.isFromPledge());
        		nameViews.add(nameView);
        	}
        	view.setNames(nameViews);
        	
        	rtnDatas.add(view);
    	}
    	return rtnDatas;
    }
    
    public List<SelectItem> getBankBranches() {
    	List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
    	List<BankBranch> models = bankBranchDAO.findActiveAll();
    	for (BankBranch model : models) {
    		SelectItem item = new SelectItem();
    		item.setValue(model.getId());
    		item.setLabel(model.getName());
    		item.setDescription(model.getDescription());
    		rtnDatas.add(item);
    	}
    	return rtnDatas;
    }
    public List<SelectItem> getAccountTypes() {
    	List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
    	List<BankAccountType> models = bankAccountTypeDAO.findOpenAccountType();
    	for (BankAccountType model : models) {
    		SelectItem item = new SelectItem();
    		item.setValue(model.getId());
    		item.setLabel(model.getName());
    		item.setDescription(model.getDescription());
    		rtnDatas.add(item);
    	}
    	return rtnDatas;
    }
    public List<SelectItem> getProductTypes(int accountTypeId) {
    	if (accountTypeId <= 0)
    		return Collections.emptyList();
    	
    	List<SelectItem> rtnDatas = new ArrayList<SelectItem>();
    	List<BankAccountProduct> models = bankAccountProductDAO.findByBankAccountTypeId(accountTypeId);
    	for (BankAccountProduct model : models) {
    		SelectItem item = new SelectItem();
    		item.setValue(model.getId());
    		item.setLabel(model.getName());
    		item.setDescription(model.getName());
    		rtnDatas.add(item);
    	}
    	return rtnDatas;
    }
    
    public List<OpenAccountNameView> getAllAccountNames(long workCaseId) {
    	if (workCaseId <= 0)
    		return Collections.emptyList();
    	List<OpenAccountNameView> rtnDatas = new ArrayList<OpenAccountNameView>();
    	List<Customer> models = customerDAO.findByWorkCaseId(workCaseId);
    	for (Customer model : models) {
    		OpenAccountNameView view= new OpenAccountNameView();
    		view.setId(0);
    		view.setFromPledge(false);
    		view.setCustomerId(model.getId());
    		view.setName(model.getDisplayName());
    		rtnDatas.add(view);
    	}
    	return rtnDatas;
    }
    public List<OpenAccountPurposeView> getAllAccountPurposes() {
    	List<BankAccountPurpose> purposes = bankAccountPurposeDAO.findActiveAll();
    	List<OpenAccountPurposeView> rtnDatas = new ArrayList<OpenAccountPurposeView>();
    	for (BankAccountPurpose purpose : purposes) {
    		OpenAccountPurposeView view = new OpenAccountPurposeView();
    		view.setPurposeId(purpose.getId());
    		view.setPurpose(purpose.getName());
    		view.setChecked(false);
    		view.setEditable(true);
    		rtnDatas.add(view);
    	}
    	return rtnDatas; 
    }
    public List<OpenAccountCreditView> getAllAccountCredits(long workCaseId) {
    	if (workCaseId <= 0)
    		return Collections.emptyList();
    	List<OpenAccountCreditView> rtnDatas = new ArrayList<OpenAccountCreditView>();
    	List<NewCreditDetail> credits = newCreditDetailDAO.findApprovedNewCreditDetail(workCaseId);
    	for (NewCreditDetail credit : credits) {
    		OpenAccountCreditView creditView = new OpenAccountCreditView();
    		creditDetailSimpleTransform.updateSimpleView(creditView, credit);
    		creditView.setOpenAccountCreditId(0);
			creditView.setFromPledge(false);
			creditView.setChecked(false);
			rtnDatas.add(creditView);
    	}
    	return rtnDatas;
    }
   
    
	
	public void saveAccountInfo(long workCaseId, AccountInfoSummaryView summaryView, List<OpenAccountFullView> openAccountViews,
			List<OpenAccountFullView> deleteList) {
		if (workCaseId <= 0)
			return;
		User user = getCurrentUser();
		WorkCase workCase = workCaseDAO.findRefById(workCaseId);

		// Save Account Info summary
		if (summaryView.getId() > 0) {
			AccountInfoSummary summaryModel = accountInfoSummaryDAO.findById(summaryView.getId());
			accountInfoSummaryTransform.updateModelFromView(summaryModel, summaryView, user);
			accountInfoSummaryDAO.persist(summaryModel);
		} else {
			AccountInfoSummary summaryModel = accountInfoSummaryTransform.transformToNewModel(summaryView, user, workCase);
			accountInfoSummaryDAO.save(summaryModel);
		}

		// Process Open Account

		for (OpenAccountFullView view : openAccountViews) {
			boolean isNew = false;
			OpenAccount model = null;
			if (view.getId() <= 0) { // new
				isNew = true;

				model = new OpenAccount();
				model.setWorkCase(workCase);
				model.setNumberOfDep(0);
				model.setPledgeAccount(false);
				model.setOpenAccountDepositList(new ArrayList<OpenAccountDeposit>());
				model.setOpenAccountCreditList(new ArrayList<OpenAccountCredit>());
				model.setOpenAccountNameList(new ArrayList<OpenAccountName>());
				model.setOpenAccountPurposeList(new ArrayList<OpenAccountPurpose>());
			} else {
				if (!view.isNeedUpdate())
					continue;
				model = openAccountDAO.findById(view.getId());
			}

			model.setRequestType(view.getRequestAccountType());
			model.setAccountNumber(view.getAccountNo());
			if (view.getBranchId() > 0)
				model.setBankBranch(bankBranchDAO.findRefById(view.getBranchId()));
			if (view.getAccountTypeId() > 0)
				model.setBankAccountType(bankAccountTypeDAO.findRefById(view.getAccountTypeId()));
			if (view.getProductTypeId() > 0)
				model.setBankAccountProduct(bankAccountProductDAO.findRefById(view.getProductTypeId()));
			model.setTerm(view.getTerm());
			model.setConfirmOpenAccount(view.getConfirmAccountType());

			// process credit
			HashMap<Long, OpenAccountCredit> creditMap = new HashMap<Long, OpenAccountCredit>();
			for (OpenAccountCredit creditModel : model.getOpenAccountCreditList()) {
				creditMap.put(creditModel.getId(), creditModel);
			}
			List<OpenAccountCreditView> creditViews = view.getCredits();
			for (OpenAccountCreditView creditView : creditViews) {
				if (creditView.getOpenAccountCreditId() > 0) {
					if (creditView.isChecked()) // do nothing
						continue;
					// remove
					OpenAccountCredit creditModel = creditMap.get(creditView.getOpenAccountCreditId());
					if (creditModel == null)
						continue;
					model.getOpenAccountCreditList().remove(creditModel);
					creditModel.setOpenAccount(null);
					openAccountCreditDAO.delete(creditModel);
				} else {
					if (!creditView.isChecked()) // do nothing
						continue;
					// Add
					OpenAccountCredit creditModel = new OpenAccountCredit();
					creditModel.setFromPledge(false);
					creditModel.setOpenAccount(model);
					if (creditView.isNewCredit())
						creditModel.setNewCreditDetail(newCreditDetailDAO.findRefById(creditView.getId()));
					else
						creditModel.setExistingCreditDetail(existingCreditDetailDAO.findRefById(creditView.getId()));
					model.getOpenAccountCreditList().add(creditModel);
				}
			}

			// process purpose
			HashMap<Long, OpenAccountPurpose> purposeMap = new HashMap<Long, OpenAccountPurpose>();
			for (OpenAccountPurpose purposeModel : model.getOpenAccountPurposeList()) {
				purposeMap.put(purposeModel.getId(),purposeModel);
			}
			List<OpenAccountPurposeView> purposeViews = view.getPurposes();
			for (OpenAccountPurposeView purposeView : purposeViews) {
				if (purposeView.getId() > 0) {
					if (purposeView.isChecked()) //do nothing
						continue;
					//remove
					OpenAccountPurpose purposeModel = purposeMap.get(purposeView.getId());
					if (purposeModel == null)
						continue;
					model.getOpenAccountPurposeList().remove(purposeModel);
					purposeModel.setOpenAccount(null);
					openAccountPurposeDAO.delete(purposeModel);
				} else {
					if (!purposeView.isChecked())
						continue;
					//Add
					OpenAccountPurpose purposeModel = new OpenAccountPurpose();
					purposeModel.setOpenAccount(model);
					purposeModel.setAccountPurpose(bankAccountPurposeDAO.findRefById(purposeView.getPurposeId()));
					model.getOpenAccountPurposeList().add(purposeModel);
				}
			}
			
			// process name
			HashMap<Long, OpenAccountName> nameMap = new HashMap<Long, OpenAccountName>();
			for (OpenAccountName nameModel : model.getOpenAccountNameList()) {
				nameMap.put(nameModel.getId(), nameModel);
			}
			List<OpenAccountNameView> nameViews = view.getNames();
			for (OpenAccountNameView nameView : nameViews) {
				if (nameView.getId() > 0) //do nothing
					continue;
				OpenAccountName nameModel = new OpenAccountName();
				nameModel.setFromPledge(false);
				nameModel.setOpenAccount(model);
				nameModel.setCustomer(customerDAO.findRefById(nameView.getCustomerId()));
				model.getOpenAccountNameList().add(nameModel);
			}
			List<OpenAccountNameView> deleteNameViews = view.getDeleteNames();
			for (OpenAccountNameView deleteView : deleteNameViews) {
				if (deleteView.getId() <= 0)
					continue;
				OpenAccountName nameModel = nameMap.get(deleteView.getId());
				if (nameModel == null)
					continue;
				model.getOpenAccountNameList().remove(nameModel);
				nameModel.setOpenAccount(null);
				openAccountNameDAO.delete(nameModel);
			}
			
			if (isNew) {
				openAccountDAO.save(model);
			} else {
				openAccountDAO.persist(model);
			}
		}
		
		//Delete
		for (OpenAccountFullView delete : deleteList) {
			if (delete.getId() <= 0)
				continue;
			OpenAccount model = openAccountDAO.findById(delete.getId());
			if (model == null)
				continue;
			openAccountCreditDAO.delete(model.getOpenAccountCreditList());
			openAccountNameDAO.delete(model.getOpenAccountNameList());
			openAccountPurposeDAO.delete(model.getOpenAccountPurposeList());
			openAccountDepositDAO.delete(model.getOpenAccountDepositList());
			openAccountDAO.delete(model);
		}
	}
}
