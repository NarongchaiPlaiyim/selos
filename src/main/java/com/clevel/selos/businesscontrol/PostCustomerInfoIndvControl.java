package com.clevel.selos.businesscontrol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.slf4j.Logger;

import com.clevel.selos.dao.master.MaritalStatusDAO;
import com.clevel.selos.dao.master.TitleDAO;
import com.clevel.selos.dao.working.CustomerAttorneyDAO;
import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.AttorneyType;
import com.clevel.selos.model.RadioValue;
import com.clevel.selos.model.db.master.MaritalStatus;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.CustomerAttorney;
import com.clevel.selos.model.db.working.Individual;
import com.clevel.selos.model.view.CustomerAttorneySelectView;
import com.clevel.selos.model.view.CustomerAttorneyView;
import com.clevel.selos.model.view.CustomerInfoPostIndvView;
import com.clevel.selos.transform.CustomerAttorneyTransform;
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
	@Inject CustomerAttorneyDAO customerAttorneyDAO;
	
	@Inject CustomerTransform customerTransform;
	@Inject CustomerAttorneyTransform customerAttorneyTransform;
	
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
	
	public void saveCustomerInfoIndividual(CustomerInfoPostIndvView view,CustomerAttorneyView attorneyView) {
		if (view.getId() <= 0)
			return;
		Customer model = customerDAO.findById(view.getId());
		customerTransform.updateModelFromPostView(model, view);
		
		Individual indv = model.getIndividual();
		if (indv != null) {
			CustomerAttorney attorneyModel = indv.getCustomerAttorney();
			if (RadioValue.YES.equals(indv.getAttorneyRequired())) {
				if (attorneyModel == null) {
					attorneyModel = customerAttorneyTransform.createNewModel(attorneyView, model.getWorkCase(), AttorneyType.ATTORNEY_IN_RIGHT);
					customerAttorneyDAO.save(attorneyModel);
					indv.setCustomerAttorney(attorneyModel);
				} else {
					attorneyModel.setAttorneyType(AttorneyType.ATTORNEY_IN_RIGHT);
					customerAttorneyTransform.updateModelFromView(attorneyModel, attorneyView);
					customerAttorneyDAO.persist(attorneyModel);
				}
			} else { //remove
				if (attorneyModel != null) {
					attorneyModel.setCustomer(null);
					customerAttorneyDAO.delete(attorneyModel);
					indv.setCustomerAttorney(null);
				}
			}
		}
		
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

	public CustomerAttorneyView getCustomerAttorneyView(long customerAttorneyId) {
		CustomerAttorney model = null;
		try {
			if (customerAttorneyId > 0)
				model = customerAttorneyDAO.findById(customerAttorneyId);
		} catch (Throwable e) {
		}
		return customerAttorneyTransform.transformToView(model);
	}

	public CustomerAttorneyView getCustomerAttorneyViewFromCustomer(long customerId) {
		Customer model = null;
		try {
			if (customerId > 0)
				model = customerDAO.findById(customerId);
		} catch (Throwable e) {
		}
		return customerAttorneyTransform.transformToView(model);
	}
	
	 public List<CustomerAttorneySelectView> getAttorneySelectList(long workCaseId) {
		 if (workCaseId <=0)
			 return Collections.emptyList();
		 List<Customer> customers = customerDAO.findCustomerCanBeAttorneyRight(workCaseId);
		 List<CustomerAttorneySelectView> rtnDatas = new ArrayList<CustomerAttorneySelectView>();
		 for (Customer customer : customers) {
			 rtnDatas.add(customerAttorneyTransform.transformAttorneySelectToView(customer));
		 }
		 return rtnDatas;
	 }
	
}
