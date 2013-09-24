package com.clevel.selos.controller;

import com.clevel.selos.dao.working.CustomerDAO;
import com.clevel.selos.model.view.CustomerInfoView;
import com.clevel.selos.system.message.Message;
import com.clevel.selos.system.message.NormalMessage;
import com.clevel.selos.system.message.ValidationMessage;
import com.clevel.selos.system.message.ExceptionMessage;
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
    @NormalMessage
    Message msg;

    @Inject
    @ValidationMessage
    Message validationMsg;

    @Inject
    @ExceptionMessage
    Message exceptionMsg;


    private List<CustomerInfoView> customerInfoViewList;
    private String[] citizenID;

    @Inject
    CustomerDAO customerDAO;

    public PrescreenChecker(){

    }

    @PostConstruct
    public void onCreation() {

        //TODO Generate row for textbox to check Citizen id
        /*if(customerInfoViewList == null){
            customerInfoViewList = new ArrayList<CustomerInfoView>();
            CustomerInfoView customer = new CustomerInfoView();
            customer.setId(new BigDecimal(1));
            customer.setCustomerName("test01");
            customer.setCitizenID("123456");
            customer.setValidCitizenID(2);
            customerInfoViewList.add(customer);
            customer = new CustomerInfoView();
            customer.setId(new BigDecimal(2));
            customer.setCustomerName("test02");
            customer.setCitizenID("1234567");
            customer.setValidCitizenID(2);
            customerInfoViewList.add(customer);
        }

        if(customerInfoViewList != null){
            int row = customerInfoViewList.size();
            citizenID = new String[row];
        }*/
    }

    public void onCheckCustomer(){
        /*List<CustomerInfoView> tmpCustomerInfoViewList = new ArrayList<CustomerInfoView>();
        tmpCustomerInfoViewList = customerInfoViewList;
        customerInfoViewList = new ArrayList<CustomerInfoView>();   //Clear old value
        for(CustomerInfoView customer : tmpCustomerInfoViewList){
            if(customer.getCitizenID().trim().equals(customer.getInputCitizenID().trim())){
                log.info("Check CitizenID Customer : {}, Match", customer.getCustomerName());
                customer.setValidCitizenID(1);
            }else{
                log.info("Check CitizenID Customer : {}, Not Match", customer.getCustomerName());
                customer.setValidCitizenID(0);
            }
            customerInfoViewList.add(customer);
        }*/

    }

    public List<CustomerInfoView> getCustomerInfoViewList() {
        return customerInfoViewList;
    }

    public void setCustomerInfoViewList(List<CustomerInfoView> customerInfoViewList) {
        this.customerInfoViewList = customerInfoViewList;
    }

    public String[] getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(String[] citizenID) {
        this.citizenID = citizenID;
    }
}
