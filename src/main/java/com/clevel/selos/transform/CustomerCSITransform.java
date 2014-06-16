package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.CustomerCSI;
import com.clevel.selos.model.view.CustomerCSIView;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class CustomerCSITransform extends Transform {

    @Inject
    public CustomerCSITransform() {
    }

    public CustomerCSI transformToModel(CustomerCSIView customerCSIView) {
        CustomerCSI customerCSI = new CustomerCSI();

        if(customerCSIView.getId() != 0){
            customerCSI.setId(customerCSIView.getId());
        }
        customerCSI.setCustomer(customerCSIView.getCustomer());
        customerCSI.setMatchedType(customerCSIView.getMatchedType());
        customerCSI.setWarningCode(customerCSIView.getWarningCode());
        customerCSI.setWarningDate(customerCSIView.getWarningDate());

        return customerCSI;
    }

    public CustomerCSIView transformToView(CustomerCSI customerCSI) {
        CustomerCSIView customerCSIView = new CustomerCSIView();

        customerCSIView.setId(customerCSI.getId());
        customerCSIView.setCustomer(customerCSI.getCustomer());
        customerCSIView.setMatchedType(customerCSI.getMatchedType());
        customerCSIView.setWarningCode(customerCSI.getWarningCode());
        customerCSIView.setWarningDate(customerCSI.getWarningDate());

        return customerCSIView;
    }

    public List<CustomerCSIView> transformToViewList(List<CustomerCSI> customerCSIList) {
        List<CustomerCSIView> CustomerCSIViews = new ArrayList<CustomerCSIView>();
        if (customerCSIList != null) {
            for (CustomerCSI customerCSI : customerCSIList) {
                CustomerCSIView CustomerCSIView = transformToView(customerCSI);
                CustomerCSIViews.add(CustomerCSIView);
            }
        }
        return CustomerCSIViews;
    }
}
