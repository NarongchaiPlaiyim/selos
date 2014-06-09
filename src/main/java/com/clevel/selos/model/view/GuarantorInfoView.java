package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class GuarantorInfoView implements Serializable {
	private static final long serialVersionUID = -6571658410554052874L;
	
	private long id;
	private String guarantorName;
	private Date signingDate;
	private String guarantorType;
	private BigDecimal guarantorAmount;
	
	private long workCaseId;
	private User modifyBy;
	private Date modifyDate;
	
	public GuarantorInfoView() {
		
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

	public String getGuarantorType() {
		return guarantorType;
	}

	public void setGuarantorType(String guarantorType) {
		this.guarantorType = guarantorType;
	}

	public BigDecimal getGuarantorAmount() {
		return guarantorAmount;
	}

	public void setGuarantorAmount(BigDecimal guarantorAmount) {
		this.guarantorAmount = guarantorAmount;
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
	public String getGuarantorName() {
		return guarantorName;
	}
	public void setGuarantorName(String guarantorName) {
		this.guarantorName = guarantorName;
	}
	public long getWorkCaseId() {
		return workCaseId;
	}
	public void setWorkCaseId(long workCaseId) {
		this.workCaseId = workCaseId;
	}
}
