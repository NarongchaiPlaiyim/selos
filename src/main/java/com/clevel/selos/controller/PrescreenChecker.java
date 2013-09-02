package com.clevel.selos.controller;

import com.clevel.selos.model.view.CustomerView;
import com.clevel.selos.system.MessageProvider;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ViewScoped
@ManagedBean(name = "prescreenChecker")
public class PrescreenChecker implements Serializable {
    @Inject
    Logger log;
    @Inject
    MessageProvider msg;

    private List<CustomerView> customerViewList;
    private String[] citizenID;

    public PrescreenChecker(){

    }

    @PostConstruct
    public void onCreation() {
        //TODO Generate row for textbox to check Citizen id
        if(customerViewList == null){
            customerViewList = new ArrayList<CustomerView>();
            CustomerView customer = new CustomerView();
            customer.setId(new BigDecimal(1));
            customer.setCustomerName("test01");
            customer.setCitizenID("123456");
            customer.setValidCitizenID(2);
            customerViewList.add(customer);
            customer = new CustomerView();
            customer.setId(new BigDecimal(2));
            customer.setCustomerName("test02");
            customer.setCitizenID("1234567");
            customer.setValidCitizenID(2);
            customerViewList.add(customer);
        }

        if(customerViewList != null){
            int row = customerViewList.size();
            citizenID = new String[row];
        }
    }

    public void onCheckCustomer(){
        List<CustomerView> tmpCustomerViewList = new ArrayList<CustomerView>();
        tmpCustomerViewList = customerViewList;
        customerViewList = new ArrayList<CustomerView>();   //Clear old value
        for(CustomerView customer : tmpCustomerViewList){
            if(customer.getCitizenID().trim().equals(customer.getInputCitizenID().trim())){
                log.info("Check CitizenID Customer : {}, Match", customer.getCustomerName());
                customer.setValidCitizenID(1);
            }else{
                log.info("Check CitizenID Customer : {}, Not Match", customer.getCustomerName());
                customer.setValidCitizenID(0);
            }
            customerViewList.add(customer);
        }

    }

    public List<CustomerView> getCustomerViewList() {
        return customerViewList;
    }

    public void setCustomerViewList(List<CustomerView> customerViewList) {
        this.customerViewList = customerViewList;
    }

    public String[] getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(String[] citizenID) {
        this.citizenID = citizenID;
    }
}
