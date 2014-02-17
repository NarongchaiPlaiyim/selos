package com.clevel.selos.model.view;

import java.io.Serializable;

public class MortgageInfoAttorneySelectView implements Serializable {
	private static final long serialVersionUID = 3281627929136469566L;
	private long customerId;
	private CustomerAttorneyView attorneyDetail;
	private String customerName;
	private String citizenId;
	private String tmbCustomerId;
	private String relation;
	
	public MortgageInfoAttorneySelectView() {
		
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public CustomerAttorneyView getAttorneyDetail() {
		return attorneyDetail;
	}

	public void setAttorneyDetail(CustomerAttorneyView attorneyDetail) {
		this.attorneyDetail = attorneyDetail;
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
	
}
