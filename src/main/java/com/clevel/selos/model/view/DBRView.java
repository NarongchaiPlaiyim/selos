package com.clevel.selos.model.view;

import com.clevel.selos.model.db.working.WorkCase;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DBRView implements Serializable {
    private long id;
    private List<DBRDetailView> dbrDetailViews;
    private BigDecimal incomeFactor;
    private BigDecimal monthlyIncome;
    private BigDecimal monthlyIncomeAdjust;
    private BigDecimal monthlyIncomePerMonth;
    private BigDecimal netMonthlyIncome;
    private BigDecimal currentDBR;
    private BigDecimal dbrBeforeRequest;
    private BigDecimal dbrInterest;
    private long workCaseId;
    private String userId;

    public DBRView() {
        reset();
    }

    public void reset() {
        this.id = 0;
        this.incomeFactor = BigDecimal.ZERO;
        this.dbrDetailViews = new ArrayList<DBRDetailView>();
        this.monthlyIncome = BigDecimal.ZERO;
        this.monthlyIncomeAdjust = BigDecimal.ZERO;
        this.monthlyIncomePerMonth = BigDecimal.ZERO;
        this.netMonthlyIncome = BigDecimal.ZERO;
        this.currentDBR = BigDecimal.ZERO;
        this.dbrBeforeRequest = BigDecimal.ZERO;
        this.dbrInterest = BigDecimal.ZERO;
        this.workCaseId = 0L;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<DBRDetailView> getDbrDetailViews() {
        return dbrDetailViews;
    }

    public void setDbrDetailViews(List<DBRDetailView> dbrDetailViews) {
        this.dbrDetailViews = dbrDetailViews;
    }

    public BigDecimal getIncomeFactor() {
        return incomeFactor;
    }

    public void setIncomeFactor(BigDecimal incomeFactor) {
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

    public long getWorkCaseId() {
        return workCaseId;
    }

    public void setWorkCaseId(long workCaseId) {
        this.workCaseId = workCaseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("dbrDetailViews", dbrDetailViews)
                .append("incomeFactor", incomeFactor)
                .append("monthlyIncome", monthlyIncome)
                .append("monthlyIncomeAdjust", monthlyIncomeAdjust)
                .append("monthlyIncomePerMonth", monthlyIncomePerMonth)
                .append("netMonthlyIncome", netMonthlyIncome)
                .append("currentDBR", currentDBR)
                .append("dbrBeforeRequest", dbrBeforeRequest)
                .append("dbrInterest", dbrInterest)
                .append("workCaseId", workCaseId)
                .append("userId", userId)
                .toString();
    }
}
