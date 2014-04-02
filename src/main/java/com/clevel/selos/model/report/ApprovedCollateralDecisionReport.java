package com.clevel.selos.model.report;

import com.clevel.selos.model.view.NewCollateralSubView;
import com.clevel.selos.model.view.ProposeCreditDetailView;
import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ApprovedCollateralDecisionReport extends ReportModel{
    //Collateral
    private String jobID;
    private Date appraisalDate;
    private String aadDecision;
    private String aadDecisionReason;
    private String aadDecisionReasonDetail;
    private String usage;
    private String typeOfUsage;
    private String mortgageCondition;
    private String  mortgageConditionDetail;
    private String bdmComments;
    private String path;
    private String UwDecision;
    private String approved;

    private List<ProposeCreditDetailView> proposeCreditDetailViewList;

    //Coll Head
    private String collateralDescription;
    private String percentLTVDescription;
    private BigDecimal existingCredit;
    private String titleDeed;
    private String collateralLocation;
    private BigDecimal appraisalValue;
    private String collTypeDescription;
    private String headCollTypeDescription;
    private String insuranceCompany;

    //Sub Collateral
    private List<NewCollateralSubView> subViewList;


    public ApprovedCollateralDecisionReport() {
        jobID = "";
        aadDecision = "";
        aadDecisionReason = "";
        aadDecisionReasonDetail = "";
        usage = "";
        typeOfUsage = "";
        mortgageCondition = "";
        mortgageConditionDetail = "";
        proposeCreditDetailViewList = new ArrayList<ProposeCreditDetailView>();
        subViewList = new ArrayList<NewCollateralSubView>();
        bdmComments = "";
        collateralDescription = "";
        percentLTVDescription = "";
        titleDeed = "";
        collateralLocation = "";
        collTypeDescription = "";
        headCollTypeDescription = "";
        insuranceCompany = "";
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

    public Date getAppraisalDate() {
        return appraisalDate;
    }

    public void setAppraisalDate(Date appraisalDate) {
        this.appraisalDate = appraisalDate;
    }

    public BigDecimal getAppraisalValue() {
        return appraisalValue;
    }

    public void setAppraisalValue(BigDecimal appraisalValue) {
        this.appraisalValue = appraisalValue;
    }

    public String getBdmComments() {
        return bdmComments;
    }

    public void setBdmComments(String bdmComments) {
        this.bdmComments = bdmComments;
    }

    public String getCollateralDescription() {
        return collateralDescription;
    }

    public void setCollateralDescription(String collateralDescription) {
        this.collateralDescription = collateralDescription;
    }

    public String getCollateralLocation() {
        return collateralLocation;
    }

    public void setCollateralLocation(String collateralLocation) {
        this.collateralLocation = collateralLocation;
    }

    public String getCollTypeDescription() {
        return collTypeDescription;
    }

    public void setCollTypeDescription(String collTypeDescription) {
        this.collTypeDescription = collTypeDescription;
    }

    public BigDecimal getExistingCredit() {
        return existingCredit;
    }

    public void setExistingCredit(BigDecimal existingCredit) {
        this.existingCredit = existingCredit;
    }

    public String getInsuranceCompany() {
        return insuranceCompany;
    }

    public void setInsuranceCompany(String insuranceCompany) {
        this.insuranceCompany = insuranceCompany;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
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


    public String getPercentLTVDescription() {
        return percentLTVDescription;
    }

    public void setPercentLTVDescription(String percentLTVDescription) {
        this.percentLTVDescription = percentLTVDescription;
    }

    public List<ProposeCreditDetailView> getProposeCreditDetailViewList() {
        return proposeCreditDetailViewList;
    }

    public void setProposeCreditDetailViewList(List<ProposeCreditDetailView> proposeCreditDetailViewList) {
        this.proposeCreditDetailViewList = proposeCreditDetailViewList;
    }

    public String getTitleDeed() {
        return titleDeed;
    }

    public void setTitleDeed(String titleDeed) {
        this.titleDeed = titleDeed;
    }

    public String getTypeOfUsage() {
        return typeOfUsage;
    }

    public void setTypeOfUsage(String typeOfUsage) {
        this.typeOfUsage = typeOfUsage;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public List<NewCollateralSubView> getSubViewList() {
        return subViewList;
    }

    public void setSubViewList(List<NewCollateralSubView> subViewList) {
        this.subViewList = subViewList;
    }

    public String getHeadCollTypeDescription() {
        return headCollTypeDescription;
    }

    public void setHeadCollTypeDescription(String headCollTypeDescription) {
        this.headCollTypeDescription = headCollTypeDescription;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUwDecision() {
        return UwDecision;
    }

    public void setUwDecision(String uwDecision) {
        UwDecision = uwDecision;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("jobID", jobID)
                .append("appraisalDate", appraisalDate)
                .append("aadDecision", aadDecision)
                .append("aadDecisionReason", aadDecisionReason)
                .append("aadDecisionReasonDetail", aadDecisionReasonDetail)
                .append("usage", usage)
                .append("typeOfUsage", typeOfUsage)
                .append("mortgageCondition", mortgageCondition)
                .append("mortgageConditionDetail", mortgageConditionDetail)
                .append("bdmComments", bdmComments)
                .append("path", path)
                .append("UwDecision", UwDecision)
                .append("approved", approved)
                .append("proposeCreditDetailViewList", proposeCreditDetailViewList)
                .append("collateralDescription", collateralDescription)
                .append("percentLTVDescription", percentLTVDescription)
                .append("existingCredit", existingCredit)
                .append("titleDeed", titleDeed)
                .append("collateralLocation", collateralLocation)
                .append("appraisalValue", appraisalValue)
                .append("collTypeDescription", collTypeDescription)
                .append("headCollTypeDescription", headCollTypeDescription)
                .append("insuranceCompany", insuranceCompany)
                .append("subViewList", subViewList)
                .toString();
    }
}
