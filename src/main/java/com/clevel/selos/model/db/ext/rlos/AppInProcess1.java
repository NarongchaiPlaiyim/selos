package com.clevel.selos.model.db.ext.rlos;


import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "ext_rlos_appin1")
public class AppInProcess1 implements Serializable {
    @Id
    @Column(name="app_ref_number", length=14)
    private String appRefNumber;

    @Column(name="product_code", length=4)
    private String productCode;

    @Column(name="project_code", length=5)
    private String projectCode;

    @Column(name="interest_rate")
    private BigDecimal interestRate;

    @Column(name="request_tenor")
    private BigDecimal requestTenor;

    @Column(name="request_limit")
    private BigDecimal requestLimit;

    @Column(name="final_tenors")
    private BigDecimal finalTenors;

    @Column(name="final_limit")
    private BigDecimal finalLimit;

    @Column(name="final_installment")
    private BigDecimal finalInstallment;

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

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getRequestTenor() {
        return requestTenor;
    }

    public void setRequestTenor(BigDecimal requestTenor) {
        this.requestTenor = requestTenor;
    }

    public BigDecimal getRequestLimit() {
        return requestLimit;
    }

    public void setRequestLimit(BigDecimal requestLimit) {
        this.requestLimit = requestLimit;
    }

    public BigDecimal getFinalTenors() {
        return finalTenors;
    }

    public void setFinalTenors(BigDecimal finalTenors) {
        this.finalTenors = finalTenors;
    }

    public BigDecimal getFinalLimit() {
        return finalLimit;
    }

    public void setFinalLimit(BigDecimal finalLimit) {
        this.finalLimit = finalLimit;
    }

    public BigDecimal getFinalInstallment() {
        return finalInstallment;
    }

    public void setFinalInstallment(BigDecimal finalInstallment) {
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
