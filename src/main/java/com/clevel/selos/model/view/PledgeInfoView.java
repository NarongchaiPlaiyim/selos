package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.clevel.selos.model.db.master.User;

public class PledgeInfoView implements Serializable {
	private static final long serialVersionUID = 5548491394072353610L;
	private long id;
	private Date signingDate;
	private String pledgeType;
	private BigDecimal pledgeAmount;
	private String accountNo;
	
	private User modifyBy;
	private Date modifyDate;
	
	public PledgeInfoView() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getSigningDate() {
		return signingDate;
	}

	public void setSigningDate(Date signingDate) {
		this.signingDate = signingDate;
	}

	public String getPledgeType() {
		return pledgeType;
	}

	public void setPledgeType(String pledgeType) {
		this.pledgeType = pledgeType;
	}

	public BigDecimal getPledgeAmount() {
		return pledgeAmount;
	}

	public void setPledgeAmount(BigDecimal pledgeAmount) {
		this.pledgeAmount = pledgeAmount;
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
	
	public String getAccountNo() {
		return accountNo;
	}
	
	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
	}
	
}
