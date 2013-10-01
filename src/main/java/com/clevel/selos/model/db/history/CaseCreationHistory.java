package com.clevel.selos.model.db.history;

import com.clevel.selos.integration.IntegrationStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "his_case_creation")
public class CaseCreationHistory implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_HIS_CASE_CREATION_ID", sequenceName="SEQ_HIS_CASE_CREATION_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_HIS_CASE_CREATION_ID")
    private long id;
    @Column(name="job_name")
    private String jobName;
    @Column(name="ca_number", nullable = false)
    private String caNumber;
    @Column(name="old_ca_number")
    private String oldCaNumber;
    @Column(name="account_no1")
    private String accountNo1;
    @Column(name="customer_id")
    private String customerId;
    @Column(name="customer_name")
    private String customerName;
    @Column(name="citizen_id")
    private String citizenId;
    @Column(name="request_type")
    private int requestType;
    @Column(name="customer_type")
    private int customerType;
    @Column(name="bdm_id")
    private String bdmId;
    @Column(name="hub_code")
    private String hubCode;
    @Column(name="region_code")
    private String regionCode;
    @Column(name="uw_id")
    private String uwId;
    @Column(name="appindate_bdm")
    private String appInDateBDM;
    @Column(name="final_approved")
    private String finalApproved;
    @Column(name="parallel")
    private String parallel;
    @Column(name="pending")
    private String pending;
    @Column(name="ca_exist")
    private String caExist;
    @Column(name="ca_end")
    private String caEnd;
    @Column(name="account_no2")
    private String accountNo2;
    @Column(name="account_no3")
    private String accountNo3;
    @Column(name="account_no4")
    private String accountNo4;
    @Column(name="account_no5")
    private String accountNo5;
    @Column(name="account_no6")
    private String accountNo6;
    @Column(name="account_no7")
    private String accountNo7;
    @Column(name="account_no8")
    private String accountNo8;
    @Column(name="account_no9")
    private String accountNo9;
    @Column(name="account_no10")
    private String accountNo10;
    @Column(name="appindate_uw")
    private String appInDateUW;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_date")
    private Date createDate;
    @Column(name="create_status")
    @Enumerated(EnumType.STRING)
    private IntegrationStatus status;
    @Column(name="create_status_detail", length = 500)
    private String statusDetail;
    @Column(name="app_number")
    private String appNumber;

    public CaseCreationHistory() {
    }

    public CaseCreationHistory(String jobName, String caNumber, String oldCaNumber, String accountNo1, String customerId, String customerName, String citizenId, int requestType, int customerType, String bdmId, String hubCode, String regionCode, String uwId, String appInDateBDM, String finalApproved, String parallel, String pending, String caExist, String caEnd, String accountNo2, String accountNo3, String accountNo4, String accountNo5, String accountNo6, String accountNo7, String accountNo8, String accountNo9, String accountNo10, String appInDateUW, Date createDate, IntegrationStatus status, String statusDetail, String appNumber) {
        this.jobName = jobName;
        this.caNumber = caNumber;
        this.oldCaNumber = oldCaNumber;
        this.accountNo1 = accountNo1;
        this.customerId = customerId;
        this.customerName = customerName;
        this.citizenId = citizenId;
        this.requestType = requestType;
        this.customerType = customerType;
        this.bdmId = bdmId;
        this.hubCode = hubCode;
        this.regionCode = regionCode;
        this.uwId = uwId;
        this.appInDateBDM = appInDateBDM;
        this.finalApproved = finalApproved;
        this.parallel = parallel;
        this.pending = pending;
        this.caExist = caExist;
        this.caEnd = caEnd;
        this.accountNo2 = accountNo2;
        this.accountNo3 = accountNo3;
        this.accountNo4 = accountNo4;
        this.accountNo5 = accountNo5;
        this.accountNo6 = accountNo6;
        this.accountNo7 = accountNo7;
        this.accountNo8 = accountNo8;
        this.accountNo9 = accountNo9;
        this.accountNo10 = accountNo10;
        this.appInDateUW = appInDateUW;
        this.createDate = createDate;
        this.status = status;
        this.statusDetail = statusDetail;
        this.appNumber = appNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCaNumber() {
        return caNumber;
    }

    public void setCaNumber(String caNumber) {
        this.caNumber = caNumber;
    }

    public String getOldCaNumber() {
        return oldCaNumber;
    }

    public void setOldCaNumber(String oldCaNumber) {
        this.oldCaNumber = oldCaNumber;
    }

    public String getAccountNo1() {
        return accountNo1;
    }

    public void setAccountNo1(String accountNo1) {
        this.accountNo1 = accountNo1;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(String citizenId) {
        this.citizenId = citizenId;
    }

    public int getRequestType() {
        return requestType;
    }

    public void setRequestType(int requestType) {
        this.requestType = requestType;
    }

    public int getCustomerType() {
        return customerType;
    }

    public void setCustomerType(int customerType) {
        this.customerType = customerType;
    }

    public String getBdmId() {
        return bdmId;
    }

    public void setBdmId(String bdmId) {
        this.bdmId = bdmId;
    }

    public String getHubCode() {
        return hubCode;
    }

    public void setHubCode(String hubCode) {
        this.hubCode = hubCode;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getUwId() {
        return uwId;
    }

    public void setUwId(String uwId) {
        this.uwId = uwId;
    }

    public String getAppInDateBDM() {
        return appInDateBDM;
    }

    public void setAppInDateBDM(String appInDateBDM) {
        this.appInDateBDM = appInDateBDM;
    }

    public String getFinalApproved() {
        return finalApproved;
    }

    public void setFinalApproved(String finalApproved) {
        this.finalApproved = finalApproved;
    }

    public String getParallel() {
        return parallel;
    }

    public void setParallel(String parallel) {
        this.parallel = parallel;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getCaExist() {
        return caExist;
    }

    public void setCaExist(String caExist) {
        this.caExist = caExist;
    }

    public String getCaEnd() {
        return caEnd;
    }

    public void setCaEnd(String caEnd) {
        this.caEnd = caEnd;
    }

    public String getAccountNo2() {
        return accountNo2;
    }

    public void setAccountNo2(String accountNo2) {
        this.accountNo2 = accountNo2;
    }

    public String getAccountNo3() {
        return accountNo3;
    }

    public void setAccountNo3(String accountNo3) {
        this.accountNo3 = accountNo3;
    }

    public String getAccountNo4() {
        return accountNo4;
    }

    public void setAccountNo4(String accountNo4) {
        this.accountNo4 = accountNo4;
    }

    public String getAccountNo5() {
        return accountNo5;
    }

    public void setAccountNo5(String accountNo5) {
        this.accountNo5 = accountNo5;
    }

    public String getAccountNo6() {
        return accountNo6;
    }

    public void setAccountNo6(String accountNo6) {
        this.accountNo6 = accountNo6;
    }

    public String getAccountNo7() {
        return accountNo7;
    }

    public void setAccountNo7(String accountNo7) {
        this.accountNo7 = accountNo7;
    }

    public String getAccountNo8() {
        return accountNo8;
    }

    public void setAccountNo8(String accountNo8) {
        this.accountNo8 = accountNo8;
    }

    public String getAccountNo9() {
        return accountNo9;
    }

    public void setAccountNo9(String accountNo9) {
        this.accountNo9 = accountNo9;
    }

    public String getAccountNo10() {
        return accountNo10;
    }

    public void setAccountNo10(String accountNo10) {
        this.accountNo10 = accountNo10;
    }

    public String getAppInDateUW() {
        return appInDateUW;
    }

    public void setAppInDateUW(String appInDateUW) {
        this.appInDateUW = appInDateUW;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public IntegrationStatus getStatus() {
        return status;
    }

    public void setStatus(IntegrationStatus status) {
        this.status = status;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("jobName", jobName).
                append("caNumber", caNumber).
                append("oldCaNumber", oldCaNumber).
                append("accountNo1", accountNo1).
                append("customerId", customerId).
                append("customerName", customerName).
                append("citizenId", citizenId).
                append("requestType", requestType).
                append("customerType", customerType).
                append("bdmId", bdmId).
                append("hubCode", hubCode).
                append("regionCode", regionCode).
                append("uwId", uwId).
                append("appInDateBDM", appInDateBDM).
                append("finalApproved", finalApproved).
                append("parallel", parallel).
                append("pending", pending).
                append("caExist", caExist).
                append("caEnd", caEnd).
                append("accountNo2", accountNo2).
                append("accountNo3", accountNo3).
                append("accountNo4", accountNo4).
                append("accountNo5", accountNo5).
                append("accountNo6", accountNo6).
                append("accountNo7", accountNo7).
                append("accountNo8", accountNo8).
                append("accountNo9", accountNo9).
                append("accountNo10", accountNo10).
                append("appInDateUW", appInDateUW).
                append("createDate", createDate).
                append("status", status).
                append("statusDetail", statusDetail).
                append("appNumber", appNumber).
                toString();
    }
}
