package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.TDRCondition;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "wrk_ncb")
public class NCB implements Serializable{
    @Id
    @SequenceGenerator(name="SEQ_WRK_NCB_ID", sequenceName="SEQ_WRK_NCB_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_NCB_ID")
    private long id;

    @OneToOne
    @JoinColumn(name="customer_id")
    private Customer customer;

    @Column(name="checking_date")
    private Date checkingDate;

    @Column(name="check_in_6_month")
    private int checkIn6Month;

    @Column(name="payment_class")
    private String paymentClass;

    @Column(name="personal_id")
    private String personalId;

    @Column(name="current_payment_type")
    private String currentPaymentType;

    @Column(name="history_payment_type")
    private String historyPaymentType;

    @Column(name="npl_flag")
    private boolean nplFlag;

    @Column(name="npl_tmb_flag")
    private boolean nplTMBFlag;

    @Column(name="npl_tmb_month")
    private int nplTMBMonth;

    @Column(name="npl_tmb_year")
    private int nplTMBYear;

    @Column(name="npl_other_flag")
    private boolean nplOtherFlag;

    @Column(name="npl_other_month")
    private int nplOtherMonth;

    @Column(name="npl_other_year")
    private int nplOtherYear;

    @Column(name="tdr_flag")
    private boolean tdrFlag;

    @Column(name="tdr_tmb_flag")
    private boolean tdrTMBFlag;

    @Column(name="tdr_tmb_month")
    private int tdrTMBMonth;

    @Column(name="tdr_tmb_year")
    private int tdrTMBYear;

    @Column(name="tdr_other_flag")
    private boolean tdrOhterFlag;

    @Column(name="tdr_other_month")
    private int tdrOtherMonth;

    @Column(name="tdr_other_year")
    private int tdrOtherYear;

    @OneToOne
    @JoinColumn(name="tdr_condition_id")
    private TDRCondition tdrCondition;

    @Column(name="remark")
    private String remark;

    @Column (name="cus_marriage_status")
    private String ncbCusMarriageStatus;


    @Column (name="last_info_as_of_date")
    private String ncbLastInfoAsOfDate;


    @Column (name="enquiry")
    private String enquiry;

    @Column (name="ncb_cus_name")
    private String ncbCusName;

    @Column (name="ncb_cus_address")
    private String ncbCusAddress;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    public boolean isNplFlag() {
        return nplFlag;
    }

    public void setNplFlag(boolean nplFlag) {
        this.nplFlag = nplFlag;
    }

    public boolean isNplTMBFlag() {
        return nplTMBFlag;
    }

    public void setNplTMBFlag(boolean nplTMBFlag) {
        this.nplTMBFlag = nplTMBFlag;
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

    public boolean isNplOtherFlag() {
        return nplOtherFlag;
    }

    public void setNplOtherFlag(boolean nplOtherFlag) {
        this.nplOtherFlag = nplOtherFlag;
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

    public boolean isTdrFlag() {
        return tdrFlag;
    }

    public void setTdrFlag(boolean tdrFlag) {
        this.tdrFlag = tdrFlag;
    }

    public boolean isTdrTMBFlag() {
        return tdrTMBFlag;
    }

    public void setTdrTMBFlag(boolean tdrTMBFlag) {
        this.tdrTMBFlag = tdrTMBFlag;
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

    public boolean isTdrOhterFlag() {
        return tdrOhterFlag;
    }

    public void setTdrOhterFlag(boolean tdrOhterFlag) {
        this.tdrOhterFlag = tdrOhterFlag;
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

    public TDRCondition getTdrCondition() {
        return tdrCondition;
    }

    public void setTdrCondition(TDRCondition tdrCondition) {
        this.tdrCondition = tdrCondition;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getNcbCusMarriageStatus() {
        return ncbCusMarriageStatus;
    }

    public void setNcbCusMarriageStatus(String ncbCusMarriageStatus) {
        this.ncbCusMarriageStatus = ncbCusMarriageStatus;
    }

    public String getNcbLastInfoAsOfDate() {
        return ncbLastInfoAsOfDate;
    }

    public void setNcbLastInfoAsOfDate(String ncbLastInfoAsOfDate) {
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
}