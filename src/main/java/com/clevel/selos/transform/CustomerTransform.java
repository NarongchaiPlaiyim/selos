package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.view.CustomerInfoView;

public class CustomerTransform extends Transform {

    public CustomerInfoView transform(Customer customer){
        CustomerInfoView customerInfoView = new CustomerInfoView();

        return customerInfoView;
    }

    public Customer transform(CustomerInfoView customerInfoView){
        Customer customer = new Customer();

        return customer;
    }
}
