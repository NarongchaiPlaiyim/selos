package com.clevel.selos.model.db.ext.ncb;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ext_ncb_result")
public class NCBResult implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_EXT_NCB_RESULT_ID", sequenceName="SEQ_EXT_NCB_RESULT_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_EXT_NCB_RESULT_ID")
    private long id;

    @Column(name="app_ref_number", length = 16)
    private String appRefNumber;

    @Column(name="customer_type", length = 2)
    private String customerType;

    @Column(name="customer_id", length = 13)
    private String customerId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="inquiry_date", nullable=false)
    private Date inquiryDate;

    @Column(name="result", length = 20)
    private String result;

    @Column(name="reason", length = 500)
    private String reason;

    public NCBResult() {
    }

    public NCBResult(String appRefNumber, String customerType, String customerId, Date inquiryDate, String result, String reason) {
        this.appRefNumber = appRefNumber;
        this.customerType = customerType;
        this.customerId = customerId;
        this.inquiryDate = inquiryDate;
        this.result = result;
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAppRefNumber() {
        return appRefNumber;
    }

    public void setAppRefNumber(String appRefNumber) {
        this.appRefNumber = appRefNumber;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Date getInquiryDate() {
        return inquiryDate;
    }

    public void setInquiryDate(Date inquiryDate) {
        this.inquiryDate = inquiryDate;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
