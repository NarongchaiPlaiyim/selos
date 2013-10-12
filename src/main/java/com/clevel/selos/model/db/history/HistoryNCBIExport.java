package com.clevel.selos.model.db.history;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "his_ext_ncbi_export")
public class HistoryNCBIExport implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_HIS_EXT_NCBI_ID", sequenceName="SEQ_HIS_EXT_NCBI_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_HIS_EXT_NCBI_ID")
    private long id;
    @Column(name="staff_id", length = 5)
    private String staffId;
    @Column(name="request_no", length = 20)
    private String requestNo;
    @Column(name="inquiry_type", length = 2)
    private String inquiryType;
    @Column(name="customer_type", length = 2)
    private String customerType;
    @Column(name="customer_document_type", length = 2)
    private String customerDocumentType;
    @Column(name="juristic_type", length = 2)
    private String juristicType;
    @Column(name="customer_id", length = 13)
    private String customerId;
    @Column(name="country_code", length = 2)
    private String countryCode;
    @Column(name="title_code", length = 2)
    private String titleCode;
    @Column(name="first_name", length = 500)
    private String firstName;
    @Column(name="last_name", length = 100)
    private String lastName;
    @Column(name="juristic_name", length = 500)
    private String juristicName;
    @Column(name="ca_number", length = 20)
    private String caNumber;
    @Column(name="caution", length = 100)
    private String caution;
    @Column(name="reference_tel", length = 20)
    private String referenceTel;
    @Column(name="inquiry_status", length = 2)
    private String inquiryStatus;
    @Column(name="inquiry_date", length = 10)
    private String inquiryDate;
    @Column(name="inquiry_time", length = 8)
    private String inquiryTime;
    @Column(name="office_code", length = 3)
    private String officeCode;
    @Column(name="status", length = 10)
    private String status;

    public HistoryNCBIExport() {
    }

    public HistoryNCBIExport(String staffId, String requestNo, String inquiryType, String customerType, String customerDocumentType, String juristicType, String customerId, String countryCode, String titleCode, String firstName, String lastName, String juristicName, String caNumber, String caution, String referenceTel, String inquiryStatus, String inquiryDate, String inquiryTime, String officeCode, String status) {
        this.staffId = staffId;
        this.requestNo = requestNo;
        this.inquiryType = inquiryType;
        this.customerType = customerType;
        this.customerDocumentType = customerDocumentType;
        this.juristicType = juristicType;
        this.customerId = customerId;
        this.countryCode = countryCode;
        this.titleCode = titleCode;
        this.firstName = firstName;
        this.lastName = lastName;
        this.juristicName = juristicName;
        this.caNumber = caNumber;
        this.caution = caution;
        this.referenceTel = referenceTel;
        this.inquiryStatus = inquiryStatus;
        this.inquiryDate = inquiryDate;
        this.inquiryTime = inquiryTime;
        this.officeCode = officeCode;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public String getStaffId() {
        return staffId;
    }

    public String getRequestNo() {
        return requestNo;
    }

    public String getInquiryType() {
        return inquiryType;
    }

    public String getCustomerType() {
        return customerType;
    }

    public String getCustomerDocumentType() {
        return customerDocumentType;
    }

    public String getJuristicType() {
        return juristicType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getTitleCode() {
        return titleCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getJuristicName() {
        return juristicName;
    }

    public String getCaNumber() {
        return caNumber;
    }

    public String getCaution() {
        return caution;
    }

    public String getReferenceTel() {
        return referenceTel;
    }

    public String getInquiryStatus() {
        return inquiryStatus;
    }

    public String getInquiryDate() {
        return inquiryDate;
    }

    public String getInquiryTime() {
        return inquiryTime;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
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
                .append("status", status)
                .toString();
    }
}
