package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;

import java.util.Date;
import java.util.List;

public class CollateralDetailView {
    private long id;
    private String jobIDSearch;
    private String jobID;
    private int no;
    private Date appraisalDate;
    private String AADDecision;
    private String AADDecisionReason;
    private String AADDecisionReasonDetail;
    private String usage;
    private String typeOfUsage;
    private String mortgageCondition;
    private String mortgageConditionDetail;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    List<CollateralHeaderDetailView> collateralHeaderDetailViewList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJobIDSearch() {
        return jobIDSearch;
    }

    public void setJobIDSearch(String jobIDSearch) {
        this.jobIDSearch = jobIDSearch;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {
        this.jobID = jobID;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
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
}
