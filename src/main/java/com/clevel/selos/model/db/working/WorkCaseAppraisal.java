package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "wrk_case_appraisal")
public class WorkCaseAppraisal {
    @Id
    @SequenceGenerator(name = "SEQ_WRK_CASE_APPRAISAL_ID", sequenceName = "SEQ_WRK_CASE_APPRAISAL_ID", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WRK_CASE_APPRAISAL_ID")
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "app_number", nullable = false)
    private String appNumber;

    @Column(name = "wob_number")
    private String wobNumber;

    @Column(name = "case_lock")
    private int lock;

    @Column(name = "lock_user")
    private String lockUser;

    @OneToOne
    @JoinColumn(name = "step_id")
    private Step step;

    @OneToOne
    @JoinColumn(name = "status_id")
    private Status status;

    @OneToOne
    @JoinColumn(name = "request_by")
    private User requestBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "request_date")
    private Date requestDate;

    @OneToOne
    @JoinColumn(name = "review_by")
    private User reviewBy;

    @OneToOne
    @JoinColumn(name = "approve_by")
    private User approveBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "approve_date")
    private Date approveDate;

    @OneToOne
    @JoinColumn(name = "modify_by")
    private User modifyBy;

    @OneToOne
    @JoinColumn(name = "step_owner")
    private User stepOwner;

    @Column(name = "appraisal_result", length = 2)
    private int appraisalResult;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAppNumber() {
        return appNumber;
    }

    public void setAppNumber(String appNumber) {
        this.appNumber = appNumber;
    }

    public String getWobNumber() {
        return wobNumber;
    }

    public void setWobNumber(String wobNumber) {
        this.wobNumber = wobNumber;
    }

    public int getLock() {
        return lock;
    }

    public void setLock(int lock) {
        this.lock = lock;
    }

    public String getLockUser() {
        return lockUser;
    }

    public void setLockUser(String lockUser) {
        this.lockUser = lockUser;
    }

    public Step getStep() {
        return step;
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public User getRequestBy() {
        return requestBy;
    }

    public void setRequestBy(User requestBy) {
        this.requestBy = requestBy;
    }

    public Date getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public User getReviewBy() {
        return reviewBy;
    }

    public void setReviewBy(User reviewBy) {
        this.reviewBy = reviewBy;
    }

    public User getApproveBy() {
        return approveBy;
    }

    public void setApproveBy(User approveBy) {
        this.approveBy = approveBy;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public User getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(User modifyBy) {
        this.modifyBy = modifyBy;
    }

    public User getStepOwner() {
        return stepOwner;
    }

    public void setStepOwner(User stepOwner) {
        this.stepOwner = stepOwner;
    }

    public int getAppraisalResult() {
        return appraisalResult;
    }

    public void setAppraisalResult(int appraisalResult) {
        this.appraisalResult = appraisalResult;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("appNumber", appNumber)
                .append("wobNumber", wobNumber)
                .append("lock", lock)
                .append("lockUser", lockUser)
                .append("step", step)
                .append("status", status)
                .append("requestBy", requestBy)
                .append("requestDate", requestDate)
                .append("reviewBy", reviewBy)
                .append("approveBy", approveBy)
                .append("approveDate", approveDate)
                .append("modifyBy", modifyBy)
                .append("stepOwner", stepOwner)
                .append("appraisalResult", appraisalResult)
                .toString();
    }
}
