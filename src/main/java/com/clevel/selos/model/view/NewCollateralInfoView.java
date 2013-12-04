package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewCollateralInfoView implements Serializable {
    private long id;
    private String jobID;
    private Date appraisalDate;
    private String  aadDecision;
    private String  aadDecisionReason;
    private String  aadDecisionReasonDetail;
    private String  usage;
    private String  typeOfUsage;
    private String  uwDecision;
    private String  uwRemark;
    private String  mortgageCondition;
    private String  mortgageConditionDetail;
    private String  bdmComments;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    private List<NewCollateralHeadDetailView> newCollateralHeadDetailViewList;
    private List<CreditTypeDetailView> creditTypeDetailViewList;

    public NewCollateralInfoView(){
        reset();
    }

    public void reset(){
        this.jobID = "";
        this.appraisalDate = new Date();
        this.aadDecision = "";
        this.aadDecisionReason = "";
        this.aadDecisionReasonDetail = "";
        this.usage = "";
        this.typeOfUsage = "";
        this.uwDecision = "";
        this.uwRemark = "";
        this.mortgageCondition = "";
        this.mortgageConditionDetail = "";
        this.bdmComments= "";

        this.newCollateralHeadDetailViewList = new ArrayList<NewCollateralHeadDetailView>();
        this.creditTypeDetailViewList = new ArrayList<CreditTypeDetailView>();
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

    public String getUwDecision() {
        return uwDecision;
    }

    public void setUwDecision(String uwDecision) {
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    public List<NewCollateralHeadDetailView> getNewCollateralHeadDetailViewList() {
        return newCollateralHeadDetailViewList;
    }

    public void setNewCollateralHeadDetailViewList(List<NewCollateralHeadDetailView> newCollateralHeadDetailViewList) {
        this.newCollateralHeadDetailViewList = newCollateralHeadDetailViewList;
    }

    public List<CreditTypeDetailView> getCreditTypeDetailViewList() {
        return creditTypeDetailViewList;
    }

    public void setCreditTypeDetailViewList(List<CreditTypeDetailView> creditTypeDetailViewList) {
        this.creditTypeDetailViewList = creditTypeDetailViewList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("jobID", jobID)
                .append("appraisalDate", appraisalDate)
                .append("aadDecision", aadDecision)
                .append("aadDecisionReason", aadDecisionReason)
                .append("aadDecisionReasonDetail", aadDecisionReasonDetail)
                .append("usage", usage)
                .append("typeOfUsage", typeOfUsage)
                .append("uwDecision", uwDecision)
                .append("uwRemark", uwRemark)
                .append("mortgageCondition", mortgageCondition)
                .append("mortgageConditionDetail", mortgageConditionDetail)
                .append("bdmComments", bdmComments)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("newCollateralHeadDetailViewList", newCollateralHeadDetailViewList)
                .append("creditTypeDetailViewList", creditTypeDetailViewList)
                .toString();
    }
}
