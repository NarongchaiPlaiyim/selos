package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.Status;
import com.clevel.selos.model.db.master.Step;
import com.clevel.selos.model.db.master.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class AbstractWorkCase implements Serializable{

    @Column(name = "app_number", length = 16,nullable = false)
    protected String appNumber;

    @Column(name = "wob_number")
    protected String wobNumber;

    @Column(name = "case_lock")
    protected int lock;

    @Column(name = "lock_user")
    protected String lockUser;

    @OneToOne
    @JoinColumn(name = "step_id")
    protected Step step;

    @OneToOne
    @JoinColumn(name = "status_id")
    protected Status status;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date")
    protected Date createDate;

    @OneToOne
    @JoinColumn(name = "create_by")
    protected User createBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modify_date")
    protected Date modifyDate;

    @OneToOne
    @JoinColumn(name = "modify_by")
    protected User modifyBy;

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
}
