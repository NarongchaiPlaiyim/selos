package com.clevel.selos.integration.ncbi.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class RequestModel {

    @XStreamAlias("STAFF_ID")
    private String STAFF_ID;

    @XStreamAlias("REQUEST_NO")
    private String requestNo;

    @XStreamAlias("INQUIRY_TYPE")
    private String inquiryType;

    @XStreamAlias("CUSTOMER_TYPE")
    private String customerType;

    @XStreamAlias("CUSTOMER_DOCUMENT_TYPE")
    private String customerDocumentType;

    @XStreamAlias("JURISTIC_TYPE")
    private String juristicType;

    @XStreamAlias("CUSTOMER_ID")
    private String customerId;

    @XStreamAlias("COUNTRY_CODE")
    private String countryCode;

    @XStreamAlias("TITLE_CODE")
    private String titleCode;

    @XStreamAlias("FIRST_NAME")
    private String firstName;

    @XStreamAlias("LAST_NAME")
    private String lastName;

    @XStreamAlias("JURISTIC_NAME")
    private String juristicName;

    @XStreamAlias("CA_NUMBER")
    private String caNumber;

    @XStreamAlias("CAUTION")
    private String caution;

    @XStreamAlias("REFERENCE_TEL")
    private String referenceTel;

    @XStreamAlias("INQUIRY_STATUS")
    private String inquiryStatus;

    @XStreamAlias("INQUIRY_DATE")
    private String inquiryDate;

    @XStreamAlias("INQUIRY_TIME")
    private String inquiryTime;

    @XStreamAlias("OFFICE_CODE")
    private String officeCode;

    public RequestModel(String requestNo) {
        this.requestNo = requestNo;
    }

    public RequestModel(String staffId, String requestNo) {
        this.STAFF_ID = staffId;
        this.requestNo = requestNo;
    }

    public RequestModel(String staffId, String requestNo, String inquiryType, String customerType, String customerDocumentType, String customerId, String countryCode, String titleCode, String firstName, String lastName, String caNumber, String caution, String referenceTel, String inquiryStatus, String inquiryDate, String inquiryTime, String officeCode) {
        this.STAFF_ID = staffId;
        this.requestNo = requestNo;
        this.inquiryType = inquiryType;
        this.customerType = customerType;
        this.customerDocumentType = customerDocumentType;
        this.juristicType = "";
        this.customerId = customerId;
        this.countryCode = countryCode;
        this.titleCode = titleCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.juristicName = "";
        this.caNumber = caNumber;
        this.caution = caution;
        this.referenceTel = referenceTel;
        this.inquiryStatus = inquiryStatus;
        this.inquiryDate = inquiryDate;
        this.inquiryTime = inquiryTime;
        this.officeCode = officeCode;
    }

    public RequestModel(String staffId, String requestNo, String inquiryType, String customerType, String customerDocumentType, String juristicType, String customerId, String countryCode, String juristicName, String caNumber, String caution, String referenceTel, String inquiryStatus, String inquiryDate, String inquiryTime, String officeCode) {
        this.STAFF_ID = staffId;
        this.requestNo = requestNo;
        this.inquiryType = inquiryType;
        this.customerType = customerType;
        this.customerDocumentType = customerDocumentType;
        this.juristicType = juristicType;
        this.customerId = customerId;
        this.countryCode = countryCode;
        this.titleCode = "";
        this.firstName = "";
        this.lastName = "";
        this.juristicName = juristicName;
        this.caNumber = caNumber;
        this.caution = caution;
        this.referenceTel = referenceTel;
        this.inquiryStatus = inquiryStatus;
        this.inquiryDate = inquiryDate;
        this.inquiryTime = inquiryTime;
        this.officeCode = officeCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("staff_Id", STAFF_ID)
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
