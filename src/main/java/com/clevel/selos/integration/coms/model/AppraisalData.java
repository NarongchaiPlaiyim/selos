package com.clevel.selos.integration.coms.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AppraisalData implements Serializable {
    private String jobId;
    private Date appraisalDate;
    private String isMATI;
    private String aadDecision;
    private String aadDecisionReason;
    private String aadDecisionReasonDetail;
    private String mortgageCondition;
    private String mortgageConditionDetail;
    private List<HeadCollateralData> headCollateralDataList;
    private List<SubCollateralData> subCollateralDataList;

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Date getAppraisalDate() {
        return appraisalDate;
    }

    public void setAppraisalDate(Date appraisalDate) {
        this.appraisalDate = appraisalDate;
    }

    public String getMATI() {
        return isMATI;
    }

    public void setMATI(String MATI) {
        isMATI = MATI;
    }

    public String getAadDecision() {
        return aadDecision;
    }

    public void setAadDecision(String aadDecision) {
        this.aadDecision = aadDecision;
    }

    public String getAadDecisionReason() {
        return aadDecisionReason;
    }

    public void setAadDecisionReason(String aadDecisionReason) {
        this.aadDecisionReason = aadDecisionReason;
    }

    public String getAadDecisionReasonDetail() {
        return aadDecisionReasonDetail;
    }

    public void setAadDecisionReasonDetail(String aadDecisionReasonDetail) {
        this.aadDecisionReasonDetail = aadDecisionReasonDetail;
    }

    public String getMortgageCondition() {
        return mortgageCondition;
    }

    public void setMortgageCondition(String mortgageCondition) {
        this.mortgageCondition = mortgageCondition;
    }

    public String getMortgageConditionDetail() {
        return mortgageConditionDetail;
    }

    public void setMortgageConditionDetail(String mortgageConditionDetail) {
        this.mortgageConditionDetail = mortgageConditionDetail;
    }

    public List<HeadCollateralData> getHeadCollateralDataList() {
        return headCollateralDataList;
    }

    public void setHeadCollateralDataList(List<HeadCollateralData> headCollateralDataList) {
        this.headCollateralDataList = headCollateralDataList;
    }

    public List<SubCollateralData> getSubCollateralDataList() {
        return subCollateralDataList;
    }

    public void setSubCollateralDataList(List<SubCollateralData> subCollateralDataList) {
        this.subCollateralDataList = subCollateralDataList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("jobId", jobId)
                .append("appraisalDate", appraisalDate)
                .append("isMATI", isMATI)
                .append("aadDecision", aadDecision)
                .append("aadDecisionReason", aadDecisionReason)
                .append("aadDecisionReasonDetail", aadDecisionReasonDetail)
                .append("mortgageCondition", mortgageCondition)
                .append("mortgageConditionDetail", mortgageConditionDetail)
                .append("headCollateralDataList", headCollateralDataList)
                .append("subCollateralDataList", subCollateralDataList)
                .toString();
    }
}
