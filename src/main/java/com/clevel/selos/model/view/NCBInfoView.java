package com.clevel.selos.model.view;

import com.clevel.selos.model.db.master.TDRCondition;
import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.Customer;

import java.io.Serializable;
import java.util.Date;

public class NCBInfoView implements Serializable {
    private long id;
    private String ncbCusMarriageStatus;
    private Date checkingDate;
    private Date ncbLastInfoAsOfDate;
    private String enquiry ;
    private int checkIn6Month;
    private String paymentClass;
    private String personalId;
    private String currentPaymentType;
    private String historyPaymentType;
    private int nplFlag;
    private int nplTMBFlag;
    private int nplTMBMonth;
    private int nplTMBYear;
    private int nplOtherFlag;
    private int nplOtherMonth;
    private int nplOtherYear;
    private int tdrFlag;
    private int tdrTMBFlag;
    private int tdrTMBMonth;
    private int tdrTMBYear;
    private int tdrOtherFlag;
    private int tdrOtherMonth;
    private int tdrOtherYear;
    private String remark;
    private TDRCondition tdrCondition;
    private Customer customer;
    private String ncbCusName;
    private String ncbCusAddress;

    private boolean active;
    private Date createDate;
    private Date modifyDate;
    private User createBy;
    private User modifyBy;


    public NCBInfoView(){
        reset();
    }

    public void reset(){
        this.checkingDate  = new Date();
        this.checkIn6Month = 0;
        this.paymentClass  = "";
        this.personalId    = "";
        this.currentPaymentType = "";
        this.historyPaymentType = "";
        this.nplFlag     = 0;
        this.nplTMBFlag  = 0;
        this.nplTMBMonth = 0;
        this.nplTMBYear  = 0;
        this.nplOtherFlag = 0;
        this.nplOtherMonth = 0;
        this.nplOtherYear  = 0;
        this.tdrFlag = 0;
        this.tdrTMBFlag = 0;
        this.tdrTMBMonth = 0;
        this.tdrTMBYear = 0;
        this.tdrOtherFlag = 0;
        this.tdrOtherMonth = 0;
        this.tdrOtherYear = 0;
        this.remark = "";
        this.tdrCondition = new TDRCondition();
        this.customer = new Customer();
        this.ncbCusName = "";
        this.ncbCusAddress = "";
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
        return nplTMBMonth;
    }

    public void setNplTMBMonth(int nplTMBMonth) {
        this.nplTMBMonth = nplTMBMonth;
    }

    public int getNplTMBYear() {
        return nplTMBYear;
    }

    public void setNplTMBYear(int nplTMBYear) {
        this.nplTMBYear = nplTMBYear;
    }


    public int getNplOtherMonth() {
        return nplOtherMonth;
    }

    public void setNplOtherMonth(int nplOtherMonth) {
        this.nplOtherMonth = nplOtherMonth;
    }

    public int getNplOtherYear() {
        return nplOtherYear;
    }

    public void setNplOtherYear(int nplOtherYear) {
        this.nplOtherYear = nplOtherYear;
    }

    public int getTdrTMBMonth() {
        return tdrTMBMonth;
    }

    public void setTdrTMBMonth(int tdrTMBMonth) {
        this.tdrTMBMonth = tdrTMBMonth;
    }

    public int getTdrTMBYear() {
        return tdrTMBYear;
    }

    public void setTdrTMBYear(int tdrTMBYear) {
        this.tdrTMBYear = tdrTMBYear;
    }

    public int getTdrOtherMonth() {
        return tdrOtherMonth;
    }

    public void setTdrOtherMonth(int tdrOtherMonth) {
        this.tdrOtherMonth = tdrOtherMonth;
    }

    public int getTdrOtherYear() {
        return tdrOtherYear;
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

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public String getNcbCusAddress() {
        return ncbCusAddress;
    }

    public void setNcbCusAddress(String ncbCusAddress) {
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

    public int getNplTMBFlag() {
        return nplTMBFlag;
    }

    public void setNplTMBFlag(int nplTMBFlag) {
        this.nplTMBFlag = nplTMBFlag;
    }

    public int getNplOtherFlag() {
        return nplOtherFlag;
    }

    public void setNplOtherFlag(int nplOtherFlag) {
        this.nplOtherFlag = nplOtherFlag;
    }

    public int getTdrTMBFlag() {
        return tdrTMBFlag;
    }

    public void setTdrTMBFlag(int tdrTMBFlag) {
        this.tdrTMBFlag = tdrTMBFlag;
    }

    public int getTdrOtherFlag() {
        return tdrOtherFlag;
    }

    public void setTdrOtherFlag(int tdrOtherFlag) {
        this.tdrOtherFlag = tdrOtherFlag;
    }
}
