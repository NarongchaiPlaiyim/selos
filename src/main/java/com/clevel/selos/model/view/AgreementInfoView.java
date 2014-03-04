package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.Date;

import com.clevel.selos.model.MortgageSignLocationType;
import com.clevel.selos.model.RadioValue;

public class AgreementInfoView implements Serializable {
	private static final long serialVersionUID = -427693013067255002L;
	private long id;
	private Date loanContractDate;
	private MortgageSignLocationType signingLocation;
	private int updLocation;
	private String comsNumber;
	private Date firstPaymentDate;
	private int payDate;
	private RadioValue confirmed;
	private String remark;

	private String modifyUser;
	private Date modifyDate;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Date getLoanContractDate() {
		return loanContractDate;
	}
	public void setLoanContractDate(Date loanContractDate) {
		this.loanContractDate = loanContractDate;
	}
	public MortgageSignLocationType getSigningLocation() {
		if (signingLocation == null)
			signingLocation = MortgageSignLocationType.NA;
		return signingLocation;
	}
	public void setSigningLocation(MortgageSignLocationType signingLocation) {
		this.signingLocation = signingLocation;
	}
	
	public String getComsNumber() {
		return comsNumber;
	}
	public void setComsNumber(String comsNumber) {
		this.comsNumber = comsNumber;
	}
	public Date getFirstPaymentDate() {
		return firstPaymentDate;
	}
	public void setFirstPaymentDate(Date firstPaymentDate) {
		this.firstPaymentDate = firstPaymentDate;
	}
	public int getPayDate() {
		return payDate;
	}
	public void setPayDate(int payDate) {
		this.payDate = payDate;
	}
	public RadioValue getConfirmed() {
		if (confirmed == null)
			confirmed = RadioValue.NA;
		return confirmed;
	}
	public void setConfirmed(RadioValue confirmed) {
		this.confirmed = confirmed;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getModifyUser() {
		return modifyUser;
	}
	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public int getUpdLocation() {
		return updLocation;
	}
	public void setUpdLocation(int updLocation) {
		this.updLocation = updLocation;
	}
}
