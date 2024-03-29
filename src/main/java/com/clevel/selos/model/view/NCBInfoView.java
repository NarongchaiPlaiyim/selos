package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.TDRCondition;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

public class NCBInfoView implements Serializable {
    private long id;
    private long customerId;
    private String ncbCusMarriageStatus;
    private Date checkingDate;
    private Date enquiryDate;
    private Date ncbLastInfoAsOfDate;
    private String enquiry;
    private int checkIn6Month;
    private String paymentClass;
    private String personalId;
    private String currentPaymentType;
    private String historyPaymentType;
    private int nplFlag;
    private String nplFlagText;
    private boolean nplTMBFlag;
    private int nplTMBMonth;
    private String nplTMBMonthStr;
    private int nplTMBYear;
    private boolean nplOtherFlag;
    private int nplOtherMonth;
    private String nplOtherMonthStr;
    private int nplOtherYear;
    private int tdrFlag;
    private String tdrFlagText;
    private boolean tdrTMBFlag;
    private int tdrTMBMonth;
    private String tdrTMBMonthStr;
    private int tdrTMBYear;
    private boolean tdrOtherFlag;
    private int tdrOtherMonth;
    private String tdrOtherMonthStr;
    private int tdrOtherYear;
    private String remark;
    private TDRCondition tdrCondition;
    private String ncbCusName;
    private AddressView ncbCusAddress;
    private String ncbFlag;
    private boolean active;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;

    private boolean nplTMBFlagNCB;
    private boolean nplOtherFlagNCB;
    private boolean tdrTMBFlagNCB;
    private boolean tdrOtherFlagNCB;
    private Date nplTMBDateNCB;
    private Date nplOtherDateNCB;
    private Date tdrTMBDateNCB;
    private Date tdrOtherDateNCB;


    //** dbr **//
    private NCBDetailView ncbDetailView;
    private CustomerInfoView customerInfoView;
    private LoanAccountTypeView loanAccountTypeView;


    public NCBInfoView() {
    }

    public void reset() {
        this.checkingDate = new Date();
        this.enquiryDate = new Date();
        this.checkIn6Month = 0;
        this.paymentClass = "";
        this.personalId = "";
        this.currentPaymentType = "";
        this.historyPaymentType = "";
        this.nplFlag = 0;
        this.nplTMBFlag = false;
        this.nplTMBMonth = 0;
        this.nplTMBMonthStr = "";
        this.nplTMBYear = 0;
        this.nplOtherFlag = false;
        this.nplOtherMonth = 0;
        this.nplOtherMonthStr = "";
        this.nplOtherYear = 0;
        this.tdrFlag = 0;
        this.tdrTMBFlag = false;
        this.tdrTMBMonth = 0;
        this.tdrTMBMonthStr = "";
        this.tdrTMBYear = 0;
        this.tdrOtherFlag = false;
        this.tdrOtherMonth = 0;
        this.tdrOtherMonthStr = "";
        this.tdrOtherYear = 0;
        this.remark = "";
        this.tdrCondition = new TDRCondition();
        this.ncbCusName = "";
        this.ncbCusAddress = new AddressView();
        this.ncbDetailView = new NCBDetailView();
        this.ncbFlag = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public Date getCheckingDate() {
        return checkingDate;
    }

    public void setCheckingDate(Date checkingDate) {
        this.checkingDate = checkingDate;
    }

    public int getCheckIn6Month() {
        return checkIn6Month;
    }

    public void setCheckIn6Month(int checkIn6Month) {
        this.checkIn6Month = checkIn6Month;
    }

    public String getPaymentClass() {
        return paymentClass;
    }

    public void setPaymentClass(String paymentClass) {
        this.paymentClass = paymentClass;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getCurrentPaymentType() {
        return currentPaymentType;
    }

    public void setCurrentPaymentType(String currentPaymentType) {
        this.currentPaymentType = currentPaymentType;
    }

    public String getHistoryPaymentType() {
        return historyPaymentType;
    }

    public void setHistoryPaymentType(String historyPaymentType) {
        this.historyPaymentType = historyPaymentType;
    }

    public int getNplTMBMonth() {
        if(nplTMBFlag == true){
            return nplTMBMonth;
        } else{
            return 0;
        }

    }

    public void setNplTMBMonth(int nplTMBMonth) {
        this.nplTMBMonth = nplTMBMonth;
    }

    public int getNplTMBYear() {
        if(nplTMBFlag == true){
            return nplTMBYear;
        } else{
            return 0;
        }

    }

    public void setNplTMBYear(int nplTMBYear) {
        this.nplTMBYear = nplTMBYear;
    }


    public int getNplOtherMonth() {
        if(nplOtherFlag == true){
            return nplOtherMonth;
        } else{
            return 0;
        }

    }

    public void setNplOtherMonth(int nplOtherMonth) {
        this.nplOtherMonth = nplOtherMonth;
    }

    public int getNplOtherYear() {
        if(nplOtherFlag == true){
            return nplOtherYear;
        } else{
            return 0;
        }

    }

    public void setNplOtherYear(int nplOtherYear) {
        this.nplOtherYear = nplOtherYear;
    }

    public int getTdrTMBMonth() {
        if(tdrTMBFlag == true){
            return tdrTMBMonth;
        }else{
            return 0;
        }

    }

    public void setTdrTMBMonth(int tdrTMBMonth) {
        this.tdrTMBMonth = tdrTMBMonth;
    }

    public int getTdrTMBYear() {
        if(tdrTMBFlag == true){
            return tdrTMBYear;
        }else{
            return 0;
        }

    }

    public void setTdrTMBYear(int tdrTMBYear) {
        this.tdrTMBYear = tdrTMBYear;
    }

    public int getTdrOtherMonth() {
        if(tdrOtherFlag == true){
            return tdrOtherMonth;
        }else{
            return 0;
        }

    }

    public void setTdrOtherMonth(int tdrOtherMonth) {
        this.tdrOtherMonth = tdrOtherMonth;
    }

    public int getTdrOtherYear() {
        if(tdrOtherFlag == true){
            return tdrOtherYear;
        }else{
            return 0;
        }

    }

    public void setTdrOtherYear(int tdrOtherYear) {
        this.tdrOtherYear = tdrOtherYear;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public TDRCondition getTdrCondition() {
        return tdrCondition;
    }

    public void setTdrCondition(TDRCondition tdrCondition) {
        this.tdrCondition = tdrCondition;
    }

    public String getNcbCusMarriageStatus() {
        return ncbCusMarriageStatus;
    }

    public void setNcbCusMarriageStatus(String ncbCusMarriageStatus) {
        this.ncbCusMarriageStatus = ncbCusMarriageStatus;
    }

    public Date getNcbLastInfoAsOfDate() {
        return ncbLastInfoAsOfDate;
    }

    public void setNcbLastInfoAsOfDate(Date ncbLastInfoAsOfDate) {
        this.ncbLastInfoAsOfDate = ncbLastInfoAsOfDate;
    }

    public String getEnquiry() {
        return enquiry;
    }

    public void setEnquiry(String enquiry) {
        this.enquiry = enquiry;
    }

    public String getNcbCusName() {
        return ncbCusName;
    }

    public void setNcbCusName(String ncbCusName) {
        this.ncbCusName = ncbCusName;
    }

    public AddressView getNcbCusAddress() {
        return ncbCusAddress;
    }

    public void setNcbCusAddress(AddressView ncbCusAddress) {
        this.ncbCusAddress = ncbCusAddress;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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

    public int getNplFlag() {
        return nplFlag;
    }

    public void setNplFlag(int nplFlag) {
        this.nplFlag = nplFlag;
    }

    public int getTdrFlag() {
        return tdrFlag;
    }

    public void setTdrFlag(int tdrFlag) {
        this.tdrFlag = tdrFlag;
    }

    public boolean isNplTMBFlag() {
        return nplTMBFlag;
    }

    public void setNplTMBFlag(boolean nplTMBFlag) {
        this.nplTMBFlag = nplTMBFlag;
    }

    public boolean isNplOtherFlag() {
        return nplOtherFlag;
    }

    public void setNplOtherFlag(boolean nplOtherFlag) {
        this.nplOtherFlag = nplOtherFlag;
    }

    public boolean isTdrTMBFlag() {
        return tdrTMBFlag;
    }

    public void setTdrTMBFlag(boolean tdrTMBFlag) {
        this.tdrTMBFlag = tdrTMBFlag;
    }

    public boolean isTdrOtherFlag() {
        return tdrOtherFlag;
    }

    public void setTdrOtherFlag(boolean tdrOtherFlag) {
        this.tdrOtherFlag = tdrOtherFlag;
    }

    public String getNplFlagText() {
        return (this.nplFlag == 2) ? "Y" : "N";
    }

    public void setNplFlagText(String nplFlagText) {
        this.nplFlagText = nplFlagText;
    }

    public String getTdrFlagText() {
        return (this.tdrFlag == 2) ? "Y" : "N";
    }

    public void setTdrFlagText(String tdrFlagText) {
        this.tdrFlagText = tdrFlagText;
    }

    public NCBDetailView getNcbDetailView() {
        return ncbDetailView;
    }

    public void setNcbDetailView(NCBDetailView ncbDetailView) {
        this.ncbDetailView = ncbDetailView;
    }

    public CustomerInfoView getCustomerInfoView() {
        return customerInfoView;
    }

    public void setCustomerInfoView(CustomerInfoView customerInfoView) {
        this.customerInfoView = customerInfoView;
    }

    public LoanAccountTypeView getLoanAccountTypeView() {
        return loanAccountTypeView;
    }

    public void setLoanAccountTypeView(LoanAccountTypeView loanAccountTypeView) {
        this.loanAccountTypeView = loanAccountTypeView;
    }

    public String getNcbFlag() {
        return ncbFlag;
    }

    public void setNcbFlag(String ncbFlag) {
        this.ncbFlag = ncbFlag;
    }

    public Date getEnquiryDate() {
        return enquiryDate;
    }

    public void setEnquiryDate(Date enquiryDate) {
        this.enquiryDate = enquiryDate;
    }

    public String getNplTMBMonthStr() {
        return nplTMBMonthStr;
    }

    public void setNplTMBMonthStr(String nplTMBMonthStr) {
        this.nplTMBMonthStr = nplTMBMonthStr;
    }

    public String getNplOtherMonthStr() {
        return nplOtherMonthStr;
    }

    public void setNplOtherMonthStr(String nplOtherMonthStr) {
        this.nplOtherMonthStr = nplOtherMonthStr;
    }

    public String getTdrTMBMonthStr() {
        return tdrTMBMonthStr;
    }

    public void setTdrTMBMonthStr(String tdrTMBMonthStr) {
        this.tdrTMBMonthStr = tdrTMBMonthStr;
    }

    public String getTdrOtherMonthStr() {
        return tdrOtherMonthStr;
    }

    public void setTdrOtherMonthStr(String tdrOtherMonthStr) {
        this.tdrOtherMonthStr = tdrOtherMonthStr;
    }

    public boolean isNplTMBFlagNCB() {
        return nplTMBFlagNCB;
    }

    public void setNplTMBFlagNCB(boolean nplTMBFlagNCB) {
        this.nplTMBFlagNCB = nplTMBFlagNCB;
    }

    public boolean isNplOtherFlagNCB() {
        return nplOtherFlagNCB;
    }

    public void setNplOtherFlagNCB(boolean nplOtherFlagNCB) {
        this.nplOtherFlagNCB = nplOtherFlagNCB;
    }

    public boolean isTdrTMBFlagNCB() {
        return tdrTMBFlagNCB;
    }

    public void setTdrTMBFlagNCB(boolean tdrTMBFlagNCB) {
        this.tdrTMBFlagNCB = tdrTMBFlagNCB;
    }

    public boolean isTdrOtherFlagNCB() {
        return tdrOtherFlagNCB;
    }

    public void setTdrOtherFlagNCB(boolean tdrOtherFlagNCB) {
        this.tdrOtherFlagNCB = tdrOtherFlagNCB;
    }

    public Date getNplTMBDateNCB() {
        return nplTMBDateNCB;
    }

    public void setNplTMBDateNCB(Date nplTMBDateNCB) {
        this.nplTMBDateNCB = nplTMBDateNCB;
    }

    public Date getNplOtherDateNCB() {
        return nplOtherDateNCB;
    }

    public void setNplOtherDateNCB(Date nplOtherDateNCB) {
        this.nplOtherDateNCB = nplOtherDateNCB;
    }

    public Date getTdrTMBDateNCB() {
        return tdrTMBDateNCB;
    }

    public void setTdrTMBDateNCB(Date tdrTMBDateNCB) {
        this.tdrTMBDateNCB = tdrTMBDateNCB;
    }

    public Date getTdrOtherDateNCB() {
        return tdrOtherDateNCB;
    }

    public void setTdrOtherDateNCB(Date tdrOtherDateNCB) {
        this.tdrOtherDateNCB = tdrOtherDateNCB;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("customerId", customerId)
                .append("ncbCusMarriageStatus", ncbCusMarriageStatus)
                .append("checkingDate", checkingDate)
                .append("enquiryDate", enquiryDate)
                .append("ncbLastInfoAsOfDate", ncbLastInfoAsOfDate)
                .append("enquiry", enquiry)
                .append("checkIn6Month", checkIn6Month)
                .append("paymentClass", paymentClass)
                .append("personalId", personalId)
                .append("currentPaymentType", currentPaymentType)
                .append("historyPaymentType", historyPaymentType)
                .append("nplFlag", nplFlag)
                .append("nplFlagText", nplFlagText)
                .append("nplTMBFlag", nplTMBFlag)
                .append("nplTMBMonth", nplTMBMonth)
                .append("nplTMBMonthStr", nplTMBMonthStr)
                .append("nplTMBYear", nplTMBYear)
                .append("nplOtherFlag", nplOtherFlag)
                .append("nplOtherMonth", nplOtherMonth)
                .append("nplOtherMonthStr", nplOtherMonthStr)
                .append("nplOtherYear", nplOtherYear)
                .append("tdrFlag", tdrFlag)
                .append("tdrFlagText", tdrFlagText)
                .append("tdrTMBFlag", tdrTMBFlag)
                .append("tdrTMBMonth", tdrTMBMonth)
                .append("tdrTMBMonthStr", tdrTMBMonthStr)
                .append("tdrTMBYear", tdrTMBYear)
                .append("tdrOtherFlag", tdrOtherFlag)
                .append("tdrOtherMonth", tdrOtherMonth)
                .append("tdrOtherMonthStr", tdrOtherMonthStr)
                .append("tdrOtherYear", tdrOtherYear)
                .append("remark", remark)
                .append("tdrCondition", tdrCondition)
                .append("ncbCusName", ncbCusName)
                .append("ncbCusAddress", ncbCusAddress)
                .append("ncbFlag", ncbFlag)
                .append("active", active)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("ncbDetailView", ncbDetailView)
                .append("customerInfoView", customerInfoView)
                .append("loanAccountTypeView", loanAccountTypeView)
                .toString();
    }
}
