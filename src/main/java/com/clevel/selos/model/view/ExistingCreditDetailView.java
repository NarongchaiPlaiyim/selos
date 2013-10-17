package com.clevel.selos.model.view;

import java.io.Serializable;
import java.math.BigDecimal;

public class ExistingCreditDetailView implements Serializable {
    private long id;
    private String accountName;
    private String accountSuf;
    private String accountNumber;
    private int accountStatusID;
    private String accountStatus;

    private String productCode;
    private String projectCode;

    private String productProgram;
    private String creditType;
    private BigDecimal limit;
    private BigDecimal outstanding;
    private BigDecimal installment;
    private BigDecimal intFeePercent;
    private String source;
    private int tenor;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountSuf() {
        return accountSuf;
    }

    public void setAccountSuf(String accountSuf) {
        this.accountSuf = accountSuf;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAccountStatusID() {
        return accountStatusID;
    }

    public void setAccountStatusID(int accountStatusID) {
        this.accountStatusID = accountStatusID;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
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

    public String getProductProgram() {
        return productProgram;
    }

    public void setProductProgram(String productProgram) {
        this.productProgram = productProgram;
    }

    public String getCreditType() {
        return creditType;
    }

    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getOutstanding() {
        return outstanding;
    }

    public void setOutstanding(BigDecimal outstanding) {
        this.outstanding = outstanding;
    }

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
    }

    public BigDecimal getIntFeePercent() {
        return intFeePercent;
    }

    public void setIntFeePercent(BigDecimal intFeePercent) {
        this.intFeePercent = intFeePercent;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getTenor() {
        return tenor;
    }

    public void setTenor(int tenor) {
        this.tenor = tenor;
    }
}
