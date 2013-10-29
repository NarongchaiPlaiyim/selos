package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;

public class DBRDetailView implements Serializable {
    private long id;
    private String accountName;
    private BigDecimal limit;
    private BigDecimal installment;
    private BigDecimal debtForCalculate;
    private LoanTypeView loanTypeView;

    public DBRDetailView() {
        reset();
    }

    public void reset() {
        this.id = 0;
        this.accountName = "";
        this.limit = BigDecimal.ZERO;
        this.installment = BigDecimal.ZERO;
        this.debtForCalculate = BigDecimal.ZERO;
        this.loanTypeView = new LoanTypeView();
    }

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

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public BigDecimal getInstallment() {
        return installment;
    }

    public void setInstallment(BigDecimal installment) {
        this.installment = installment;
    }

    public BigDecimal getDebtForCalculate() {
        return debtForCalculate;
    }

    public void setDebtForCalculate(BigDecimal debtForCalculate) {
        this.debtForCalculate = debtForCalculate;
    }

    public LoanTypeView getLoanTypeView() {
        return loanTypeView;
    }

    public void setLoanTypeView(LoanTypeView loanTypeView) {
        this.loanTypeView = loanTypeView;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("accountName", accountName)
                .append("limit", limit)
                .append("installment", installment)
                .append("debtForCalculate", debtForCalculate)
                .append("loanTypeView", loanTypeView)
                .toString();
    }
}
