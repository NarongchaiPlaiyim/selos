package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.clevel.selos.model.AttorneyRelationType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.User;

public class MortgageInfoView implements Serializable {
	private static final long serialVersionUID = 3331548097385786737L;
	private long id;
	private Date signingDate;
	private int osCompanyId;
	private int landOfficeId;
	private String mortgageType;
	private int mortgageOrder;
	private BigDecimal mortgageAmount;
	
	private RadioValue poa;
	private AttorneyRelationType attorneyRelation;
	
	private User modifyBy;
	private Date modifyDate;
	
	private long customerAttorneyId;
	
	public MortgageInfoView() {
		
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

	public int getOsCompanyId() {
		return osCompanyId;
	}

	public void setOsCompanyId(int osCompanyId) {
		this.osCompanyId = osCompanyId;
	}

	public int getLandOfficeId() {
		return landOfficeId;
	}

	public void setLandOfficeId(int landOfficeId) {
		this.landOfficeId = landOfficeId;
	}

	public String getMortgageType() {
		return mortgageType;
	}

	public void setMortgageType(String mortgageType) {
		this.mortgageType = mortgageType;
	}

	public int getMortgageOrder() {
		return mortgageOrder;
	}

	public void setMortgageOrder(int mortgageOrder) {
		this.mortgageOrder = mortgageOrder;
	}

	public BigDecimal getMortgageAmount() {
		return mortgageAmount;
	}

	public void setMortgageAmount(BigDecimal mortgageAmount) {
		this.mortgageAmount = mortgageAmount;
	}

	public RadioValue getPoa() {
		if (poa == null)
			return RadioValue.NA;
		else
			return poa;
	}

	public void setPoa(RadioValue poa) {
		this.poa = poa;
	}

	public AttorneyRelationType getAttorneyRelation() {
		if (attorneyRelation == null)
			return AttorneyRelationType.NA;
		else
			return attorneyRelation;
	}

	public void setAttorneyRelation(AttorneyRelationType attorneyRelation) {
		this.attorneyRelation = attorneyRelation;
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
	public long getCustomerAttorneyId() {
		return customerAttorneyId;
	}
	public void setCustomerAttorneyId(long customerAttorneyId) {
		this.customerAttorneyId = customerAttorneyId;
	}
}
