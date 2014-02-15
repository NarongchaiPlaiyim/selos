package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.InsuranceCompany;
import com.clevel.selos.model.db.master.User;

public class BAPAInfoView implements Serializable {
	private static final long serialVersionUID = -8964123899265413304L;

	private long id;
	private RadioValue applyBA;
	private RadioValue payToInsuranceCompany;
	private User modifyBy;
	private Date modifyDate;
	private InsuranceCompany insuranceCompany;
	
	private int updInsuranceCompany;
	
	private BigDecimal totalLimit;
	private BigDecimal totalPremium;
    private BigDecimal totalExpense;
	
	public BAPAInfoView() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public RadioValue getApplyBA() {
		return applyBA;
	}

	public void setApplyBA(RadioValue applyBA) {
		this.applyBA = applyBA;
	}

	public RadioValue getPayToInsuranceCompany() {
		return payToInsuranceCompany;
	}

	public void setPayToInsuranceCompany(RadioValue payToInsuranceCompany) {
		this.payToInsuranceCompany = payToInsuranceCompany;
	}

	public User getModifyBy() {
		return modifyBy;
	}

	public void setModifyBy(User modifyBy) {
		this.modifyBy = modifyBy;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public InsuranceCompany getInsuranceCompany() {
		return insuranceCompany;
	}

	public void setInsuranceCompany(InsuranceCompany insuranceCompany) {
		this.insuranceCompany = insuranceCompany;
	}

	public int getUpdInsuranceCompany() {
		return updInsuranceCompany;
	}

	public void setUpdInsuranceCompany(int updInsuranceCompany) {
		this.updInsuranceCompany = updInsuranceCompany;
	}

	public BigDecimal getTotalLimit() {
		return totalLimit;
	}

	public void setTotalLimit(BigDecimal totalLimit) {
		this.totalLimit = totalLimit;
	}

	public BigDecimal getTotalPremium() {
		return totalPremium;
	}

	public void setTotalPremium(BigDecimal totalPremium) {
		this.totalPremium = totalPremium;
	}

	public BigDecimal getTotalExpense() {
		return totalExpense;
	}

	public void setTotalExpense(BigDecimal totalExpense) {
		this.totalExpense = totalExpense;
	}
	
	
}
