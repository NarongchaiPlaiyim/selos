package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.CustomerAcceptance;
import com.clevel.selos.model.view.CustomerAcceptanceView;
import org.joda.time.DateTime;

public class CustomerAcceptanceTransform extends Transform {

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

    public CustomerAcceptanceView transformToView(CustomerAcceptance customerAcceptance) {
        CustomerAcceptanceView customerAcceptanceView = new CustomerAcceptanceView();
        customerAcceptanceView.setId(customerAcceptance.getId());
        customerAcceptanceView.setApproveResult(customerAcceptance.getApproveResult());
        customerAcceptanceView.setCreateBy(customerAcceptance.getCreateBy());
        customerAcceptanceView.setCreateDate(customerAcceptance.getCreateDate());
        customerAcceptanceView.setModifyBy(customerAcceptance.getModifyBy());
        customerAcceptanceView.setModifyDate(customerAcceptance.getModifyDate());

        return customerAcceptanceView;
    }
}
