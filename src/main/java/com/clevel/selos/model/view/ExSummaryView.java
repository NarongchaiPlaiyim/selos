package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.AuthorizationDOA;
import com.clevel.selos.model.db.master.User;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExSummaryView  implements Serializable {
    private long id;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    //Borrower
    private List<CustomerInfoView> borrowerListView;         //wo transform
    private String businessLocationName;
    private String businessLocationAddress;
    private String businessLocationAddressEN;
    private String owner;

    //Trade Finance


    //Borrower Characteristic & Business Information Summary
    private ExSumCharacteristicView exSumCharacteristicView;
    private ExSumBusinessInfoView exSumBusinessInfoView;
    private String businessOperationActivity;
    private String businessPermission;
    private Date expiryDate;

    //NCB Record
    private List<NCBInfoView> ncbInfoListView;               //wo transform

    //Account Movement
    private List<ExSumAccountMovementView> exSumAccMovementViewList;

    //Collateral
    private ExSumCollateralView exSumCollateralView;

    //Deviate
    private String applicationResult;
    private List<ExSumDecisionView> exSumDecisionListView;

    //Business Overview and Support Decision
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

    //UW Decision and Explanation
    private AuthorizationDOA approveAuthority;
    private String uwCode;                          //wo transform
    private int decision;    //radio                //wo transform
    private List<ExSumReasonView> deviateCode;

    //UW Comment
    private String uwComment;

    public  ExSummaryView(){}

    public void reset(){
        borrowerListView = new ArrayList<CustomerInfoView>();
        ncbInfoListView = new ArrayList<NCBInfoView>();
        exSumDecisionListView = new ArrayList<ExSumDecisionView>();
        exSumCharacteristicView = new ExSumCharacteristicView();
        exSumBusinessInfoView = new ExSumBusinessInfoView();
        exSumAccMovementViewList = new ArrayList<ExSumAccountMovementView>();
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

    public ExSumCharacteristicView getExSumCharacteristicView() {
        return exSumCharacteristicView;
    }

    public void setExSumCharacteristicView(ExSumCharacteristicView exSumCharacteristicView) {
        this.exSumCharacteristicView = exSumCharacteristicView;
    }

    public List<ExSumAccountMovementView> getExSumAccMovementViewList() {
        return exSumAccMovementViewList;
    }

    public void setExSumAccMovementViewList(List<ExSumAccountMovementView> exSumAccMovementViewList) {
        this.exSumAccMovementViewList = exSumAccMovementViewList;
    }

    public ExSumBusinessInfoView getExSumBusinessInfoView() {
        return exSumBusinessInfoView;
    }

    public void setExSumBusinessInfoView(ExSumBusinessInfoView exSumBusinessInfoView) {
        this.exSumBusinessInfoView = exSumBusinessInfoView;
    }

    public List<CustomerInfoView> getBorrowerListView() {
        return borrowerListView;
    }

    public void setBorrowerListView(List<CustomerInfoView> borrowerListView) {
        this.borrowerListView = borrowerListView;
    }

    public List<NCBInfoView> getNcbInfoListView() {
        return ncbInfoListView;
    }

    public void setNcbInfoListView(List<NCBInfoView> ncbInfoListView) {
        this.ncbInfoListView = ncbInfoListView;
    }

    public ExSumCollateralView getExSumCollateralView() {
        return exSumCollateralView;
    }

    public void setExSumCollateralView(ExSumCollateralView exSumCollateralView) {
        this.exSumCollateralView = exSumCollateralView;
    }

    public AuthorizationDOA getApproveAuthority() {
        return approveAuthority;
    }

    public void setApproveAuthority(AuthorizationDOA approveAuthority) {
        this.approveAuthority = approveAuthority;
    }

    public List<ExSumReasonView> getDeviateCode() {
        return deviateCode;
    }

    public void setDeviateCode(List<ExSumReasonView> deviateCode) {
        this.deviateCode = deviateCode;
    }

    public String getBusinessLocationName() {
        return businessLocationName;
    }

    public void setBusinessLocationName(String businessLocationName) {
        this.businessLocationName = businessLocationName;
    }

    public String getBusinessLocationAddress() {
        return businessLocationAddress;
    }

    public void setBusinessLocationAddress(String businessLocationAddress) {
        this.businessLocationAddress = businessLocationAddress;
    }

    public String getBusinessLocationAddressEN() {
        return businessLocationAddressEN;
    }

    public void setBusinessLocationAddressEN(String businessLocationAddressEN) {
        this.businessLocationAddressEN = businessLocationAddressEN;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
