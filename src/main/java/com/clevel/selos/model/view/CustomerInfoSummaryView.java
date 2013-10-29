package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.CustomerEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CustomerInfoSummaryView implements Serializable {
    private long id;
    private CustomerEntity customerEntity;
    private List<CustomerInfoView> customerInfoViewList;
    private List<CustomerInfoView> borrowerCustomerViewList;
    private List<CustomerInfoView> guarantorCustomerViewList;
    private List<CustomerInfoView> relatedCustomerViewList;

    public CustomerInfoSummaryView() {
        reset();
    }

    public void reset() {
        this.customerEntity = new CustomerEntity();
        this.customerInfoViewList = new ArrayList<CustomerInfoView>();
        this.borrowerCustomerViewList = new ArrayList<CustomerInfoView>();
        this.guarantorCustomerViewList = new ArrayList<CustomerInfoView>();
        this.relatedCustomerViewList = new ArrayList<CustomerInfoView>();
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

    public List<CustomerInfoView> getGuarantorCustomerViewList() {
        return guarantorCustomerViewList;
    }

    public void setGuarantorCustomerViewList(List<CustomerInfoView> guarantorCustomerViewList) {
        this.guarantorCustomerViewList = guarantorCustomerViewList;
    }

    public List<CustomerInfoView> getRelatedCustomerViewList() {
        return relatedCustomerViewList;
    }

    public void setRelatedCustomerViewList(List<CustomerInfoView> relatedCustomerViewList) {
        this.relatedCustomerViewList = relatedCustomerViewList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("customerEntity", customerEntity).
                append("customerInfoViewList", customerInfoViewList).
                append("borrowerCustomerViewList", borrowerCustomerViewList).
                append("guarantorCustomerViewList", guarantorCustomerViewList).
                append("relatedCustomerViewList", relatedCustomerViewList).
                toString();
    }
}
