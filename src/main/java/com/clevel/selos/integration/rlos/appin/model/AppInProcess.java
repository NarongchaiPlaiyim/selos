package com.clevel.selos.integration.rlos.appin.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class AppInProcess implements Serializable {
    private String appNumber;
    private String status;
    private List<CreditDetail> creditDetailList;
    private List<CustomerDetail> customerDetailList;

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CreditDetail> getCreditDetailList() {
        return creditDetailList;
    }

    public void setCreditDetailList(List<CreditDetail> creditDetailList) {
        this.creditDetailList = creditDetailList;
    }

    public List<CustomerDetail> getCustomerDetailList() {
        return customerDetailList;
    }

    public void setCustomerDetailList(List<CustomerDetail> customerDetailList) {
        this.customerDetailList = customerDetailList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("appNumber", appNumber)
                .append("status", status)
                .append("creditDetailList", creditDetailList)
                .append("customerDetailList", customerDetailList)
                .toString();
    }
}
