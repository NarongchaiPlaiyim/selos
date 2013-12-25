package com.clevel.selos.model.view;


import com.clevel.selos.model.db.master.Bank;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class DisbursementBahtnetDetailView implements Serializable{

    private Bank bankName;
    private int bankCode;
    private String accountNumber;
    private String branchName;
    private String benefitName;
    private List<CreditTypeDetailView> creditType;
    private BigDecimal totalAmount;

    public Bank getBankName() {
        return bankName;
    }

    public void setBankName(Bank bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getBenefitName() {
        return benefitName;
    }

    public void setBenefitName(String benefitName) {
        this.benefitName = benefitName;
    }

    public List<CreditTypeDetailView> getCreditType() {
        return creditType;
    }

    public void setCreditType(List<CreditTypeDetailView> creditType) {
        this.creditType = creditType;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getBankCode() {
        return bankCode;
    }

    public void setBankCode(int bankCode) {
        this.bankCode = bankCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("bankName", bankName)
                .append("accountNumber", accountNumber)
                .append("branchName", branchName)
                .append("benefitName", benefitName)
                .append("creditType", creditType)
                .append("totalAmount", totalAmount)
                .toString();
    }
}
