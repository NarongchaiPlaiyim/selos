package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Reason;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name="wrk_contact_record")
public class ContactRecordDetail {
    @Id
    @SequenceGenerator(name="SEQ_WRK_CONTACT_REC_ID", sequenceName="SEQ_WRK_CONTACT_REC_ID", allocationSize=1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="SEQ_WRK_CONTACT_REC_ID")
    private long id;

    @Column(name="no")
    private int no;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="calling_date")
    private Date callingDate;

    @Column(name="calling_time")
    private String callingTime;

    @Column(name="calling_result")
    private int callingResult;

    @Column(name="accept_result")
    private int acceptResult;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="next_calling_date")
    private Date nextCallingDate ;

    @Column(name="next_calling_time")
    private String nextCallingTime;

    @OneToOne
    @JoinColumn(name="reason_id")
    private Reason reason;

    @Column(name="remark")
    private String remark;

    @Column(name="status")
    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    private Date createDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    private Date modifyDate;

    @OneToOne
    @JoinColumn(name="create_user_id")
    private User createBy;

    @OneToOne
    @JoinColumn(name="modify_user_id")
    private User modifyBy;

    @ManyToOne
    @JoinColumn(name = "customer_acceptance_id")
    private CustomerAcceptance customerAcceptance;

    @ManyToOne
    @JoinColumn(name = "appraisal_id")
    private Appraisal appraisal;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public Date getCallingDate() {
        return callingDate;
    }

    public void setCallingDate(Date callingDate) {
        this.callingDate = callingDate;
    }

    public String getCallingTime() {
        return callingTime;
    }

    public void setCallingTime(String callingTime) {
        this.callingTime = callingTime;
    }

    public int getCallingResult() {
        return callingResult;
    }

    public void setCallingResult(int callingResult) {
        this.callingResult = callingResult;
    }

    public int getAcceptResult() {
        return acceptResult;
    }

    public void setAcceptResult(int acceptResult) {
        this.acceptResult = acceptResult;
    }

    public Date getNextCallingDate() {
        return nextCallingDate;
    }

    public void setNextCallingDate(Date nextCallingDate) {
        this.nextCallingDate = nextCallingDate;
    }

    public String getNextCallingTime() {
        return nextCallingTime;
    }

    public void setNextCallingTime(String nextCallingTime) {
        this.nextCallingTime = nextCallingTime;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    public CustomerAcceptance getCustomerAcceptance() {
        return customerAcceptance;
    }

    public void setCustomerAcceptance(CustomerAcceptance customerAcceptance) {
        this.customerAcceptance = customerAcceptance;
    }

    public Appraisal getAppraisal() {
        return appraisal;
    }

    public void setAppraisal(Appraisal appraisal) {
        this.appraisal = appraisal;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("no", no)
                .append("callingDate", callingDate)
                .append("callingTime", callingTime)
                .append("callingResult", callingResult)
                .append("acceptResult", acceptResult)
                .append("nextCallingDate", nextCallingDate)
                .append("nextCallingTime", nextCallingTime)
                .append("reason", reason)
                .append("remark", remark)
                .append("remark", status)
                .append("createDate", createDate)
                .append("modifyDate", modifyDate)
                .append("createBy", createBy)
                .append("modifyBy", modifyBy)
                .append("customerAcceptance", customerAcceptance)
                .append("appraisal", appraisal)
                .toString();
    }
}
