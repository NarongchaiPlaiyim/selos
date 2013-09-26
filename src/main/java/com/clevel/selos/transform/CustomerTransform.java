package com.clevel.selos.transform;

import com.clevel.selos.model.db.working.Customer;
import com.clevel.selos.model.db.working.Individual;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import com.clevel.selos.model.view.CustomerInfoView;

public class CustomerTransform extends Transform {

    public CustomerInfoView transformToView(Customer customer){
        CustomerInfoView customerInfoView = new CustomerInfoView();

        return customerInfoView;
    }

    public Customer transformToModel(CustomerInfoView customerInfoView, WorkCase workCase){
        Customer customer = new Customer();

        return customer;
    }

    public Customer transformToModel(CustomerInfoView customerInfoView, WorkCasePrescreen workCasePrescreen){
        Customer customer = new Customer();

        return customer;
    }

    private Individual transformToIndividual(CustomerInfoView customerInfoView){
        Individual individual = new Individual();

        return individual;
    }
}
