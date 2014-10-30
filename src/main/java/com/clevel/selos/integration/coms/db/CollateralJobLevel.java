package com.clevel.selos.integration.coms.db;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class CollateralJobLevel implements Serializable {
    private String jobNo; //1. JOB_NO
    private Date curApprDate; //2. CUR_APPR_DATE
    private String isMATI; //3. IS_MATI
    private String decision; //4. DECISION
    private String usages;

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    public Date getCurApprDate() {
        return curApprDate;
    }

    public void setCurApprDate(Date curApprDate) {
        this.curApprDate = curApprDate;
    }

    public String getMATI() {
        return isMATI;
    }

    public void setMATI(String MATI) {
        isMATI = MATI;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getUsages() {
        return usages;
    }

    public void setUsages(String usages) {
        this.usages = usages;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("jobNo", jobNo)
                .append("curApprDate", curApprDate)
                .append("isMATI", isMATI)
                .append("decision", decision)
                .append("usages", usages)
                .toString();
    }
}
