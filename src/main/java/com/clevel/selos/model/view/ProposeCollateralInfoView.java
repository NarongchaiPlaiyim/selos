package com.clevel.selos.model.view;

import com.clevel.selos.model.DecisionType;
import com.clevel.selos.model.ProposeType;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProposeCollateralInfoView implements Serializable {
    private long id;
    private DecisionType uwDecision;
    private String jobID;
    private Date appraisalDate;
    private int numberMonthsFromApprDate;
    private String aadDecision;
    private String aadDecisionReason;
    private String aadDecisionReasonDetail;
    private String usage;
    private String typeOfUsage;
    private String uwRemark;
    private String mortgageCondition;
    private String mortgageConditionDetail;
    private String bdmComments;
    private BigDecimal premiumAmount;
    private ProposeType proposeType;
    private boolean coms;

    private int appraisalRequest;

    private List<ProposeCollateralInfoHeadView> proposeCollateralInfoHeadViewList;
    private List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList;

    public ProposeCollateralInfoView(){
        reset();
    }

    public void reset(){
        this.uwDecision = DecisionType.NO_DECISION;
        this.jobID = "";
        this.appraisalDate = null;
        this.numberMonthsFromApprDate = 0;
        this.aadDecision = "";
        this.aadDecisionReason = "";
        this.aadDecisionReasonDetail = "";
        this.usage = "";
        this.typeOfUsage = "";
        this.uwRemark = "";
        this.mortgageCondition = "";
        this.mortgageConditionDetail = "";
        this.bdmComments= "";
        this.premiumAmount = BigDecimal.ZERO;
        this.proposeType= ProposeType.P;
        this.coms = false;
        this.proposeCollateralInfoHeadViewList = new ArrayList<ProposeCollateralInfoHeadView>();
        ProposeCollateralInfoHeadView proposeCollateralInfoHeadView = new ProposeCollateralInfoHeadView();
        this.proposeCollateralInfoHeadViewList.add(proposeCollateralInfoHeadView);
        this.proposeCreditInfoDetailViewList = new ArrayList<ProposeCreditInfoDetailView>();
        this.appraisalRequest = 1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public Date getAppraisalDate() {
        return appraisalDate;
    }

    public void setAppraisalDate(Date appraisalDate) {
        this.appraisalDate = appraisalDate;
    }

    public int getNumberMonthsFromApprDate() {
        return numberMonthsFromApprDate;
    }

    public void setNumberMonthsFromApprDate(int numberMonthsFromApprDate) {
        this.numberMonthsFromApprDate = numberMonthsFromApprDate;
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

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getTypeOfUsage() {
        return typeOfUsage;
    }

    public void setTypeOfUsage(String typeOfUsage) {
        this.typeOfUsage = typeOfUsage;
    }

    public DecisionType getUwDecision() {
        return uwDecision;
    }

    public void setUwDecision(DecisionType uwDecision) {
        this.uwDecision = uwDecision;
    }

    public String getUwRemark() {
        return uwRemark;
    }

    public void setUwRemark(String uwRemark) {
        this.uwRemark = uwRemark;
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

    public String getBdmComments() {
        return bdmComments;
    }

    public void setBdmComments(String bdmComments) {
        this.bdmComments = bdmComments;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public void setPremiumAmount(BigDecimal premiumAmount) {
        this.premiumAmount = premiumAmount;
    }

    public ProposeType getProposeType() {
        return proposeType;
    }

    public void setProposeType(ProposeType proposeType) {
        this.proposeType = proposeType;
    }

    public boolean isComs() {
        return coms;
    }

    public void setComs(boolean coms) {
        this.coms = coms;
    }

    public List<ProposeCollateralInfoHeadView> getProposeCollateralInfoHeadViewList() {
        return proposeCollateralInfoHeadViewList;
    }

    public void setProposeCollateralInfoHeadViewList(List<ProposeCollateralInfoHeadView> proposeCollateralInfoHeadViewList) {
        this.proposeCollateralInfoHeadViewList = proposeCollateralInfoHeadViewList;
    }

    public List<ProposeCreditInfoDetailView> getProposeCreditInfoDetailViewList() {
        return proposeCreditInfoDetailViewList;
    }

    public void setProposeCreditInfoDetailViewList(List<ProposeCreditInfoDetailView> proposeCreditInfoDetailViewList) {
        this.proposeCreditInfoDetailViewList = proposeCreditInfoDetailViewList;
    }

    public int getAppraisalRequest() {
        return appraisalRequest;
    }

    public void setAppraisalRequest(int appraisalRequest) {
        this.appraisalRequest = appraisalRequest;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("uwDecision", uwDecision)
                .append("jobID", jobID)
                .append("appraisalDate", appraisalDate)
                .append("numberMonthsFromApprDate", numberMonthsFromApprDate)
                .append("aadDecision", aadDecision)
                .append("aadDecisionReason", aadDecisionReason)
                .append("aadDecisionReasonDetail", aadDecisionReasonDetail)
                .append("usage", usage)
                .append("typeOfUsage", typeOfUsage)
                .append("uwRemark", uwRemark)
                .append("mortgageCondition", mortgageCondition)
                .append("mortgageConditionDetail", mortgageConditionDetail)
                .append("bdmComments", bdmComments)
                .append("premiumAmount", premiumAmount)
                .append("proposeType", proposeType)
                .append("coms", coms)
                .append("appraisalRequest", appraisalRequest)
                .append("proposeCollateralInfoHeadViewList", proposeCollateralInfoHeadViewList)
                .append("proposeCreditInfoDetailViewList", proposeCreditInfoDetailViewList)
                .toString();
    }
}
