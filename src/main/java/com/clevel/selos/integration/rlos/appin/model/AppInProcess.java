package com.clevel.selos.integration.rlos.appin.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class AppInProcess implements Serializable {
    private String appNumber;
    private String productCode;
    private String projectCode;
    private String interestRate;
    private String requestTenor;
    private String requestLimit;
    private String finalTenors;
    private String finalLimit;
    private String finalInstallment;
    private String status;
    private List<CustomerDetail> customerDetailList;

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getRequestTenor() {
        return requestTenor;
    }

    public void setRequestTenor(String requestTenor) {
        this.requestTenor = requestTenor;
    }

    public String getRequestLimit() {
        return requestLimit;
    }

    public void setRequestLimit(String requestLimit) {
        this.requestLimit = requestLimit;
    }

    public String getFinalTenors() {
        return finalTenors;
    }

    public void setFinalTenors(String finalTenors) {
        this.finalTenors = finalTenors;
    }

    public String getFinalLimit() {
        return finalLimit;
    }

    public void setFinalLimit(String finalLimit) {
        this.finalLimit = finalLimit;
    }

    public String getFinalInstallment() {
        return finalInstallment;
    }

    public void setFinalInstallment(String finalInstallment) {
        this.finalInstallment = finalInstallment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
                .append("productCode", productCode)
                .append("projectCode", projectCode)
                .append("interestRate", interestRate)
                .append("requestTenor", requestTenor)
                .append("requestLimit", requestLimit)
                .append("finalTenors", finalTenors)
                .append("finalLimit", finalLimit)
                .append("finalInstallment", finalInstallment)
                .append("status", status)
                .append("customerDetailList", customerDetailList)
                .toString();
    }
}
