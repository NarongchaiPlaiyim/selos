package com.clevel.selos.model.report;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RejectLetterCancelCodeByExSum {

    private int exSumNCB;
    private int exSumPolicy;
    private int exSumIncome;

    public RejectLetterCancelCodeByExSum() {
    }

    public int getExSumNCB() {
        return exSumNCB;
    }

    public void setExSumNCB(int exSumNCB) {
        this.exSumNCB = exSumNCB;
    }

    public int getExSumPolicy() {
        return exSumPolicy;
    }

    public void setExSumPolicy(int exSumPolicy) {
        this.exSumPolicy = exSumPolicy;
    }

    public int getExSumIncome() {
        return exSumIncome;
    }

    public void setExSumIncome(int exSumIncome) {
        this.exSumIncome = exSumIncome;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("exSumNCB", exSumNCB)
                .append("exSumPolicy", exSumPolicy)
                .append("exSumIncome", exSumIncome)
                .toString();
    }
}
