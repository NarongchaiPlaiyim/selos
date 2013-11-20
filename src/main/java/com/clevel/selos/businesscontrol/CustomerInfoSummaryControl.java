package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.dao.working.IndividualDAO;
import com.clevel.selos.dao.working.WorkCaseDAO;
import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.view.CustomerInfoSummaryView;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.transform.CustomerTransform;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.List;

@Stateless
public class CustomerInfoSummaryControl extends BusinessControl {
    @Inject
    CustomerDAO customerDAO;
    @Inject
    WorkCaseDAO workCaseDAO;
    @Inject
    IndividualDAO individualDAO;

    @Inject
    CustomerTransform customerTransform;

    public CustomerInfoSummaryView getCustomerInfoSummary(long workCaseId) {
        log.info("getCustomerInfoSummary ::: workCaseId : {}", workCaseId);
        CustomerInfoSummaryView customerInfoSummaryView = new CustomerInfoSummaryView();

        List<Customer> customerList = customerDAO.findByWorkCaseId(workCaseId);
        List<CustomerInfoView> customerInfoViewList = customerTransform.transformToViewList(customerList);

        List<CustomerInfoView> borrowerCustomerList = customerTransform.transformToBorrowerViewList(customerInfoViewList);
        customerInfoSummaryView.setBorrowerCustomerViewList(borrowerCustomerList);

        List<CustomerInfoView> guarantorCustomerList = customerTransform.transformToGuarantorViewList(customerInfoViewList);
        customerInfoSummaryView.setGuarantorCustomerViewList(guarantorCustomerList);

        List<CustomerInfoView> relatedCustomerList = customerTransform.transformToRelatedViewList(customerInfoViewList);
        customerInfoSummaryView.setRelatedCustomerViewList(relatedCustomerList);

        return customerInfoSummaryView;
    }

    // For Customer Info. Detail - Individual
    public int getCaseBorrowerTypeIdByWorkCase(long workCaseId) {
        int caseBorrowerTypeId = 0;
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        if (workCase != null) {
            if (workCase.getBorrowerType() != null) {
                caseBorrowerTypeId = workCase.getBorrowerType().getId();
            }
        }
        return caseBorrowerTypeId;
    }

    public void saveCustomerInfoIndividual(CustomerInfoView customerInfoView, long workCaseId) {
        WorkCase workCase = workCaseDAO.findById(workCaseId);

        Customer customer = customerTransform.transformToModel(customerInfoView, null, workCase);
        customerDAO.persist(customer);
        individualDAO.persist(customer.getIndividual());

    }
}
