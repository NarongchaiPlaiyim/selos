package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerInfoSummaryView implements Serializable {
    private long id;
    private CustomerEntity customerEntity;
    private List<CustomerInfoView> customerInfoViewList;
    private List<CustomerInfoView> borrowerCustomerViewList;

    public CustomerInfoSummaryView(){
        reset();
    }

    public void reset(){
        this.customerEntity = new CustomerEntity();
        this.customerInfoViewList = new ArrayList<CustomerInfoView>();
        this.borrowerCustomerViewList = new ArrayList<CustomerInfoView>();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public List<CustomerInfoView> getCustomerInfoViewList() {
        return customerInfoViewList;
    }

    public void setCustomerInfoViewList(List<CustomerInfoView> customerInfoViewList) {
        this.customerInfoViewList = customerInfoViewList;
    }

    public List<CustomerInfoView> getBorrowerCustomerViewList() {
        return borrowerCustomerViewList;
    }

    public void setBorrowerCustomerViewList(List<CustomerInfoView> borrowerCustomerViewList) {
        this.borrowerCustomerViewList = borrowerCustomerViewList;
    }
}
