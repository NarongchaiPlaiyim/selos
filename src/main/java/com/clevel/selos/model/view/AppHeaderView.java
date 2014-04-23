package com.clevel.selos.model.view;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.List;

public class AppHeaderView implements Serializable {
    private String caseStatus;
    private String submitDate;
    private String requestType;
    private String refinance;
    private String refinanceOut;
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
    private List<AppBorrowerHeaderView> borrowerHeaderViewList;
    private List<String> productProgramList;
    private String productGroup;

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

    public List<AppBorrowerHeaderView> getBorrowerHeaderViewList() {
        return borrowerHeaderViewList;
    }

    public void setBorrowerHeaderViewList(List<AppBorrowerHeaderView> borrowerHeaderViewList) {
        this.borrowerHeaderViewList = borrowerHeaderViewList;
    }

    public List<String> getProductProgramList() {
        return productProgramList;
    }

    public void setProductProgramList(List<String> productProgramList) {
        this.productProgramList = productProgramList;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    public String getRefinanceOut() {
        return refinanceOut;
    }

    public void setRefinanceOut(String refinanceOut) {
        this.refinanceOut = refinanceOut;
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
                .append("borrowerHeaderViewList", borrowerHeaderViewList)
                .append("productProgramList", productProgramList)
                .append("productGroup", productGroup)
                .toString();
    }
}
