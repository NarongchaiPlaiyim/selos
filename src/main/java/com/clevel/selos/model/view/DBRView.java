package com.clevel.selos.model.view;

import com.clevel.selos.model.db.working.WorkCase;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class DBRView implements Serializable {
    private long id;
    private WorkCase workCase;
    private List<DBRDetailView> dbrDetailViews;
    private int incomeFactor;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyIncomeAdjust;
    private BigDecimal monthlyIncomePerMonth;
    private BigDecimal netMonthlyIncome;
    private BigDecimal currentDBR;
    private BigDecimal dbrBeforeRequest;
    private BigDecimal dbrInterest;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public WorkCase getWorkCase() {
        return workCase;
    }

    public void setWorkCase(WorkCase workCase) {
        this.workCase = workCase;
    }

    public List<DBRDetailView> getDbrDetailViews() {
        return dbrDetailViews;
    }

    public void setDbrDetailViews(List<DBRDetailView> dbrDetailViews) {
        this.dbrDetailViews = dbrDetailViews;
    }

    public int getIncomeFactor() {
        return incomeFactor;
    }

    public void setIncomeFactor(int incomeFactor) {
        this.incomeFactor = incomeFactor;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimal getMonthlyIncomeAdjust() {
        return monthlyIncomeAdjust;
    }

    public void setMonthlyIncomeAdjust(BigDecimal monthlyIncomeAdjust) {
        this.monthlyIncomeAdjust = monthlyIncomeAdjust;
    }

    public BigDecimal getMonthlyIncomePerMonth() {
        return monthlyIncomePerMonth;
    }

    public void setMonthlyIncomePerMonth(BigDecimal monthlyIncomePerMonth) {
        this.monthlyIncomePerMonth = monthlyIncomePerMonth;
    }

    public BigDecimal getNetMonthlyIncome() {
        return netMonthlyIncome;
    }

    public void setNetMonthlyIncome(BigDecimal netMonthlyIncome) {
        this.netMonthlyIncome = netMonthlyIncome;
    }

    public BigDecimal getCurrentDBR() {
        return currentDBR;
    }

    public void setCurrentDBR(BigDecimal currentDBR) {
        this.currentDBR = currentDBR;
    }

    public BigDecimal getDbrBeforeRequest() {
        return dbrBeforeRequest;
    }

    public void setDbrBeforeRequest(BigDecimal dbrBeforeRequest) {
        this.dbrBeforeRequest = dbrBeforeRequest;
    }

    public BigDecimal getDbrInterest() {
        return dbrInterest;
    }

    public void setDbrInterest(BigDecimal dbrInterest) {
        this.dbrInterest = dbrInterest;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("workCase", workCase)
                .append("dbrDetailViews", dbrDetailViews)
                .append("incomeFactor", incomeFactor)
                .append("monthlyIncome", monthlyIncome)
                .append("monthlyIncomeAdjust", monthlyIncomeAdjust)
                .append("monthlyIncomePerMonth", monthlyIncomePerMonth)
                .append("netMonthlyIncome", netMonthlyIncome)
                .append("currentDBR", currentDBR)
                .append("dbrBeforeRequest", dbrBeforeRequest)
                .append("dbrInterest", dbrInterest)
                .toString();
    }
}
