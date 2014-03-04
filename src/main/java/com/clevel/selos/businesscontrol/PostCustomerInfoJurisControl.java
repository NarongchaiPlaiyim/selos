package com.clevel.selos.businesscontrol;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.CustomerInfoPostJurisView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.transform.CustomerTransform;

@Stateless
public class PostCustomerInfoJurisControl extends BusinessControl {
	private static final long serialVersionUID = -5483606932083214408L;

	@Inject
    @SELOS
    private Logger log;
	
	@Inject CustomerDAO customerDAO;
	@Inject CustomerTransform customerTransform;
	
	public PostCustomerInfoJurisControl() {
		
	}
	public CustomerInfoPostJurisView getCustomer(long customerId) {
		Customer result = null;
		try {
			if (customerId > 0)
				result = customerDAO.findById(customerId);
		} catch (Throwable e) {
		}
		return customerTransform.transformToJurisPostView(result);
	}
	
	public List<CustomerInfoView> getCustomerCommittees(long customerId) {
		List<CustomerInfoView> cusIndViewList = new ArrayList<CustomerInfoView>();
		if (customerId <= 0)
			return cusIndViewList;
		
		List<Customer> list = customerDAO.findCustomerByCommitteeId(customerId);
		if (list != null && !list.isEmpty()) {
			for (Customer committee : list) {
				 cusIndViewList.add(customerTransform.transformToView(committee));
			}
		}
		return cusIndViewList;
	}
	
	public void saveCustomerInfoJuristic(CustomerInfoPostJurisView view) {
		if (view.getId() <= 0)
			return;
		Customer model = customerDAO.findById(view.getId());
		customerTransform.updateModelFromPostView(model, view,getCurrentUser());
		customerDAO.persist(model);
	}
}
