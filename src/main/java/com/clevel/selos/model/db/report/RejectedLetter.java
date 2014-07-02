package com.clevel.selos.model.db.report;

import com.clevel.selos.model.db.master.User;
import com.clevel.selos.model.db.working.WorkCase;
import com.clevel.selos.model.db.working.WorkCasePrescreen;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "rpt_reject_letter")
public class RejectedLetter implements Serializable{
    @Id
    @OneToOne
    @JoinColumn(name = "workcase_prescreen_id")
    private WorkCasePrescreen workCasePrescreen;

    @Column(name = "app_number", length = 30,nullable = false)
    protected String appNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "rejected_date")
    private Date rejectedDate;

    @Column(name = "hub_code", length = 4)
    protected String hubCode;

    @Column(name = "borrower1", length = 100)
    protected String borrower1;

    @Column(name = "citizen_id1", length = 13)
    protected String citizenId1;

    @Column(name = "borrower2", length = 100)
    protected String borrower2;

    @Column(name = "citizen_id2", length = 13)
    protected String citizenId2;

    @Column(name = "borrower3", length = 100)
    protected String borrower3;

    @Column(name = "citizen_id3", length = 13)
    protected String citizenId3;

    @Column(name = "borrower4", length = 100)
    protected String borrower4;

    @Column(name = "citizen_id4", length = 13)
    protected String citizenId4;

    @Column(name = "borrower5", length = 100)
    protected String borrower5;

    @Column(name = "citizen_id5", length = 13)
    protected String citizenId5;

    @Column(name = "addr_line1", length = 100)
    protected String addrLine1;

    @Column(name = "addr_line2", length = 100)
    protected String addrLine2;

    @Column(name = "addr_line3", length = 100)
    protected String addrLine3;

    @Column(name = "addr_line4", length = 100)
    protected String addrLine4;

    @Column(name = "province", length = 50)
    protected String province;

    @Column(name = "zipcode", length = 10)
    protected String zipcode;

    @Column(name = "zone_office_phone", length = 50)
    protected String zoneOfficePhone;

    public WorkCasePrescreen getWorkCasePrescreen() {
        return workCasePrescreen;
    }

    public void setWorkCasePrescreen(WorkCasePrescreen workCasePrescreen) {
        this.workCasePrescreen = workCasePrescreen;
    }

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
    }

    public Date getRejectedDate() {
        return rejectedDate;
    }

    public void setRejectedDate(Date rejectedDate) {
        this.rejectedDate = rejectedDate;
    }

    public String getHubCode() {
        return hubCode;
    }

    public void setHubCode(String hubCode) {
        this.hubCode = hubCode;
    }

    public String getBorrower1() {
        return borrower1;
    }

    public void setBorrower1(String borrower1) {
        this.borrower1 = borrower1;
    }

    public String getCitizenId1() {
        return citizenId1;
    }

    public void setCitizenId1(String citizenId1) {
        this.citizenId1 = citizenId1;
    }

    public String getBorrower2() {
        return borrower2;
    }

    public void setBorrower2(String borrower2) {
        this.borrower2 = borrower2;
    }

    public String getCitizenId2() {
        return citizenId2;
    }

    public void setCitizenId2(String citizenId2) {
        this.citizenId2 = citizenId2;
    }

    public String getBorrower3() {
        return borrower3;
    }

    public void setBorrower3(String borrower3) {
        this.borrower3 = borrower3;
    }

    public String getCitizenId3() {
        return citizenId3;
    }

    public void setCitizenId3(String citizenId3) {
        this.citizenId3 = citizenId3;
    }

    public String getBorrower4() {
        return borrower4;
    }

    public void setBorrower4(String borrower4) {
        this.borrower4 = borrower4;
    }

    public String getCitizenId4() {
        return citizenId4;
    }

    public void setCitizenId4(String citizenId4) {
        this.citizenId4 = citizenId4;
    }

    public String getBorrower5() {
        return borrower5;
    }

    public void setBorrower5(String borrower5) {
        this.borrower5 = borrower5;
    }

    public String getCitizenId5() {
        return citizenId5;
    }

    public void setCitizenId5(String citizenId5) {
        this.citizenId5 = citizenId5;
    }

    public String getAddrLine1() {
        return addrLine1;
    }

    public void setAddrLine1(String addrLine1) {
        this.addrLine1 = addrLine1;
    }

    public String getAddrLine2() {
        return addrLine2;
    }

    public void setAddrLine2(String addrLine2) {
        this.addrLine2 = addrLine2;
    }

    public String getAddrLine3() {
        return addrLine3;
    }

    public void setAddrLine3(String addrLine3) {
        this.addrLine3 = addrLine3;
    }

    public String getAddrLine4() {
        return addrLine4;
    }

    public void setAddrLine4(String addrLine4) {
        this.addrLine4 = addrLine4;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getZoneOfficePhone() {
        return zoneOfficePhone;
    }

    public void setZoneOfficePhone(String zoneOfficePhone) {
        this.zoneOfficePhone = zoneOfficePhone;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("workCasePrescreen", workCasePrescreen)
                .append("appNumber", appNumber)
                .append("rejectedDate", rejectedDate)
                .append("hubCode", hubCode)
                .append("borrower1", borrower1)
                .append("citizenId1", citizenId1)
                .append("borrower2", borrower2)
                .append("citizenId2", citizenId2)
                .append("borrower3", borrower3)
                .append("citizenId3", citizenId3)
                .append("borrower4", borrower4)
                .append("citizenId4", citizenId4)
                .append("borrower5", borrower5)
                .append("citizenId5", citizenId5)
                .append("addrLine1", addrLine1)
                .append("addrLine2", addrLine2)
                .append("addrLine3", addrLine3)
                .append("addrLine4", addrLine4)
                .append("province", province)
                .append("zipcode", zipcode)
                .append("zoneOfficePhone", zoneOfficePhone)
                .toString();
    }
}
