package com.clevel.selos.businesscontrol;

import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.CustomerInfoPostIndvView;
import com.clevel.selos.transform.CustomerTransform;

public class PostCustomerInfoIndvControl extends BusinessControl {
	private static final long serialVersionUID = 4851179619034165901L;

	@Inject
    @SELOS
    private Logger log;
	
	@Inject CustomerDAO customerDAO;
	@Inject CustomerTransform customerTransform;
	
	public PostCustomerInfoIndvControl() {
		
	}
	
	public CustomerInfoPostIndvView getCustomer(long customerId) {
		Customer result = null;
		try {
			if (customerId > 0)
				result = customerDAO.findById(customerId);
		} catch (Throwable e) {
		}
		return customerTransform.transformToIndvPostView(result);
	}
}
