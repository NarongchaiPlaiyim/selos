package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.CustomerInfoSummaryView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.transform.CustomerTransform;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class CustomerInfoSummaryControl extends BusinessControl {
    @Inject
    Logger log;

    @Inject
    CustomerDAO customerDAO;
    @Inject
    CustomerTransform customerTransform;

    public CustomerInfoSummaryView getCustomerInfoSummary(long workCaseId){
        log.info("getCustomerInfoSummary ::: workCaseId : {}", workCaseId);
        CustomerInfoSummaryView customerInfoSummaryView = new CustomerInfoSummaryView();

        List<Customer> customerList = customerDAO.findByWorkCaseId(workCaseId);
        List<CustomerInfoView> customerInfoViewList = customerTransform.transformToViewList(customerList);
        List<CustomerInfoView> borrowerCustomerList = customerTransform.transformToBorrowerViewList(customerInfoViewList);

        customerInfoSummaryView.setBorrowerCustomerViewList(borrowerCustomerList);

        return customerInfoSummaryView;
    }
}
