package com.clevel.selos.model.view;

import java.util.List;

public class PledgeInfoFullView extends PledgeInfoView {
	private static final long serialVersionUID = 1190780672380216212L;
	private List<CreditDetailSimpleView> credits;
	private List<CustomerInfoSimpleView> customers;
	
	public PledgeInfoFullView() {
		
	}

	public List<CreditDetailSimpleView> getCredits() {
		return credits;
	}

	public void setCredits(List<CreditDetailSimpleView> credits) {
		this.credits = credits;
	}

	public List<CustomerInfoSimpleView> getCustomers() {
		return customers;
	}

	public void setCustomers(List<CustomerInfoSimpleView> customers) {
		this.customers = customers;
	}
	
}
