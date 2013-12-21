package com.clevel.selos.model.view;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class DisbursementDepositBaDetailView implements Serializable{

    private String accountNumber;
    private String accountName;
    private List<CreditTypeDetailView> creditType;
    private BigDecimal totalAmount;


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("accountNumber", accountNumber)
                .append("accountName", accountName)
                .append("creditType", creditType)
                .append("totalAmount", totalAmount)
                .toString();
    }
}
