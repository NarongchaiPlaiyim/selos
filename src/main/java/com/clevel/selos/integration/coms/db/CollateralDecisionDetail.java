package com.clevel.selos.integration.coms.db;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class CollateralDecisionDetail implements Serializable {
    private String reasonCondNo; // COND_NO,REA
    private String reasonCondRemark; // COND_REMARK,REA
    private String condNo; // COND_NO,CON
    private String condRemark; // COND_REMARK,CON

    public String getReasonCondNo() {
        return reasonCondNo;
    }

    public void setReasonCondNo(String reasonCondNo) {
        this.reasonCondNo = reasonCondNo;
    }

    public String getReasonCondRemark() {
        return reasonCondRemark;
    }

    public void setReasonCondRemark(String reasonCondRemark) {
        this.reasonCondRemark = reasonCondRemark;
    }

    public String getCondNo() {
        return condNo;
    }

    public void setCondNo(String condNo) {
        this.condNo = condNo;
    }

    public String getCondRemark() {
        return condRemark;
    }

    public void setCondRemark(String condRemark) {
        this.condRemark = condRemark;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("reasonCondNo", reasonCondNo)
                .append("reasonCondRemark", reasonCondRemark)
                .append("condNo", condNo)
                .append("condRemark", condRemark)
                .toString();
    }
}
