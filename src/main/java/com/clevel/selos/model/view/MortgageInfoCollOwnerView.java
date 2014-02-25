package com.clevel.selos.model.view;

import java.io.Serializable;

public class MortgageInfoCollOwnerView implements Serializable {
	private static final long serialVersionUID = -1830278203542716141L;
	private long id;
	private long customerId;
	private String customerName;
	private String citizenId;
	private String tmbCustomerId;
	private String relation;
	private boolean isPOA;
	private boolean canSelectPOA;
	private boolean isJuristic;
	
	public MortgageInfoCollOwnerView() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCitizenId() {
		return citizenId;
	}

	public void setCitizenId(String citizenId) {
		this.citizenId = citizenId;
	}

	public String getTmbCustomerId() {
		return tmbCustomerId;
	}

	public void setTmbCustomerId(String tmbCustomerId) {
		this.tmbCustomerId = tmbCustomerId;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public boolean isPOA() {
		return isPOA;
	}

	public void setPOA(boolean isPOA) {
		this.isPOA = isPOA;
	}

	public boolean isCanSelectPOA() {
		return canSelectPOA;
	}

	public void setCanSelectPOA(boolean canSelectPOA) {
		this.canSelectPOA = canSelectPOA;
	}
	
	public boolean isJuristic() {
		return isJuristic;
	}
	public void setJuristic(boolean isJuristic) {
		this.isJuristic = isJuristic;
	}
}
