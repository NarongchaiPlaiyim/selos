package com.clevel.selos.transform;

import java.util.Date;

import com.clevel.selos.dao.working.CustomerAcceptanceDAO;
import com.clevel.selos.integration.SELOS;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.CustomerAcceptance;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.CustomerAcceptanceView;
import com.clevel.selos.util.Util;

import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.inject.Inject;

public class CustomerAcceptanceTransform {
    private static final long serialVersionUID = -1413018148590835051L;
	@Inject
    @SELOS
    private Logger log;
    @Inject
    private CustomerAcceptanceDAO customerAcceptanceDAO;
    private CustomerAcceptance model;
    private CustomerAcceptanceView view;
    @Inject
    public CustomerAcceptanceTransform() {

    }
	public CustomerAcceptance transformToNewModel(CustomerAcceptanceView view,WorkCase workCase,User user) {
    	Date now = new Date();
        CustomerAcceptance model = new CustomerAcceptance();
        model.setApproveResult(view.getApproveResult());
        model.setCreateBy(user);
        model.setCreateDate(now);
        model.setModifyBy(user);
        model.setModifyDate(now);
        model.setWorkCase(workCase);
        return model;
    }
    public CustomerAcceptance transformToModel(CustomerAcceptanceView customerAcceptanceView) {
        CustomerAcceptance customerAcceptance = new CustomerAcceptance();
        if (customerAcceptanceView.getId() != 0) {
            customerAcceptance.setId(customerAcceptanceView.getId());
        } else if (customerAcceptanceView.getId() == 0) {
            customerAcceptance.setCreateBy(customerAcceptanceView.getCreateBy());
            customerAcceptance.setCreateDate(DateTime.now().toDate());
        }
        customerAcceptance.setApproveResult(customerAcceptanceView.getApproveResult());
        customerAcceptance.setModifyBy(customerAcceptanceView.getModifyBy());
        customerAcceptance.setModifyDate(DateTime.now().toDate());

        return customerAcceptance;
    }

    public CustomerAcceptance transformToModel(final CustomerAcceptanceView view, final WorkCase workCase, final User user) {
        log.debug("-- transform customerAcceptanceView to CustomerAcceptance [{}]", ""+view.toString());
        if(!Util.isZero(view.getId())){
            model = customerAcceptanceDAO.findCustomerAcceptanceByWorkCase(workCase);
            if(Util.isNull(model)){
                model = new CustomerAcceptance();
                model.setCreateDate(DateTime.now().toDate());
                model.setCreateBy(user);
                model.setWorkCase(workCase);
            }
        } else {
            model = new CustomerAcceptance();
            model.setCreateDate(DateTime.now().toDate());
            model.setCreateBy(user);
            model.setWorkCase(workCase);
        }
        model.setApproveResult(view.getApproveResult());
        model.setModifyBy(user);
        model.setModifyDate(DateTime.now().toDate());
        return model;
    }

    public CustomerAcceptanceView transformToView(final CustomerAcceptance model) {
        log.debug("-- transform CustomerAcceptance to CustomerAcceptanceView [{}]", ""+model.toString());
        view = new CustomerAcceptanceView();
        view.setId(model.getId());
        view.setApproveResult(model.getApproveResult());
        view.setCreateDate(model.getCreateDate());
        view.setCreateBy(model.getCreateBy());
        view.setModifyBy(model.getModifyBy());
        view.setModifyDate(model.getModifyDate());
        return view;
    }
}
