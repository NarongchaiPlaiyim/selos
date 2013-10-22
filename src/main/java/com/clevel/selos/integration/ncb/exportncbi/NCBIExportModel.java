package com.clevel.selos.integration.ncb.exportncbi;


import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class NCBIExportModel implements Serializable {
    private String staffId;
    private String requestNo;
    private String inquiryType;
    private String customerType;
    private String customerDocumentType;
    private String juristicType;
    private String customerId;
    private String countryCode;
    private String titleCode;
    private String firstName;
    private String lastName;
    private String juristicName;
    private String caNumber;
    private String caution;
    private String referenceTel;
    private String inquiryStatus;
    private String inquiryDate;
    private String inquiryTime;
    private String officeCode;

    public NCBIExportModel() {
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public void setRequestNo(String requestNo) {
        this.requestNo = requestNo;
    }

    public String getInquiryType() {
        return inquiryType;
    }

    public void setInquiryType(String inquiryType) {
        this.inquiryType = inquiryType;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerDocumentType() {
        return customerDocumentType;
    }

    public void setCustomerDocumentType(String customerDocumentType) {
        this.customerDocumentType = customerDocumentType;
    }

    public String getJuristicType() {
        return juristicType;
    }

    public void setJuristicType(String juristicType) {
        this.juristicType = juristicType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getTitleCode() {
        return titleCode;
    }

    public void setTitleCode(String titleCode) {
        this.titleCode = titleCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJuristicName() {
        return juristicName;
    }

    public void setJuristicName(String juristicName) {
        this.juristicName = juristicName;
    }

    public String getCaNumber() {
        return caNumber;
    }

    public void setCaNumber(String caNumber) {
        this.caNumber = caNumber;
    }

    public String getCaution() {
        return caution;
    }

    public void setCaution(String caution) {
        this.caution = caution;
    }

    public String getReferenceTel() {
        return referenceTel;
    }

    public void setReferenceTel(String referenceTel) {
        this.referenceTel = referenceTel;
    }

    public String getInquiryStatus() {
        return inquiryStatus;
    }

    public void setInquiryStatus(String inquiryStatus) {
        this.inquiryStatus = inquiryStatus;
    }

    public String getInquiryDate() {
        return inquiryDate;
    }

    public void setInquiryDate(String inquiryDate) {
        this.inquiryDate = inquiryDate;
    }

    public String getInquiryTime() {
        return inquiryTime;
    }

    public void setInquiryTime(String inquiryTime) {
        this.inquiryTime = inquiryTime;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("staffId", staffId)
                .append("requestNo", requestNo)
                .append("inquiryType", inquiryType)
                .append("customerType", customerType)
                .append("customerDocumentType", customerDocumentType)
                .append("juristicType", juristicType)
                .append("customerId", customerId)
                .append("countryCode", countryCode)
                .append("titleCode", titleCode)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("juristicName", juristicName)
                .append("caNumber", caNumber)
                .append("caution", caution)
                .append("referenceTel", referenceTel)
                .append("inquiryStatus", inquiryStatus)
                .append("inquiryDate", inquiryDate)
                .append("inquiryTime", inquiryTime)
                .append("officeCode", officeCode)
                .toString();
    }
}
