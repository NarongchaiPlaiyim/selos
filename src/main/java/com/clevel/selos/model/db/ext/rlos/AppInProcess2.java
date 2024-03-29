package com.clevel.selos.model.db.ext.rlos;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "ext_rlos_appin2")
public class AppInProcess2 implements Serializable {
    @Id
    @Column(name = "app_ref_number", length = 14)
    private String appRefNumber;

    @Column(name = "product_code", length = 4)
    private String productCode;

    @Column(name = "project_code", length = 5)
    private String projectCode;

    @Column(name = "interest_rate")
    private BigDecimal interestRate;

    @Column(name = "request_tenor")
    private BigDecimal requestTenor;

    @Column(name = "request_limit")
    private BigDecimal requestLimit;

    @Column(name = "final_tenors")
    private BigDecimal finalTenors;

    @Column(name = "final_limit")
    private BigDecimal finalLimit;

    @Column(name = "final_installment")
    private BigDecimal finalInstallment;

    @Column(name = "product_code2", length = 4)
    private String productCode2;

    @Column(name = "project_code2", length = 5)
    private String projectCode2;

    @Column(name = "interest_rate2")
    private BigDecimal interestRate2;

    @Column(name = "request_tenor2")
    private BigDecimal requestTenor2;

    @Column(name = "request_limit2")
    private BigDecimal requestLimit2;

    @Column(name = "final_tenors2")
    private BigDecimal finalTenors2;

    @Column(name = "final_limit2")
    private BigDecimal finalLimit2;

    @Column(name = "final_installment2")
    private BigDecimal finalInstallment2;

    @Column(name = "status")
    private String status;

    @Column(name = "dt_snt_stp")
    private String dateSentSTP;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "appInProcess2")
    private List<CustomerDetail2> customerDetail2s;

    public AppInProcess2() {
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
        if (interestRate == null) {
            return BigDecimal.ZERO;
        }
        return interestRate;
    }

    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getRequestTenor() {
        if (requestTenor == null) {
            return BigDecimal.ZERO;
        }
        return requestTenor;
    }

    public void setRequestTenor(BigDecimal requestTenor) {
        this.requestTenor = requestTenor;
    }

    public BigDecimal getRequestLimit() {
        if (requestLimit == null) {
            return BigDecimal.ZERO;
        }
        return requestLimit;
    }

    public void setRequestLimit(BigDecimal requestLimit) {
        this.requestLimit = requestLimit;
    }

    public BigDecimal getFinalTenors() {
        if (finalTenors == null) {
            return BigDecimal.ZERO;
        }
        return finalTenors;
    }

    public void setFinalTenors(BigDecimal finalTenors) {
        this.finalTenors = finalTenors;
    }

    public BigDecimal getFinalLimit() {
        if (finalLimit == null) {
            return BigDecimal.ZERO;
        }
        return finalLimit;
    }

    public void setFinalLimit(BigDecimal finalLimit) {
        this.finalLimit = finalLimit;
    }

    public BigDecimal getFinalInstallment() {
        if (finalInstallment == null) {
            return BigDecimal.ZERO;
        }
        return finalInstallment;
    }

    public void setFinalInstallment(BigDecimal finalInstallment) {
        this.finalInstallment = finalInstallment;
    }

    public String getProductCode2() {
        return productCode2;
    }

    public void setProductCode2(String productCode2) {
        this.productCode2 = productCode2;
    }

    public String getProjectCode2() {
        return projectCode2;
    }

    public void setProjectCode2(String projectCode2) {
        this.projectCode2 = projectCode2;
    }

    public BigDecimal getInterestRate2() {
        if (interestRate2 == null) {
            return BigDecimal.ZERO;
        }
        return interestRate2;
    }

    public void setInterestRate2(BigDecimal interestRate2) {
        this.interestRate2 = interestRate2;
    }

    public BigDecimal getRequestTenor2() {
        if (requestTenor2 == null) {
            return BigDecimal.ZERO;
        }
        return requestTenor2;
    }

    public void setRequestTenor2(BigDecimal requestTenor2) {
        this.requestTenor2 = requestTenor2;
    }

    public BigDecimal getRequestLimit2() {
        if (requestLimit2 == null) {
            return BigDecimal.ZERO;
        }
        return requestLimit2;
    }

    public void setRequestLimit2(BigDecimal requestLimit2) {
        this.requestLimit2 = requestLimit2;
    }

    public BigDecimal getFinalTenors2() {
        if (finalTenors2 == null) {
            return BigDecimal.ZERO;
        }
        return finalTenors2;
    }

    public void setFinalTenors2(BigDecimal finalTenors2) {
        this.finalTenors2 = finalTenors2;
    }

    public BigDecimal getFinalLimit2() {
        if (finalLimit2 == null) {
            return BigDecimal.ZERO;
        }
        return finalLimit2;
    }

    public void setFinalLimit2(BigDecimal finalLimit2) {
        this.finalLimit2 = finalLimit2;
    }

    public BigDecimal getFinalInstallment2() {
        if (finalInstallment2 == null) {
            return BigDecimal.ZERO;
        }
        return finalInstallment2;
    }

    public void setFinalInstallment2(BigDecimal finalInstallment2) {
        this.finalInstallment2 = finalInstallment2;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateSentSTP() {
        return dateSentSTP;
    }

    public void setDateSentSTP(String dateSentSTP) {
        this.dateSentSTP = dateSentSTP;
    }

    public List<CustomerDetail2> getCustomerDetail2s() {
        return customerDetail2s;
    }

    public void setCustomerDetail2s(List<CustomerDetail2> customerDetail2s) {
        this.customerDetail2s = customerDetail2s;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("appRefNumber", appRefNumber)
                .append("productCode", productCode)
                .append("projectCode", projectCode)
                .append("interestRate", interestRate)
                .append("requestTenor", requestTenor)
                .append("requestLimit", requestLimit)
                .append("finalTenors", finalTenors)
                .append("finalLimit", finalLimit)
                .append("finalInstallment", finalInstallment)
                .append("productCode2", productCode2)
                .append("projectCode2", projectCode2)
                .append("interestRate2", interestRate2)
                .append("requestTenor2", requestTenor2)
                .append("requestLimit2", requestLimit2)
                .append("finalTenors2", finalTenors2)
                .append("finalLimit2", finalLimit2)
                .append("finalInstallment2", finalInstallment2)
                .append("status", status)
                .append("dateSentSTP", dateSentSTP)
                .append("customerDetail2s", customerDetail2s)
                .toString();
    }
}
