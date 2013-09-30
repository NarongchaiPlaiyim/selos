package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "wrk_case")
public class WorkCase implements Serializable {
    @Id
    @SequenceGenerator(name="SEQ_WRK_CASE_ID", sequenceName = "SEQ_WRK_CASE_ID", allocationSize=1)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_WRK_CASE_ID")
    @Column(name="id", nullable=false)
    private long id;
    @Column(name="ca_number", length = 30, nullable=false)
    private String caNumber;
    @Column(name="app_ref_number", nullable=false)
    private String appRefNumber;
    @Column(name="wob_number")
    private String wobNumber;
    @Column(name="case_lock")
    private int lock;
    @Column(name="lock_user")
    private String lockUser;
    @OneToOne
    @JoinColumn(name="step_id")
    private Step step;
    @OneToOne
    @JoinColumn(name="status_id")
    private Status status;

    @OneToMany(mappedBy="workCase")
    private List<Customer> customerList;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="create_date")
    private Date createDate;
    @OneToOne
    @JoinColumn(name="create_by")
    private User createBy;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modify_date")
    private Date modifyDate;
    @OneToOne
    @JoinColumn(name="modify_by")
    private User modifyBy;
    @OneToOne
    @JoinColumn(name="step_owner")
    private User stepOwner;

    @OneToOne
    @JoinColumn(name="workcaseprescreen_id")
    private WorkCasePrescreen workCasePrescreen;

    public WorkCase() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCaNumber() {
        return caNumber;
    }

    public void setCaNumber(String caNumber) {
        this.caNumber = caNumber;
    }

    public String getAppRefNumber() {
        return appRefNumber;
    }

    public void setAppRefNumber(String appRefNumber) {
        this.appRefNumber = appRefNumber;
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

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public User getCreateBy() {
        return createBy;
    }

    public void setCreateBy(User createBy) {
        this.createBy = createBy;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
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

    public WorkCasePrescreen getWorkCasePrescreen() {
        return workCasePrescreen;
    }

    public void setWorkCasePrescreen(WorkCasePrescreen workCasePrescreen) {
        this.workCasePrescreen = workCasePrescreen;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).
                append("id", id).
                append("caNumber", caNumber).
                append("appRefNumber", appRefNumber).
                append("wobNumber", wobNumber).
                append("lock", lock).
                append("lockUser", lockUser).
                append("step", step).
                append("status", status).
                append("customerList", customerList).
                append("createDate", createDate).
                append("createBy", createBy).
                append("modifyDate", modifyDate).
                append("modifyBy", modifyBy).
                append("stepOwner", stepOwner).
                append("workCasePrescreen", workCasePrescreen).
                toString();
    }
}
