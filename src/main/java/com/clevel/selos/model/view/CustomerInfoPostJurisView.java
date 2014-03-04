package com.clevel.selos.model.view;

import java.util.Date;

import com.clevel.selos.util.Util;

public class CustomerInfoPostJurisView extends CustomerInfoPostBaseView<CustomerInfoPostJurisView> {
	private static final long serialVersionUID = 7810126343864665205L;

	private long juristicId;
	
	private Date registrationDate;
	private String contactPerson;

	public CustomerInfoPostJurisView() {
		
	}

	public long getJuristicId() {
		return juristicId;
	}

	public void setJuristicId(long juristicId) {
		this.juristicId = juristicId;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}
	@Override
	protected void updateOwnValue(CustomerInfoPostJurisView view) {
		juristicId = view.juristicId;
		registrationDate = view.registrationDate;
		contactPerson = view.contactPerson;
	}
	@Override
	public void calculateAge() {
		if (registrationDate == null)
			age = 0;
		else
			age = Util.calAge(registrationDate);
	}
	@Override
	public int getDefaultCustomerEntityId() {
		return 2;
	}
}
