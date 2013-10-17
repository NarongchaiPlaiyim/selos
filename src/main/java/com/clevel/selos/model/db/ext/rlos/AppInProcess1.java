package com.clevel.selos.model.db.ext.rlos;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "ext_rlos_appin1")
public class AppInProcess1 implements Serializable {
    @Id
    @Column(name="app_ref_number", length=9)
    private String appRefNumber;

    @Column(name="product_code", length=4)
    private String productCode;

    @Column(name="project_code", length=5)
    private String projectCode;

    @Column(name="interest_rate", length=5)
    private String interestRate;

    @Column(name="request_tenor", length=2)
    private String requestTenor;

    @Column(name="request_limit", length=9)
    private String requestLimit;

    @Column(name="final_tenors", length=3)
    private String finalTenors;

    @Column(name="final_limit", length=9)
    private String finalLimit;

    @Column(name="final_installment", length=9)
    private String finalInstallment;

    @Column(name="status")
    private String status;

    @OneToMany(fetch=FetchType.LAZY, mappedBy="appInProcess1")
    private List<CustomerDetail1> customerDetail1s;

    public AppInProcess1() {
    }

    public String getAppRefNumber() {
        return appRefNumber;
    }

    public void setAppRefNumber(String appRefNumber) {
        this.appRefNumber = appRefNumber;
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

    public List<CustomerDetail1> getCustomerDetail1s() {
        return customerDetail1s;
    }

    public void setCustomerDetail1s(List<CustomerDetail1> customerDetail1s) {
        this.customerDetail1s = customerDetail1s;
    }
}
