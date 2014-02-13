package com.clevel.selos.model.db.working;

import com.clevel.selos.model.db.master.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@MappedSuperclass
public class AbstractWorkCase implements Serializable{

    @Column(name = "app_number", length = 16,nullable = false)
    protected String appNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "received_date")
    protected Date receivedDate;

    @OneToOne
    @JoinColumn(name = "at_user_team_id")
    protected UserTeam atUserTeam;

    @OneToOne
    @JoinColumn(name = "product_group_id")
    protected ProductGroup productGroup;

    @OneToOne
    @JoinColumn(name = "request_type_id")
    protected RequestType request_type;

    @OneToOne
    @JoinColumn(name = "from_user_id")
    protected User fromUser;

    @OneToOne
    @JoinColumn(name = "at_user_id")
    protected User atUser;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "appointment_date")
    protected Date appointmentDate;

    @ManyToOne
    @JoinColumn(name = "doa_level_id")
    protected AuthorizationDOA authorizationDOA;

    @Temporal(TemporalType.TIMESTAMP)
    @JoinColumn(name = "sla_end_date")
    protected Date slaEndDate;

    @Column(name = "total_time_at_user", length = 1, columnDefinition = "int default 0")
    protected int totalTimeAtUser;

    @Column(name = "total_time_at_process", length = 1, columnDefinition = "int default 0")
    protected int totalTimeAtProcess;

    @Column(name = "wob_number")
    protected String wobNumber;

    @Column(name = "case_lock", length = 1, columnDefinition = "int default 0")
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

    @Column(name = "bpm_active", length = 1, columnDefinition = "int default 1")
    protected int bpmActive;

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
