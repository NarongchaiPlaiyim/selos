package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ProposeCollateralInfoView implements Serializable {
    private long id;
    private long jobID;
    private Date appraisalDate;
    private String  AADDecision;
    private String  AADDecisionReason;
    private String  AADDecisionReasonDetail;
    private String  usage;
    private String  typeOfUsage;
    private String  UWDecision;
    private String  UWRemark;
    private String  mortgageCondition;
    private String  mortgageConditionDetail;
    private String  BDMComments;
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

    public long getJobID() {
        return jobID;
    }

    public void setJobID(long jobID) {
        this.jobID = jobID;
    }

    public Date getAppraisalDate() {
        return appraisalDate;
    }

    public void setAppraisalDate(Date appraisalDate) {
        this.appraisalDate = appraisalDate;
    }

    public String getAADDecision() {
        return AADDecision;
    }

    public void setAADDecision(String AADDecision) {
        this.AADDecision = AADDecision;
    }

    public String getAADDecisionReason() {
        return AADDecisionReason;
    }

    public void setAADDecisionReason(String AADDecisionReason) {
        this.AADDecisionReason = AADDecisionReason;
    }

    public String getAADDecisionReasonDetail() {
        return AADDecisionReasonDetail;
    }

    public void setAADDecisionReasonDetail(String AADDecisionReasonDetail) {
        this.AADDecisionReasonDetail = AADDecisionReasonDetail;
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

    public String getUWDecision() {
        return UWDecision;
    }

    public void setUWDecision(String UWDecision) {
        this.UWDecision = UWDecision;
    }

    public String getUWRemark() {
        return UWRemark;
    }

    public void setUWRemark(String UWRemark) {
        this.UWRemark = UWRemark;
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

    public String getBDMComments() {
        return BDMComments;
    }

    public void setBDMComments(String BDMComments) {
        this.BDMComments = BDMComments;
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
}
