package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExSummaryView  implements Serializable {
    private long id;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;
    //Borrower Characteristic
    private List<ExSumCharacteristicView> exCharacteristicListView;
/*     private String customer;
    private BigDecimal currentDBR;
    private BigDecimal finalDBR;
    private BigDecimal income;
    private BigDecimal recommendedWCNeed;
    private BigDecimal actualWC;
    private Date startBusinessDate;
    private String yearInBusiness;
    private BigDecimal salePerYearBDM;
    private BigDecimal salePerYearUW;
    private BigDecimal groupSaleBDM;
    private BigDecimal groupSaleUW;
    private BigDecimal groupExposureBDM;
    private BigDecimal groupExposureUW;*/

    //Business Information Summary
    private List<ExSumBusinessInfoView> exSumBusinessInfoListView;
/*    private BigDecimal netFixAsset;
    private int     noOfEmployee;
    private String  bizProvince;
    private String  bizType;
    private String  bizGroup;
    private String  bizCode;
    private String  bizDesc;
    private String  qualitativeClass;
    private BigDecimal bizSize;
    private BigDecimal BDM;
    private BigDecimal UW;
    private BigDecimal AR;
    private BigDecimal AP;
    private BigDecimal INV;*/

    private String businessOperationActivity;
    private String businessPermission;
    private Date expiryDate;

    private String applicationResult;
    private List<ExSumDecisionView> exSumDecisionListView;

    private String natureOfBusiness;
    private String historicalAndReasonOfChange;
    private String tmbCreditHistory;
    private String supportReason;
    private int    rm008Code;     //radio
    private String rm008Remark;
    private int    rm204Code;     //radio
    private String rm204Remark;
    private int    rm020Code;    //radio
    private String rm020Remark;

    private String uwCode;
    private int    decision;    //radio

    private String reasonCode;
    private String reasonDesc;
    private String uwComment;


    public  ExSummaryView(){}

    public void reset(){
        exSumDecisionListView = new ArrayList<ExSumDecisionView>();
        exCharacteristicListView = new ArrayList<ExSumCharacteristicView>();
        exSumBusinessInfoListView = new ArrayList<ExSumBusinessInfoView>();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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


    public String getBusinessOperationActivity() {
        return businessOperationActivity;
    }

    public void setBusinessOperationActivity(String businessOperationActivity) {
        this.businessOperationActivity = businessOperationActivity;
    }

    public String getBusinessPermission() {
        return businessPermission;
    }

    public void setBusinessPermission(String businessPermission) {
        this.businessPermission = businessPermission;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getApplicationResult() {
        return applicationResult;
    }

    public void setApplicationResult(String applicationResult) {
        this.applicationResult = applicationResult;
    }


    public String getNatureOfBusiness() {
        return natureOfBusiness;
    }

    public void setNatureOfBusiness(String natureOfBusiness) {
        this.natureOfBusiness = natureOfBusiness;
    }

    public String getHistoricalAndReasonOfChange() {
        return historicalAndReasonOfChange;
    }

    public void setHistoricalAndReasonOfChange(String historicalAndReasonOfChange) {
        this.historicalAndReasonOfChange = historicalAndReasonOfChange;
    }

    public String getTmbCreditHistory() {
        return tmbCreditHistory;
    }

    public void setTmbCreditHistory(String tmbCreditHistory) {
        this.tmbCreditHistory = tmbCreditHistory;
    }

    public String getSupportReason() {
        return supportReason;
    }

    public void setSupportReason(String supportReason) {
        this.supportReason = supportReason;
    }

    public int getRm008Code() {
        return rm008Code;
    }

    public void setRm008Code(int rm008Code) {
        this.rm008Code = rm008Code;
    }

    public String getRm008Remark() {
        return rm008Remark;
    }

    public void setRm008Remark(String rm008Remark) {
        this.rm008Remark = rm008Remark;
    }

    public int getRm204Code() {
        return rm204Code;
    }

    public void setRm204Code(int rm204Code) {
        this.rm204Code = rm204Code;
    }

    public String getRm204Remark() {
        return rm204Remark;
    }

    public void setRm204Remark(String rm204Remark) {
        this.rm204Remark = rm204Remark;
    }

    public int getRm020Code() {
        return rm020Code;
    }

    public void setRm020Code(int rm020Code) {
        this.rm020Code = rm020Code;
    }

    public String getRm020Remark() {
        return rm020Remark;
    }

    public void setRm020Remark(String rm020Remark) {
        this.rm020Remark = rm020Remark;
    }

    public String getUwCode() {
        return uwCode;
    }

    public void setUwCode(String uwCode) {
        this.uwCode = uwCode;
    }

    public int getDecision() {
        return decision;
    }

    public void setDecision(int decision) {
        this.decision = decision;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonDesc() {
        return reasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        this.reasonDesc = reasonDesc;
    }

    public String getUwComment() {
        return uwComment;
    }

    public void setUwComment(String uwComment) {
        this.uwComment = uwComment;
    }

    public List<ExSumDecisionView> getExSumDecisionListView() {
        return exSumDecisionListView;
    }

    public void setExSumDecisionListView(List<ExSumDecisionView> exSumDecisionListView) {
        exSumDecisionListView = exSumDecisionListView;
    }

    public List<ExSumCharacteristicView> getExCharacteristicListView() {
        return exCharacteristicListView;
    }

    public void setExCharacteristicListView(List<ExSumCharacteristicView> exCharacteristicListView) {
        this.exCharacteristicListView = exCharacteristicListView;
    }

    public List<ExSumBusinessInfoView> getExSumBusinessInfoListView() {
        return exSumBusinessInfoListView;
    }

    public void setExSumBusinessInfoListView(List<ExSumBusinessInfoView> exSumBusinessInfoListView) {
        this.exSumBusinessInfoListView = exSumBusinessInfoListView;
    }
}
