package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProposeCollateralInfoView implements Serializable {
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

    private List<CollateralHeaderDetailView> collateralHeaderDetailViewList;
    private CollateralHeaderDetailView collateralHeaderDetailView;

    private List<CreditTypeDetailView> creditTypeDetailViewList;
    private List<CollateralDetailView> CollateralDetailViewList;

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

    public List<CollateralHeaderDetailView> getCollateralHeaderDetailViewList() {
        return collateralHeaderDetailViewList;
    }

    public void setCollateralHeaderDetailViewList(List<CollateralHeaderDetailView> collateralHeaderDetailViewList) {
        this.collateralHeaderDetailViewList = collateralHeaderDetailViewList;
    }

    public CollateralHeaderDetailView getCollateralHeaderDetailView() {
        return collateralHeaderDetailView;
    }

    public void setCollateralHeaderDetailView(CollateralHeaderDetailView collateralHeaderDetailView) {
        this.collateralHeaderDetailView = collateralHeaderDetailView;
    }

    public List<CreditTypeDetailView> getCreditTypeDetailViewList() {
        return creditTypeDetailViewList;
    }

    public void setCreditTypeDetailViewList(List<CreditTypeDetailView> creditTypeDetailViewList) {
        this.creditTypeDetailViewList = creditTypeDetailViewList;
    }

    public List<CollateralDetailView> getCollateralDetailViewList() {
        return CollateralDetailViewList;
    }

    public void setCollateralDetailViewList(List<CollateralDetailView> collateralDetailViewList) {
        CollateralDetailViewList = collateralDetailViewList;
    }
}
