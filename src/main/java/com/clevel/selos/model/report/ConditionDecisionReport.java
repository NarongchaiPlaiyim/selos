package com.clevel.selos.model.report;

import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ConditionDecisionReport extends ReportModel{

    private int count;
    private String loanType;
    private String conditionDesc;

    public ConditionDecisionReport() {
        count = 0;
        loanType = "";
        conditionDesc = "";
    }

    public String getConditionDesc() {
        return conditionDesc;
    }

    public void setConditionDesc(String conditionDesc) {
        this.conditionDesc = conditionDesc;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("conditionDesc", conditionDesc)
                .append("count", count)
                .append("loanType", loanType)
                .toString();
    }
}
