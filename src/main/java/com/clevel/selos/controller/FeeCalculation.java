package com.clevel.selos.controller;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.primefaces.context.RequestContext;
import org.slf4j.Logger;

import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.view.FeeCalculationDetail;
import com.clevel.selos.util.FacesUtil;
import com.clevel.selos.util.Util;

@ViewScoped
@ManagedBean(name="feeCalculation")
public class FeeCalculation implements Serializable {

	private static final long serialVersionUID = -8238319076227615414L;

	@Inject @SELOS
	private Logger log;
	
	//Private variable
	private boolean preRenderCheck = false;
	private long workCaseId = -1;
	private long stepId = -1;
	private User user;
	
	//Property
	private List<FeeCalculationDetail> debitFromCustomerList;
	private List<FeeCalculationDetail> informCustomerList;
	private List<FeeCalculationDetail> penaltyFeeList;
	private BigDecimal totalDebitFromCustomer;
	private BigDecimal totalInformCustomer;
	private BigDecimal grandTotalAgreement;
	private BigDecimal totalPenaltyFee;
	
	
	public FeeCalculation() {
	}
	
	public List<FeeCalculationDetail> getDebitFromCustomerList() {
		return debitFromCustomerList;
	}
	public List<FeeCalculationDetail> getInformCustomerList() {
		return informCustomerList;
	}
	public List<FeeCalculationDetail> getPenaltyFeeList() {
		return penaltyFeeList;
	}
	public BigDecimal getTotalDebitFromCustomer() {
		return totalDebitFromCustomer;
	}
	public BigDecimal getGrandTotalAgreement() {
		return grandTotalAgreement;
	}
	public BigDecimal getTotalInformCustomer() {
		return totalInformCustomer;
	}
	public BigDecimal getTotalPenaltyFee() {
		return totalPenaltyFee;
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
			user = (User) session.getAttribute("user");
		}
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
	
	public void onSaveFeeCalculation() {
		
		RequestContext.getCurrentInstance().addCallbackParam("functionComplete", true);
	}
	
	/*
	 * Private method
	 */
	private void _loadInitData() {
		preRenderCheck = false;
		//Calculate fee
		debitFromCustomerList = new ArrayList<FeeCalculationDetail>();
		informCustomerList = new ArrayList<FeeCalculationDetail>();
		penaltyFeeList = new ArrayList<FeeCalculationDetail>();
		
		_retriveDebitFromCustomer();
		_retriveInformCustomer();
		_retrivePenaltyFee();
		
		totalDebitFromCustomer = _calculateTotal(debitFromCustomerList);
		totalInformCustomer = _calculateTotal(informCustomerList);
		totalPenaltyFee = _calculateTotal(penaltyFeeList);
		grandTotalAgreement = totalDebitFromCustomer.add(totalInformCustomer);
	}
	
	private void _retriveDebitFromCustomer() {
		FeeCalculationDetail detail = new FeeCalculationDetail();
		detail.setFeeType("Duty Stamp");
		detail.setFormula("");
		detail.setAmount(new BigDecimal(2550));
		debitFromCustomerList.add(detail);
		
		detail = new FeeCalculationDetail();
		detail.setFeeType("Front End Fee");
		detail.setFormula("วงเงินสินเชื่อ * 2/100 (แต่ละวงเงิน)");
		detail.setAmount(new BigDecimal(15000));
		debitFromCustomerList.add(detail);
		
		detail = new FeeCalculationDetail();
		detail.setFeeType("Mortgage Service");
		detail.setFormula("");
		detail.setAmount(new BigDecimal(15000));
		debitFromCustomerList.add(detail);
		
		detail = new FeeCalculationDetail();
		detail.setFeeType("Insurance");
		detail.setFormula("");
		detail.setAmount(new BigDecimal(15000));
		debitFromCustomerList.add(detail);
		
		detail = new FeeCalculationDetail();
		detail.setFeeType("BA ส่วนต่าง/PA");
		detail.setFormula("");
		detail.setAmount(new BigDecimal(15000));
		debitFromCustomerList.add(detail);
	}
	
	private void _retriveInformCustomer() {
		FeeCalculationDetail detail = new FeeCalculationDetail();
		detail.setFeeType("ค่าจำนองที่กรมที่ดิน");
		detail.setFormula("");
		detail.setAmount(new BigDecimal(100000));
		informCustomerList.add(detail);
		
		detail = new FeeCalculationDetail();
		detail.setFeeType("TCG Fee");
		detail.setFormula("");
		detail.setAmount(new BigDecimal(175000));
		informCustomerList.add(detail);
	}
	
	private void _retrivePenaltyFee() {
		FeeCalculationDetail detail = new FeeCalculationDetail();
		detail.setFeeType("Prepayment Fee");
		detail.setFormula("");
		detail.setAmount(new BigDecimal(0));
		penaltyFeeList.add(detail);
		
		detail = new FeeCalculationDetail();
		detail.setFeeType("Commitment Fee");
		detail.setFormula("");
		detail.setAmount(null);
		penaltyFeeList.add(detail);
		
		detail = new FeeCalculationDetail();
		detail.setFeeType("Extension Fee");
		detail.setFormula("");
		detail.setAmount(null);
		penaltyFeeList.add(detail);
		
		detail = new FeeCalculationDetail();
		detail.setFeeType("Cancellation Fee");
		detail.setFormula("");
		detail.setAmount(null);
		penaltyFeeList.add(detail);
	}
	
	private BigDecimal _calculateTotal(List<FeeCalculationDetail> list) {
		BigDecimal summ = new BigDecimal(0);
		for (FeeCalculationDetail detail : list) {
			if (detail.getAmount() == null)
				continue;
			summ = summ.add(detail.getAmount());
		}
		return summ;
	}
}
