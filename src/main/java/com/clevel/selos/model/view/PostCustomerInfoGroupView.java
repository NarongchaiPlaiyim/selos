package com.clevel.selos.model.view;

import java.io.Serializable;
import java.util.List;

public class PostCustomerInfoGroupView implements Serializable {
	private static final long serialVersionUID = 47690074984512326L;
	private String titleKey;
	private List<CustomerInfoView> customers;
	
	public PostCustomerInfoGroupView() {
		
	}

	public String getTitleKey() {
		return titleKey;
	}

	public void setTitleKey(String titleKey) {
		this.titleKey = titleKey;
	}

	public List<CustomerInfoView> getCustomers() {
		return customers;
	}

	public void setCustomers(List<CustomerInfoView> customers) {
		this.customers = customers;
	}
	
}
