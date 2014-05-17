package com.clevel.selos.model.report;


import com.clevel.selos.model.view.AppBorrowerHeaderView;
import com.clevel.selos.report.ReportModel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.inject.Inject;
import java.util.Date;

public class HeaderAndFooterReport extends ReportModel{

    private String caseStatus;
    private String submitDate;
    private String requestType;
    private String refinance;
    private String bdmName;
    private String bdmPhoneNumber;
    private String bdmPhoneExtNumber;
    private String bdmZoneName;
    private String bdmRegionName;
    private String uwName;
    private String uwPhoneNumber;
    private String uwPhoneExtNumber;
    private String uwTeamName;
    private String appNo;
    private String caNo;
    private String appRefNo;
    private String appRefDate;
    private String preScreenResult;
    private String productGroup;

    private String borrowerName;
    private String borrowerName2;
    private String borrowerName3;
    private String borrowerName4;
    private String borrowerName5;

    private String personalId;
    private String personalId2;
    private String personalId3;
    private String personalId4;
    private String personalId5;

    private String creditDecision;
    private Date approvedDate;

    //Footer
    private String genFooter;

    @Inject
    public HeaderAndFooterReport() {
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getSubmitDate() {
        return submitDate;
    }

    public void setSubmitDate(String submitDate) {
        this.submitDate = submitDate;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getRefinance() {
        return refinance;
    }

    public void setRefinance(String refinance) {
        this.refinance = refinance;
    }

    public String getBdmName() {
        return bdmName;
    }

    public void setBdmName(String bdmName) {
        this.bdmName = bdmName;
    }

    public String getBdmPhoneNumber() {
        return bdmPhoneNumber;
    }

    public void setBdmPhoneNumber(String bdmPhoneNumber) {
        this.bdmPhoneNumber = bdmPhoneNumber;
    }

    public String getBdmPhoneExtNumber() {
        return bdmPhoneExtNumber;
    }

    public void setBdmPhoneExtNumber(String bdmPhoneExtNumber) {
        this.bdmPhoneExtNumber = bdmPhoneExtNumber;
    }

    public String getBdmZoneName() {
        return bdmZoneName;
    }

    public void setBdmZoneName(String bdmZoneName) {
        this.bdmZoneName = bdmZoneName;
    }

    public String getBdmRegionName() {
        return bdmRegionName;
    }

    public void setBdmRegionName(String bdmRegionName) {
        this.bdmRegionName = bdmRegionName;
    }

    public String getUwName() {
        return uwName;
    }

    public void setUwName(String uwName) {
        this.uwName = uwName;
    }

    public String getUwPhoneNumber() {
        return uwPhoneNumber;
    }

    public void setUwPhoneNumber(String uwPhoneNumber) {
        this.uwPhoneNumber = uwPhoneNumber;
    }

    public String getUwPhoneExtNumber() {
        return uwPhoneExtNumber;
    }

    public void setUwPhoneExtNumber(String uwPhoneExtNumber) {
        this.uwPhoneExtNumber = uwPhoneExtNumber;
    }

    public String getUwTeamName() {
        return uwTeamName;
    }

    public void setUwTeamName(String uwTeamName) {
        this.uwTeamName = uwTeamName;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo;
    }

    public String getCaNo() {
        return caNo;
    }

    public void setCaNo(String caNo) {
        this.caNo = caNo;
    }

    public String getAppRefNo() {
        return appRefNo;
    }

    public void setAppRefNo(String appRefNo) {
        this.appRefNo = appRefNo;
    }

    public String getAppRefDate() {
        return appRefDate;
    }

    public void setAppRefDate(String appRefDate) {
        this.appRefDate = appRefDate;
    }

    public String getPreScreenResult() {
        return preScreenResult;
    }

    public void setPreScreenResult(String preScreenResult) {
        this.preScreenResult = preScreenResult;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getBorrowerName2() {
        return borrowerName2;
    }

    public void setBorrowerName2(String borrowerName2) {
        this.borrowerName2 = borrowerName2;
    }

    public String getBorrowerName3() {
        return borrowerName3;
    }

    public void setBorrowerName3(String borrowerName3) {
        this.borrowerName3 = borrowerName3;
    }

    public String getBorrowerName4() {
        return borrowerName4;
    }

    public void setBorrowerName4(String borrowerName4) {
        this.borrowerName4 = borrowerName4;
    }

    public String getBorrowerName5() {
        return borrowerName5;
    }

    public void setBorrowerName5(String borrowerName5) {
        this.borrowerName5 = borrowerName5;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public String getPersonalId2() {
        return personalId2;
    }

    public void setPersonalId2(String personalId2) {
        this.personalId2 = personalId2;
    }

    public String getPersonalId3() {
        return personalId3;
    }

    public void setPersonalId3(String personalId3) {
        this.personalId3 = personalId3;
    }

    public String getPersonalId4() {
        return personalId4;
    }

    public void setPersonalId4(String personalId4) {
        this.personalId4 = personalId4;
    }

    public String getPersonalId5() {
        return personalId5;
    }

    public void setPersonalId5(String personalId5) {
        this.personalId5 = personalId5;
    }

    public String getCreditDecision() {
        return creditDecision;
    }

    public void setCreditDecision(String creditDecision) {
        this.creditDecision = creditDecision;
    }

    public Date getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(Date approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getGenFooter() {
        return genFooter;
    }

    public void setGenFooter(String genFooter) {
        this.genFooter = genFooter;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("caseStatus", caseStatus)
                .append("submitDate", submitDate)
                .append("requestType", requestType)
                .append("refinance", refinance)
                .append("bdmName", bdmName)
                .append("bdmPhoneNumber", bdmPhoneNumber)
                .append("bdmPhoneExtNumber", bdmPhoneExtNumber)
                .append("bdmZoneName", bdmZoneName)
                .append("bdmRegionName", bdmRegionName)
                .append("uwName", uwName)
                .append("uwPhoneNumber", uwPhoneNumber)
                .append("uwPhoneExtNumber", uwPhoneExtNumber)
                .append("uwTeamName", uwTeamName)
                .append("appNo", appNo)
                .append("caNo", caNo)
                .append("appRefNo", appRefNo)
                .append("appRefDate", appRefDate)
                .append("preScreenResult", preScreenResult)
                .append("productGroup", productGroup)
                .append("borrowerName", borrowerName)
                .append("borrowerName2", borrowerName2)
                .append("borrowerName3", borrowerName3)
                .append("borrowerName4", borrowerName4)
                .append("borrowerName5", borrowerName5)
                .append("personalId", personalId)
                .append("personalId2", personalId2)
                .append("personalId3", personalId3)
                .append("personalId4", personalId4)
                .append("personalId5", personalId5)
                .append("creditDecision", creditDecision)
                .append("approvedDate", approvedDate)
                .append("genFooter", genFooter)
                .toString();
    }
}
