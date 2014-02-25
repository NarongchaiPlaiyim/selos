package com.clevel.selos.businesscontrol;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.master.MaritalStatusDAO;
import com.clevel.selos.dao.master.TitleDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.MaritalStatus;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.CustomerInfoPostIndvView;
import com.clevel.selos.transform.CustomerTransform;

@Stateless
public class PostCustomerInfoIndvControl extends BusinessControl {
	private static final long serialVersionUID = 4851179619034165901L;

	@Inject
    @SELOS
    private Logger log;
	
	@Inject MaritalStatusDAO maritalStatusDAO;
	@Inject TitleDAO titleDAO;
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
	
	public void saveCustomerInfoIndividual(CustomerInfoPostIndvView view) {
		if (view.getId() <= 0)
			return;
		Customer model = customerDAO.findById(view.getId());
		customerTransform.updateModelFromPostView(model, view);
		
		MaritalStatus maritalStatus = maritalStatusDAO.findById(view.getMaritalStatusId());
    	if (maritalStatus.getSpouseFlag() == 1 && model.getSpouseId() > 0) {
    		//update spouse info
    		Customer spouse = customerDAO.findById(model.getSpouseId());
    		if (spouse != null) {
    			if (view.getSpouseTitleId() > 0) {
    				spouse.setTitle(titleDAO.findRefById(view.getSpouseTitleId()));
    			} else {
    				spouse.setTitle(null);
    			}
    			spouse.setNameTh(view.getSpouseNameTH());
    			spouse.setLastNameTh(view.getLastNameTH());
    			customerDAO.persist(spouse);
    		}
    	}
		customerDAO.persist(model);
	}
}
