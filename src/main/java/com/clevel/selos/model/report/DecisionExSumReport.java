package com.clevel.selos.model.report;

import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


public class DecisionExSumReport extends ReportModel{

    private String colorResult;
    private String applicationResult;

    private long id;
    private String flag;
    private String group;
    private String ruleName;
    private String cusName;
    private String deviationReason;

    public DecisionExSumReport() {
        this.flag = "";
        this.group = "";
        this.cusName = "";
        this.deviationReason = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getDeviationReason() {
        return deviationReason;
    }

    public void setDeviationReason(String deviationReason) {
        this.deviationReason = deviationReason;
    }

    public String getColorResult() {
        return colorResult;
    }

    public void setColorResult(String colorResult) {
        this.colorResult = colorResult;
    }

    public String getApplicationResult() {
        return applicationResult;
    }

    public void setApplicationResult(String applicationResult) {
        this.applicationResult = applicationResult;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("colorResult", colorResult)
                .append("applicationResult", applicationResult)
                .append("id", id)
                .append("flag", flag)
                .append("group", group)
                .append("ruleName", ruleName)
                .append("cusName", cusName)
                .append("deviationReason", deviationReason)
                .toString();
    }
}
