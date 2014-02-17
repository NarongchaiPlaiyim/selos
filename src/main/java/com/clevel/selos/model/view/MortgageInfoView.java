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
	private long osCompanyId;
	private long landOfficeId;
	private String mortgageType;
	private int mortgageOrder;
	private BigDecimal mortgageAmount;
	
	private RadioValue poa;
	private AttorneyRelationType attorneyRelation;
	
	private User modifyBy;
	private Date modifyDate;
	
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

	public long getOsCompanyId() {
		return osCompanyId;
	}

	public void setOsCompanyId(long osCompanyId) {
		this.osCompanyId = osCompanyId;
	}

	public long getLandOfficeId() {
		return landOfficeId;
	}

	public void setLandOfficeId(long landOfficeId) {
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
		return poa;
	}

	public void setPoa(RadioValue poa) {
		this.poa = poa;
	}

	public AttorneyRelationType getAttorneyRelation() {
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
	
}
