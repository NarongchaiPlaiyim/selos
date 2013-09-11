package com.clevel.selos.model.db.export;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "exp_ncbi")
public class NCBIExport implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_EXP_NCBI_ID", sequenceName="SEQ_EXP_NCBI_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_EXP_NCBI_ID")
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
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="inquiry_date", nullable=false)
    private Date inquiryDate;
    @Column(name="office_code", length = 3)
    private String officeCode;

    public NCBIExport() {
    }

    public NCBIExport(String staffId, String requestNo, String inquiryType, String customerType, String customerDocumentType, String juristicType, String customerId, String countryCode, String titleCode, String firstName, String lastName, String juristicName, String caNumber, String caution, String referenceTel, String inquiryStatus, Date inquiryDate, String officeCode) {
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
        this.officeCode = officeCode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Date getInquiryDate() {
        return inquiryDate;
    }

    public void setInquiryDate(Date inquiryDate) {
        this.inquiryDate = inquiryDate;
    }

    public String getOfficeCode() {
        return officeCode;
    }

    public void setOfficeCode(String officeCode) {
        this.officeCode = officeCode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("staffId", staffId).
                append("requestNo", requestNo).
                append("inquiryType", inquiryType).
                append("customerType", customerType).
                append("customerDocumentType", customerDocumentType).
                append("juristicType", juristicType).
                append("customerId", customerId).
                append("countryCode", countryCode).
                append("titleCode", titleCode).
                append("firstName", firstName).
                append("lastName", lastName).
                append("juristicName", juristicName).
                append("caNumber", caNumber).
                append("caution", caution).
                append("referenceTel", referenceTel).
                append("inquiryStatus", inquiryStatus).
                append("inquiryDate", inquiryDate).
                append("officeCode", officeCode).
                toString();
    }
}
