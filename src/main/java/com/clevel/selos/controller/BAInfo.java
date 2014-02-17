package com.clevel.selos.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.primefaces.model.SelectableDataModel;
import org.slf4j.Logger;

import com.clevel.selos.businesscontrol.BAPAInfoControl;
import com.clevel.selos.businesscontrol.BasicInfoControl;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.ApproveType;
import com.clevel.selos.model.BAPAType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.BAResultHC;
import com.clevel.selos.model.db.master.InsuranceCompany;
import com.clevel.selos.model.view.BAPAInfoCreditToSelectView;
import com.clevel.selos.model.view.BAPAInfoCreditView;
import com.clevel.selos.model.view.BAPAInfoCustomerView;
import com.clevel.selos.model.view.BAPAInfoView;
import com.clevel.selos.model.view.BasicInfoView;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

@ViewScoped
@ManagedBean(name="baInfo")
public class BAInfo implements Serializable {
	private static final long serialVersionUID = -6678163555513002049L;

	@Inject @SELOS
	private Logger log;
	
	@Inject
	private BasicInfoControl basicInfoControl;
	
	@Inject
	private BAPAInfoControl bapaInfoControl;
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private BasicInfoView basicInfoView;
	private List<BAPAInfoCreditView> deleteCreditList;
	private List<BAPAInfoCreditToSelectView> toSelectCredits;
	
	//Property
	private CreditSelectedDataModel creditDataModel;
	private BAPAInfoView bapaInfoView;
	private List<InsuranceCompany> insuranceCompanies;
	private List<BAResultHC> baResultHCs;
	
	private List<BAPAInfoCustomerView> bapaInfoCustomers;
	private List<BAPAInfoCreditView> bapaInfoCredits;
	private BAPAInfoCustomerView bapaInfoCustomerView;
	private int deleteCreditRowId;
	private BAPAInfoCreditView bapaInfoCreditView;
	private boolean addCreditDialog;
	private BAPAInfoCreditToSelectView selectedCredit;
	
	private BigDecimal totalLimit;
	private BigDecimal totalPremium;
	private BigDecimal totalExpense;
	
	public BAInfo() {
	}
	
	public Date getLastUpdateDateTime() {
		return bapaInfoView.getModifyDate();
	}
	public String getLastUpdateBy() {
		if (bapaInfoView.getModifyBy() == null)
			return "";
		else
			return bapaInfoView.getModifyBy().getDisplayName();
	}
	public ApproveType getApproveType() {
		if (basicInfoView == null)
			return ApproveType.NA;
		else
			return basicInfoView.getApproveType();
	}
	public String getMinCheckDate() {
		SimpleDateFormat dFmt = new SimpleDateFormat("dd/MM/yyyy",new Locale("th", "TH"));
		return dFmt.format(new Date());
	}
	public String getMaxCheckDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, 90);
		SimpleDateFormat dFmt = new SimpleDateFormat("dd/MM/yyyy",new Locale("th", "TH"));
		return dFmt.format(calendar.getTime());
	}
	
	public BAPAInfoView getBapaInfoView() {
		return bapaInfoView;
	}
	public List<InsuranceCompany> getInsuranceCompanies() {
		return insuranceCompanies;
	}
	public List<BAResultHC> getBaResultHCs() {
		return baResultHCs;
	}
	public String getInsuranceAccount() {
		if (bapaInfoView.getUpdInsuranceCompany() > 0) {
			for (InsuranceCompany company : insuranceCompanies) {
				if (company.getId() == bapaInfoView.getUpdInsuranceCompany())
					return company.getAccountNumber();
			}
		}
		return null;
	}
	public List<BAPAInfoCustomerView> getBapaInfoCustomers() {
		return bapaInfoCustomers;
	}
	
	public boolean canUpdateBAInfoTable() {
		return RadioValue.YES.equals(bapaInfoView.getApplyBA());
	}
	public BAPAInfoCustomerView getBapaInfoCustomerView() {
		return bapaInfoCustomerView;
	}
	public void setBapaInfoCustomerView(BAPAInfoCustomerView bapaInfoCustomerView) {
		this.bapaInfoCustomerView = bapaInfoCustomerView;
	}
	public String getDisplayResultHealthCheck(BAPAInfoCustomerView view) {
		if (view == null || view.getUpdBAResultHC() <= 0 || baResultHCs == null)
			return "";
		for (BAResultHC hc : baResultHCs) {
			if (hc.getId() == view.getUpdBAResultHC())
				return hc.getName();
		}
		return "";
	}
	public List<BAPAInfoCreditView> getBapaInfoCredits() {
		return bapaInfoCredits;
	}
	public BAPAInfoCreditView getBapaInfoCreditView() {
		return bapaInfoCreditView;
	}
	
	public boolean isAddCreditDialog() {
		return addCreditDialog;
	}
	public int getDeleteCreditRowId() {
		return deleteCreditRowId;
	}
	public void setDeleteCreditRowId(int deleteCreditRowId) {
		this.deleteCreditRowId = deleteCreditRowId;
	}
	public BigDecimal getTotalExpense() {
		return totalExpense;
	}
	public BigDecimal getTotalLimit() {
		return totalLimit;
	}
	public BigDecimal getTotalPremium() {
		return totalPremium;
	}
	public CreditSelectedDataModel getCreditDataModel() {
		return creditDataModel;
	}
	public BAPAInfoCreditToSelectView getSelectedCredit() {
		return selectedCredit;
	}
	public void setSelectedCredit(BAPAInfoCreditToSelectView selectedCredit) {
		this.selectedCredit = selectedCredit;
	}
	/*
	 * Action
	 */
	@PostConstruct
	private void init() {
		log.info("Construct");
		HttpSession session = FacesUtil.getSession(false);
		if (session != null) {
			workCaseId = Util.parseLong(session.getAttribute("workCaseId"), -1);
			stepId = Util.parseLong(session.getAttribute("stepId"), -1);
		}
		insuranceCompanies = bapaInfoControl.getInsuranceCompanies();
		baResultHCs = bapaInfoControl.getBAResultHCs();
		toSelectCredits = bapaInfoControl.getBAPAInfoCreditToSelectView(workCaseId);
		creditDataModel = new CreditSelectedDataModel();
		creditDataModel.setWrappedData(toSelectCredits);
		_loadInitData();
	}
	
	public void preRender() {
		if (preRenderCheck)
			return;
		preRenderCheck = true;
		
		String redirectPage = null;
		if (workCaseId > 0) {
			//TODO Validate step 
			if (stepId <= 0) {
				redirectPage = "/site/inbox.jsf";
			} else {
				return;
			}
		}
		try {
			if (redirectPage == null) {
				redirectPage = "/site/inbox.jsf";
			}
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(ec.getRequestContextPath()+redirectPage);
		} catch (IOException e) {
			log.error("Fail to redirect screen to "+redirectPage,e);
		}
	}
	public void onChangeApplyToBA() {
		//DO NOTHING
	}
	public void onOpenApplyInformationDialog(BAPAInfoCustomerView selected) {
		bapaInfoCustomerView = selected;
		
	}
	public void onApplyBAInformation() {
		bapaInfoCustomerView.setNeedUpdate(true);
		bapaInfoCustomerView = null;
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onOpenAddBAPAInformationDialog() {
		bapaInfoCreditView = new BAPAInfoCreditView();
		bapaInfoCreditView.setId(0);
		bapaInfoCreditView.setType(BAPAType.NA);
		bapaInfoCreditView.setPayByCustomer(true);
		bapaInfoCreditView.setFromCredit(false);
		bapaInfoCreditView.setLimit(new BigDecimal(0));
		
		
		selectedCredit = null;
		addCreditDialog = true;
	}
	public void onAddBAPAInformation() {
		if (selectedCredit != null) {
			//fill in credit info
			bapaInfoCreditView.setCreditId(selectedCredit.getId());
			bapaInfoCreditView.setProductProgram(selectedCredit.getProductProgram());
			bapaInfoCreditView.setCreditType(selectedCredit.getCreditType());
			bapaInfoCreditView.setLoanPurpose(selectedCredit.getLoanPurpose());
		} else {
			bapaInfoCreditView.setProductProgram("-");
			bapaInfoCreditView.setCreditType("-");
			bapaInfoCreditView.setLoanPurpose("-");
		}
		bapaInfoCredits.add(bapaInfoCreditView);
		bapaInfoCreditView = null;
		selectedCredit = null;
		
		_calculateTotal();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onOpenUpdateBAPAInformaionDialog(BAPAInfoCreditView selected) {
		log.info("Open Update BAPA INfo "+selected);
		bapaInfoCreditView = selected;
		long creditId = bapaInfoCreditView.getCreditId();
		selectedCredit = null;
		if (creditId > 0) {
			for (BAPAInfoCreditToSelectView credit : toSelectCredits) {
				if (credit.getId() == creditId) {
					selectedCredit = credit;
					break;
				}
			}
		}
		addCreditDialog = false;
	}
	public void onUpdateBAPAInformation() {
		if (!bapaInfoCreditView.isFromCredit()) {
			if (selectedCredit != null) {
				bapaInfoCreditView.setCreditId(selectedCredit.getId());
				bapaInfoCreditView.setProductProgram(selectedCredit.getProductProgram());
				bapaInfoCreditView.setCreditType(selectedCredit.getCreditType());
				bapaInfoCreditView.setLoanPurpose(selectedCredit.getLoanPurpose());
			} else {
				bapaInfoCreditView.setCreditId(0);
				bapaInfoCreditView.setProductProgram("-");
				bapaInfoCreditView.setCreditType("-");
				bapaInfoCreditView.setLoanPurpose("-");
			}
		}
		
		bapaInfoCreditView.setNeedUpdate(true);
		bapaInfoCreditView = null;
		selectedCredit = null;
		
		_calculateTotal();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onDeleteBAPAInformation() {
		if (deleteCreditRowId < 0 || deleteCreditRowId > bapaInfoCredits.size())
			return;
		BAPAInfoCreditView delete  = bapaInfoCredits.remove(deleteCreditRowId);
		if (delete != null) {
			deleteCreditList.add(delete);
		}
		deleteCreditRowId = -1;
		
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	public void onSaveBAInformation() {
		bapaInfoView.setTotalExpense(totalExpense);
		bapaInfoView.setTotalPremium(totalPremium);
		bapaInfoView.setTotalLimit(totalLimit);
		
		bapaInfoControl.saveBAPAInfo(workCaseId, bapaInfoView, bapaInfoCustomers, bapaInfoCredits, deleteCreditList);
		
		_loadInitData();
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	/*
	 * Private method
	 */
	private void _loadInitData() {
		preRenderCheck = false;
		bapaInfoView = bapaInfoControl.getBAPAInfoView(workCaseId);
		
		if (workCaseId > 0) {
			basicInfoView = basicInfoControl.getBasicInfo(workCaseId);
			bapaInfoCustomers = bapaInfoControl.getBAPAInfoCustomerView(workCaseId, bapaInfoView.getId());
			bapaInfoCredits = new ArrayList<BAPAInfoCreditView>(bapaInfoControl.getBAPAInfoCreditView(workCaseId, bapaInfoView.getId()));
		} else {
			bapaInfoCredits = new ArrayList<BAPAInfoCreditView>();
		}
		bapaInfoCustomerView = null;
		bapaInfoCreditView = null;
		deleteCreditList = new ArrayList<BAPAInfoCreditView>();
		deleteCreditRowId = -1;
		
		_calculateTotal();
	}
	private void _calculateTotal() {
		totalExpense = new BigDecimal(0);
		totalPremium = new BigDecimal(0);
		totalLimit = new BigDecimal(0);
		
		if (bapaInfoCredits == null)
			return;
		for (BAPAInfoCreditView credit : bapaInfoCredits) {
			if (credit.getExpenseAmount() != null)
				totalExpense = totalExpense.add(credit.getExpenseAmount());
			if (credit.getPremiumAmount() != null)
				totalPremium = totalPremium.add(credit.getPremiumAmount());
			if (credit.getLimit() != null)
				totalLimit = totalLimit.add(credit.getLimit());
		}
	}
	
	public class CreditSelectedDataModel extends ListDataModel<BAPAInfoCreditToSelectView> implements SelectableDataModel<BAPAInfoCreditToSelectView> {
		public CreditSelectedDataModel() {
			
		}
		@SuppressWarnings("unchecked")
		@Override
		public BAPAInfoCreditToSelectView getRowData(String key) {
			long id = Util.parseLong(key, -1);
			if (id <= 0)
				return null;
			List<BAPAInfoCreditToSelectView> datas = (List<BAPAInfoCreditToSelectView>)getWrappedData();
			if (datas == null || datas.isEmpty())
				return null;
			for (BAPAInfoCreditToSelectView data : datas) {
				if (data.getId() == id)
					return data;
			}
			return null;
		}
		@Override
		public Object getRowKey(BAPAInfoCreditToSelectView data) {
			return data.getId();
		}
		
	}
}


