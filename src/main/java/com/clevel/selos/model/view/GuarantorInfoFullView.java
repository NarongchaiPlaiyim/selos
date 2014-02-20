package com.clevel.selos.model.view;

import java.util.List;

public class GuarantorInfoFullView extends GuarantorInfoView {
	private static final long serialVersionUID = -5839523036669309462L;
	private List<CreditDetailSimpleView> credits;
	private List<CustomerInfoSimpleView> customers;
	
	public GuarantorInfoFullView() {
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
