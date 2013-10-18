package com.clevel.selos.businesscontrol;

import com.clevel.selos.dao.master.CustomerEntityDAO;
import com.clevel.selos.dao.master.UserDAO;
import com.clevel.selos.dao.working.*;
import com.clevel.selos.model.db.master.BAPaymentMethod;
import com.clevel.selos.model.db.master.CustomerEntity;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.*;
import com.clevel.selos.model.view.*;
import com.clevel.selos.transform.BasicInfoAccPurposeTransform;
import com.clevel.selos.transform.BasicInfoAccountTransform;
import com.clevel.selos.transform.BasicInfoTransform;
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
    WorkCaseDAO workCaseDAO;

    @Inject
    CustomerTransform customerTransform;

    public CustomerInfoSummaryView getCustomerInfoSummary(long workCaseId){
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
    public int getCaseBorrowerTypeIdByWorkCase(long workCaseId){
        int caseBorrowerTypeId = 0;
        WorkCase workCase = workCaseDAO.findById(workCaseId);
        if(workCase != null){
            if(workCase.getBorrowerType() != null){
                caseBorrowerTypeId = workCase.getBorrowerType().getId();
            }
        }
        return caseBorrowerTypeId;
    }
}
